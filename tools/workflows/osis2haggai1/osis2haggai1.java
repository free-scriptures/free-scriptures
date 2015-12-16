/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of osis2haggai1 workflow.
 *
 * osis2haggai1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * osis2haggai1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with osis2haggai1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/osis2haggai1/osis2haggai1.java
 * @brief Workflow to automatically process an OSIS file to Haggai XML.
 * @author Stephan Kreutzer
 * @since 2015-02-03
 */



import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;
import java.net.URLDecoder;



public class osis2haggai1
{
    public static void main(String args[])
    {
        System.out.print("osis2haggai1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        String programPath = osis2haggai1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
            programPath = URLDecoder.decode(programPath, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        File osisFile = null;

        if (args.length >= 1)
        {
            osisFile = new File(args[0]);
        }
        else
        {
            ProcessBuilder builder = new ProcessBuilder("java", "file_picker1", programPath + "config_file_picker1_in.xml");
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "file_picker" + File.separator + "file_picker1"));
            builder.redirectErrorStream(true);

            try
            {
                Process process = builder.start();
                Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");
                
                while (scanner.hasNext() == true)
                {
                    String line = scanner.next();
                    
                    System.out.println(line);
                    
                    if (line.contains("' selected.") == true)
                    {
                        StringTokenizer tokenizer = new StringTokenizer(line, "'");
                        
                        if (tokenizer.countTokens() >= 2)
                        {
                            tokenizer.nextToken();
                            osisFile = new File(tokenizer.nextToken());
                        }
                    }
                }
                
                scanner.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        if (osisFile == null)
        {
            System.out.println("osis2haggai1 workflow: No input OSIS file specified.");
            System.exit(-1);
        }

        if (osisFile.exists() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (osisFile.isFile() != true)
        {
            System.out.print("osis2haggai1 workflow: Path '" + osisFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisFile.canRead() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        File osisSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "bibletechnologies.org" + File.separator + "osisCore.2.1.1.xsd");
        
        if (osisSchema.exists() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS Schema file '" + osisSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (osisSchema.isFile() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS Schema Path '" + osisSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisSchema.canRead() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS Schema file '" + osisSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("osis2haggai1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() != true)
            {
                System.out.print("osis2haggai1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-1);
            }
        }


        ProcessBuilder builder = new ProcessBuilder("java", "bommanager1", osisFile.getAbsolutePath(), "remove", tempDirectory.getAbsolutePath() + File.separator + "osis.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "bommanager" + File.separator + "bommanager1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();

                System.out.println(line);
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        osisFile = new File(tempDirectory.getAbsolutePath() + File.separator + "osis.xml");

        if (osisFile.exists() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (osisFile.isFile() != true)
        {
            System.out.print("osis2haggai1 workflow: Path '" + osisFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisFile.canRead() != true)
        {
            System.out.print("osis2haggai1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        boolean validOSIS = false;

        builder = new ProcessBuilder("java", "schemavalidator1", osisFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_empty.xml", osisSchema.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "bibletechnologies.org" + File.separator + "config_schemata_osis_core_2_1_1.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();

                System.out.println(line);

                if (line.contains("schemavalidator1: Valid.") == true)
                {
                    validOSIS = true;
                }
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (validOSIS != true)
        {
            System.out.println("osis2haggai1 workflow: '" + osisFile.getAbsolutePath() + "' isn't a valid OSIS file according to the OSIS Schema file '" + osisSchema.getAbsolutePath() + "'.");
            System.exit(1);
        }


        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(new File(tempDirectory.getAbsolutePath() + File.separator + "config_osis2haggai1.xml")),
                                    "UTF8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was generated by osis2haggai1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/free-scriptures/free-scriptures/ and http://www.free-scriptures.org). -->\n");
            writer.write("<osis2haggai1-config>\n");
            writer.write("  <in>\n");
            writer.write("    <inOSISFile>" + osisFile.getAbsolutePath() + "</inOSISFile>\n");
            writer.write("    <osisSchemaValidation>false</osisSchemaValidation>\n");
            writer.write("    <xmlReaderDTDValidation>false</xmlReaderDTDValidation>\n");
            writer.write("    <xmlReaderNamespaceProcessing>true</xmlReaderNamespaceProcessing>\n");
            writer.write("    <xmlReaderCoalesceAdjacentCharacterData>true</xmlReaderCoalesceAdjacentCharacterData>\n");
            writer.write("    <xmlReaderResolveExternalParsedEntities>true</xmlReaderResolveExternalParsedEntities>\n");
            writer.write("    <xmlReaderUseDTDFallback>false</xmlReaderUseDTDFallback>\n");
            writer.write("  </in>\n");
            writer.write("  <out>\n");
            writer.write("    <outHaggaiFile>./haggai.xml</outHaggaiFile>\n");
            writer.write("    <title>Bible text in Haggai XML</title>\n");
            writer.write("  </out>\n");
            writer.write("</osis2haggai1-config>\n");

            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }


        builder = new ProcessBuilder("java", "osis2haggai1", tempDirectory.getAbsolutePath() + File.separator + "config_osis2haggai1.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "osis2haggai" + File.separator + "osis2haggai1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");
            
            while (scanner.hasNext() == true)
            {
                System.out.println(scanner.next());
            }
            
            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }


        File haggaiFile = new File(tempDirectory.getAbsolutePath() + File.separator + "haggai.xml");

        if (haggaiFile.exists() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File haggaiSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "haggai_20150516.xsd");
        
        if (haggaiSchema.exists() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiSchema.isFile() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML Schema Path '" + haggaiSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiSchema.canRead() != true)
        {
            System.out.print("osis2haggai1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        boolean validHaggai = false;

        builder = new ProcessBuilder("java", "schemavalidator1", haggaiFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_empty.xml", haggaiSchema.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "config_schemata_haggai_20130620.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                String line = scanner.next();

                System.out.println(line);

                if (line.contains("schemavalidator1: Valid.") == true)
                {
                    validHaggai = true;
                }
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (validHaggai != true)
        {
            System.out.println("osis2haggai1 workflow: The conversion from OSIS file '" + osisFile.getAbsolutePath() + "' to Haggai XML failed.");
            System.exit(1);
        }


        File outFile = null;

        if (args.length >= 2)
        {
            outFile = new File(args[1]);
        }
        else
        {
            builder = new ProcessBuilder("java", "file_picker1", programPath + "config_file_picker1_out.xml", osisFile.getAbsoluteFile().getParent());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "file_picker" + File.separator + "file_picker1"));
            builder.redirectErrorStream(true);

            try
            {
                Process process = builder.start();
                Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");
                
                while (scanner.hasNext() == true)
                {
                    String line = scanner.next();
                    
                    System.out.println(line);
                    
                    if (line.contains("' selected.") == true)
                    {
                        StringTokenizer tokenizer = new StringTokenizer(line, "'");
                        
                        if (tokenizer.countTokens() >= 2)
                        {
                            tokenizer.nextToken();
                            outFile = new File(tokenizer.nextToken());
                        }
                    }
                }
                
                scanner.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        if (outFile == null)
        {
            System.out.println("osis2haggai1 workflow: No output Haggai XML file specified.");
            System.exit(-1);
        }

        if (outFile.exists() == true)
        {
            if (outFile.isFile() != true)
            {
                System.out.print("osis2haggai1 workflow: Haggai XML output path '" + outFile.getAbsolutePath() + "' isn't a file.\n");
                System.exit(-1);
            }
        }
        
        CopyFileBinary(haggaiFile, outFile);
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("osis2haggai1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("osis2haggai1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("osis2haggai1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
            return -3;
        }
    
    
        byte[] buffer = new byte[1024];

        try
        {
            to.createNewFile();

            FileInputStream reader = new FileInputStream(from);
            FileOutputStream writer = new FileOutputStream(to);
            
            int bytesRead = reader.read(buffer, 0, buffer.length);
            
            while (bytesRead > 0)
            {
                writer.write(buffer, 0, bytesRead);
                bytesRead = reader.read(buffer, 0, buffer.length);
            }
            
            writer.close();
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
    
        return 0;
    }
}
