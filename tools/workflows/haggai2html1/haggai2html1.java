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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;



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

        String programPath = haggai2html1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        File haggaiFile = null;

        if (args.length >= 1)
        {
            haggaiFile = new File(args[0]);
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


        ProcessBuilder builder = new ProcessBuilder("java", "bommanager1", haggaiFile.getAbsolutePath(), "remove", tempDirectory.getAbsolutePath() + File.separator + "haggai.xml");
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
        
        haggaiFile = new File(tempDirectory.getAbsolutePath() + File.separator + "haggai.xml");

        if (haggaiFile.exists() != true)
        {
            System.out.print("haggai2html1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
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

        boolean validHaggaiXML = false;

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
            builder = new ProcessBuilder("java", "option_picker1", programPath + "config_option_picker1.xml");
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "option_picker" + File.separator + "option_picker1"));
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
                            transformationFile = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2html" + File.separator + tokenizer.nextToken());
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

            if (transformationFile == null)
            {
                System.out.println("haggai2html1 workflow: No transformation file specified.");
                System.exit(-1);
            }

            try
            {
                File transformationFileDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2html" + File.separator);

                if (transformationFile.getParentFile().getCanonicalPath().equals(transformationFileDirectory.getCanonicalPath()) != true)
                {
                    System.out.println("haggai2html1 workflow: The selected transformation file '" + transformationFile.getAbsolutePath() + "' isn't located in directory '" + transformationFileDirectory.getCanonicalPath() + "'.");
                    System.exit(-1);
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
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

        File xhtmlFile = new File(tempDirectory.getAbsolutePath() + File.separator + "html.html");

        if (xhtmlFile.exists() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML file '" + xhtmlFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (xhtmlFile.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML path '" + xhtmlFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (xhtmlFile.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML file '" + xhtmlFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        String doctypeDeclaration = new String("<!DOCTYPE");
        int doctypePosMatching = 0;
        String doctype = new String();
    
        try
        {
            FileInputStream in = new FileInputStream(xhtmlFile);
            
            int currentByte = 0;
 
            do
            {
                currentByte = in.read();
                
                if (currentByte < 0 ||
                    currentByte > 255)
                {
                    break;
                }
                

                char currentByteCharacter = (char) currentByte;
                
                if (doctypePosMatching < doctypeDeclaration.length())
                {
                    if (currentByteCharacter == doctypeDeclaration.charAt(doctypePosMatching))
                    {
                        doctypePosMatching++;
                        doctype += currentByteCharacter;
                    }
                    else
                    {
                        doctypePosMatching = 0;
                        doctype = new String();
                    }
                }
                else
                {
                    doctype += currentByteCharacter;
                
                    if (currentByteCharacter == '>')
                    {
                        break;
                    }
                }
            
            } while (true);
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

        File xhtmlEntityResolverConfig = null;
        File xhtmlSchema = null;
        File xhtmlSchemaResolverConfig = null;

        if (doctype.contains("\"-//W3C//DTD XHTML 1.0 Strict//EN\"") == true)
        {
            xhtmlEntityResolverConfig = new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_xhtml1-strict.xml");
            xhtmlSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml1-strict.xsd");
            xhtmlSchemaResolverConfig = new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "schemata" + File.separator + "config_xhtml1-strict.xml");
        }
        else if (doctype.contains("\"-//W3C//DTD XHTML 1.1//EN\"") == true)
        {
            xhtmlSchemaResolverConfig = new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_xhtml1_1.xml");
            xhtmlSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11.xsd");
            xhtmlSchemaResolverConfig = new File(programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "schemata" + File.separator + "config_xhtml1_1.xml");
        }
        else
        {
            System.out.print("haggai2html1 workflow: Unknown XHTML version '" + doctype + "' in '" + xhtmlFile.getAbsolutePath() + "'.\n");
            System.exit(-1);
        }


        if (xhtmlEntityResolverConfig.exists() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Entity resolver configuration file '" + xhtmlEntityResolverConfig.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (xhtmlEntityResolverConfig.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Entity resolver configuration path '" + xhtmlEntityResolverConfig.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (xhtmlEntityResolverConfig.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Entity resolver configuration file '" + xhtmlEntityResolverConfig.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        if (xhtmlSchema.exists() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema file '" + xhtmlSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (xhtmlSchema.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema Path '" + xhtmlSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (xhtmlSchema.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema file '" + xhtmlSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        if (xhtmlSchemaResolverConfig.exists() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema resolver configuration file '" + xhtmlSchemaResolverConfig.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (xhtmlSchemaResolverConfig.isFile() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema resolver configuration path '" + xhtmlSchemaResolverConfig.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (xhtmlSchemaResolverConfig.canRead() != true)
        {
            System.out.print("haggai2html1 workflow: XHTML Schema resolver configuration file '" + xhtmlSchemaResolverConfig.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        boolean validXHTML = false;

        builder = new ProcessBuilder("java", "schemavalidator1", xhtmlFile.getAbsolutePath(), xhtmlEntityResolverConfig.getAbsolutePath(), xhtmlSchema.getAbsolutePath(), xhtmlSchemaResolverConfig.getAbsolutePath());
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
                    validXHTML = true;
                }
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (validXHTML != true)
        {
            System.out.println("haggai2html1 workflow: The conversion from Haggai XML file '" + haggaiFile.getAbsolutePath() + "' to XHTML failed because of XSLT stylesheet '" + transformationFile.getAbsolutePath() + "'.");
            System.exit(1);
        }


        File outFile = null;

        if (args.length >= 3)
        {
            outFile = new File(args[2]);
        }
        else
        {
            builder = new ProcessBuilder("java", "file_picker1", programPath + "config_file_picker1_out.xml", haggaiFile.getAbsoluteFile().getParent());
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
            System.out.println("haggai2html1 workflow: No output XHTML file specified.");
            System.exit(-1);
        }

        if (outFile.exists() == true)
        {
            if (outFile.isFile() != true)
            {
                System.out.print("haggai2html1 workflow: XHTML output path '" + outFile.getAbsolutePath() + "' isn't a file.\n");
                System.exit(-1);
            }
        }
        
        CopyFileBinary(xhtmlFile, outFile);
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("haggai2html1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("haggai2html1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("haggai2html1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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
