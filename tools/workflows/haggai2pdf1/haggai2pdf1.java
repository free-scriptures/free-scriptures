/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of haggai2pdf1 workflow.
 *
 * haggai2pdf1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * haggai2pdf1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with haggai2pdf1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/haggai2pdf1.java
 * @brief Workflow to automatically convert a Haggai XML file to PDF using
 *     pdflatex.
 * @author Stephan Kreutzer
 * @since 2015-02-23
 */



import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



public class haggai2pdf1
{
    public static void main(String args[])
    {
        System.out.print("haggai2pdf1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        String programPath = haggai2pdf1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

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
            System.out.println("haggai2pdf1 workflow: No input Haggai XML file specified.");
            System.exit(-1);
        }

        if (haggaiFile.exists() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        // haggaiFile will get overwritten, outDirectory eventually too, but keep
        // the parent directory of the input Haggai XML file as a suggestion.
        File outDirectory = haggaiFile.getAbsoluteFile().getParentFile();

        File haggaiSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "haggai_20130620.xsd");
        
        if (haggaiSchema.exists() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiSchema.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML Schema Path '" + haggaiSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiSchema.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("haggai2pdf1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() == true)
            {
                if (haggai2pdf1.DeleteFileRecursively(tempDirectory) == 0)
                {
                    if (tempDirectory.mkdir() != true)
                    {
                        System.out.print("haggai2pdf1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                        System.exit(-1);
                    }
                }
                else
                {
                    System.out.println("haggai2pdf1 workflow: Can't clean '" + tempDirectory.getAbsolutePath() + "'.");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.print("haggai2pdf1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
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
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
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
            System.out.println("haggai2pdf1 workflow: '" + haggaiFile.getAbsolutePath() + "' isn't a valid Haggai XML file according to the Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "'.");
            System.exit(1);
        }


        builder = new ProcessBuilder("java", "xml_prepare4latex1", haggaiFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "latex" + File.separator + "xml_prepare4latex1" + File.separator + "entities" + File.separator + "config.xml", tempDirectory.getAbsolutePath() + File.separator + "haggai_prepared.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "latex" + File.separator + "xml_prepare4latex1"));
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

        haggaiFile = new File(tempDirectory.getAbsolutePath() + File.separator + "haggai_prepared.xml");

        if (haggaiFile.exists() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
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
                            transformationFile = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2latex" + File.separator + tokenizer.nextToken());
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
                System.out.println("haggai2pdf1 workflow: No transformation file specified.");
                System.exit(-1);
            }

            try
            {
                File transformationFileDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2latex" + File.separator);

                if (transformationFile.getParentFile().getCanonicalPath().equals(transformationFileDirectory.getCanonicalPath()) != true)
                {
                    System.out.println("haggai2pdf1 workflow: The selected transformation file '" + transformationFile.getAbsolutePath() + "' isn't located in directory '" + transformationFileDirectory.getCanonicalPath() + "'.");
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
            System.out.print("haggai2pdf1 workflow: Transformation file '" + transformationFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (transformationFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: Transformation path '" + transformationFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (transformationFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: Transformation file '" + transformationFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        builder = new ProcessBuilder("java", "xsltransformator1", haggaiFile.getAbsolutePath(), transformationFile.getAbsolutePath(), tempDirectory.getAbsolutePath() + File.separator + "latex.tex");
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

        File latexFile = new File(tempDirectory.getAbsolutePath() + File.separator + "latex.tex");

        if (latexFile.exists() != true)
        {
            System.out.print("haggai2pdf1 workflow: LaTeX file '" + latexFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (latexFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: LaTeX path '" + latexFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (latexFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: LaTeX file '" + latexFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        if (args.length >= 3)
        {
            File txtreplaceDictionary = new File(args[2]);

            if (txtreplaceDictionary.exists() != true)
            {
                System.out.print("haggai2pdf1 workflow: txtreplace1 replacement dictionary file '" + txtreplaceDictionary.getAbsolutePath() + "' doesn't exist.\n");
                System.exit(-1);
            }

            if (txtreplaceDictionary.isFile() != true)
            {
                System.out.print("haggai2pdf1 workflow: txtreplace1 replacement dictionary path '" + txtreplaceDictionary.getAbsolutePath() + "' isn't a file.\n");
                System.exit(-1);
            }

            if (txtreplaceDictionary.canRead() != true)
            {
                System.out.print("haggai2pdf1 workflow: txtreplace1 replacement dictionary file '" + txtreplaceDictionary.getAbsolutePath() + "' isn't readable.\n");
                System.exit(-1);
            }

            builder = new ProcessBuilder("java", "txtreplace1", latexFile.getAbsolutePath(), txtreplaceDictionary.getAbsolutePath(), latexFile.getAbsolutePath());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "txtreplace" + File.separator + "txtreplace1"));
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
        }

        for (int i = 0; i < 4; i++)
        {
            builder = new ProcessBuilder("pdflatex", "-halt-on-error", latexFile.getAbsolutePath());
            builder.directory(tempDirectory);
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
        }

        if (args.length >= 4)
        {
            outDirectory = new File(args[3]);
        }
        else
        {
            builder = new ProcessBuilder("java", "file_picker2", outDirectory.getAbsolutePath());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "file_picker" + File.separator + "file_picker2"));
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
                            outDirectory = new File(tokenizer.nextToken());
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

        if (outDirectory == null)
        {
            System.out.println("haggai2pdf1 workflow: No output directory specified.");
            System.exit(-1);
        }

        if (outDirectory.exists() == true)
        {
            if (outDirectory.isDirectory() != true)
            {
                System.out.print("haggai2pdf1 workflow: Output path '" + outDirectory.getAbsolutePath() + "' isn't a directory.\n");
                System.exit(-1);
            }
        }

        CopyFileBinary(latexFile, new File(outDirectory.getAbsolutePath() + File.separator + "result.tex"));

        File pdfFile = new File(tempDirectory.getAbsolutePath() + File.separator + "latex.pdf");

        if (pdfFile.exists() != true)
        {
            System.out.print("haggai2pdf1 workflow: PDF file '" + pdfFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (pdfFile.isFile() != true)
        {
            System.out.print("haggai2pdf1 workflow: PDF path '" + pdfFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (pdfFile.canRead() != true)
        {
            System.out.print("haggai2pdf1 workflow: PDF file '" + pdfFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        CopyFileBinary(pdfFile, new File(outDirectory.getAbsolutePath() + File.separator + "result.pdf"));
    }

    public static int DeleteFileRecursively(File file)
    {
        if (file.isDirectory() == true)
        {
            for (File child : file.listFiles())
            {
                if (haggai2pdf1.DeleteFileRecursively(child) != 0)
                {
                    return -1;
                }
            }
        }
        
        if (file.delete() != true)
        {
            System.out.println("haggai2pdf1 workflow: Can't delete '" + file.getAbsolutePath() + "'.");
            return -1;
        }
    
        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("haggai2pdf1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("haggai2pdf1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("haggai2pdf1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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
