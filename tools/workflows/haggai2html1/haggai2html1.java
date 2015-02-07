/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of haggai2html1 workflow.
 *
 * haggai2html1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * haggai2html1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with haggai2html1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/haggai2html1.java
 * @brief Workflow to automatically convert a Haggai XML file to XHTML.
 * @author Stephan Kreutzer
 * @since 2015-02-06
 */



import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/*

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;
*/



public class haggai2html1
{
    public static void main(String args[])
    {
        System.out.print("haggai2html1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        String programPath = haggai2html1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        File haggaiFile = null;

        if (args.length >= 1)
        {
            haggaiFile = new File(args[0]);
        }
        else
        {
            ProcessBuilder builder = new ProcessBuilder("java", "file_picker1", programPath + "config_file_picker1.xml");
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
                            haggaiFile = new File(tokenizer.nextToken());
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

        if (haggaiFile == null)
        {
            System.out.println("haggai2html1 workflow: No input Haggai XML file specified.");
            System.exit(-1);
        }

        if (haggaiFile.exists() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        File haggaiSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "haggai_20130620.xsd");
        
        if (haggaiSchema.exists() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiSchema.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML Schema Path '" + haggaiSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiSchema.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        boolean validHaggaiXML = false;

        ProcessBuilder builder = new ProcessBuilder("java", "schemavalidator1", haggaiFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_empty.xml", haggaiSchema.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "config_schemata_haggai_20130620.xml");
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
                    validHaggaiXML = true;
                }
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (validHaggaiXML != true)
        {
            System.out.println("haggai2html1 workflow: '" + haggaiFile.getAbsolutePath() + "' isn't a valid Haggai XML file according to the Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "'.");
            System.exit(1);
        }


        File transformationFile = null;

        if (args.length >= 2)
        {
            transformationFile = new File(args[1]);
        }
        else
        {
            builder = new ProcessBuilder("java", "haggai2html1_workflow_xslt_picker1", programPath + "gui" + File.separator + "haggai2html1_workflow_xslt_picker1" + File.separator + "config.xml");
            builder.directory(new File(programPath + "gui" + File.separator + "haggai2html1_workflow_xslt_picker1"));
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
                            transformationFile = new File(tokenizer.nextToken());
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

        if (transformationFile == null)
        {
            System.out.println("haggai2html1 workflow: No transformation file specified.");
            System.exit(-1);
        }

        if (transformationFile.exists() != true)
        {
            System.out.print("haggai2html1 workflow: Transformation file '" + transformationFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (transformationFile.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: Transformation path '" + transformationFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (transformationFile.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: Transformation file '" + transformationFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("haggai2html1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() != true)
            {
                System.out.print("haggai2html1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-1);
            }
        }

        builder = new ProcessBuilder("java", "xsltransformator1", haggaiFile.getAbsolutePath(), transformationFile.getAbsolutePath(), tempDirectory.getAbsolutePath() + File.separator + "html.html");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "xsltransformator" + File.separator + "xsltransformator1"));
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


/*
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

        File haggaiSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "haggai_20130620.xsd");
        
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
        
        CopyFileBinary(haggaiFile, outFile);*/
    }
/*
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
    }*/
}
