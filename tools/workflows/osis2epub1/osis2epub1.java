/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of osis2epub1 workflow.
 *
 * osis2epub1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * osis2epub1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with osis2epub1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/osis2epub1/osis2epub1.java
 * @brief Workflow to automatically process an OSIS file to EPUB2.
 * @author Stephan Kreutzer
 * @since 2015-03-16
 */



import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.IOException;



public class osis2epub1
{
    public static void main(String args[])
    {
        System.out.print("osis2epub1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        String programPath = osis2epub1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
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
            System.out.println("osis2epub1 workflow: No input OSIS file specified.");
            System.exit(-1);
        }

        if (osisFile.exists() != true)
        {
            System.out.print("osis2epub1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (osisFile.isFile() != true)
        {
            System.out.print("osis2epub1 workflow: Path '" + osisFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisFile.canRead() != true)
        {
            System.out.print("osis2epub1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("osis2epub1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() != true)
            {
                System.out.print("osis2epub1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-1);
            }
        }

        ProcessBuilder builder = new ProcessBuilder("java", "osis2haggai1", osisFile.getAbsolutePath(), tempDirectory.getAbsolutePath() + File.separator + "haggai.xml");
        builder.directory(new File(programPath + ".." + File.separator + "osis2haggai1"));
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

        File haggaiFile = new File(tempDirectory.getAbsolutePath() + File.separator + "haggai.xml");

        if (haggaiFile.exists() != true)
        {
            System.out.print("osis2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("osis2epub1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("osis2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        String backend = new String();

        if (args.length >= 2)
        {
            backend = args[1];
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
                            backend = tokenizer.nextToken();
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

        if (backend.equalsIgnoreCase("html2epub1") == true)
        {
            builder = null;

            if (args.length >= 3)
            {
                // transformation-directory out-file

                if (args.length >= 4)
                {
                    builder = new ProcessBuilder("java", "haggai2epub1", haggaiFile.getAbsolutePath(), args[2], args[3]);
                }
                else if (args.length == 3)
                {
                    builder = new ProcessBuilder("java", "haggai2epub1", haggaiFile.getAbsolutePath(), args[2]);
                }
            }
            else
            {
                builder = new ProcessBuilder("java", "haggai2epub1", haggaiFile.getAbsolutePath());
            }

            builder.directory(new File(programPath + ".." + File.separator + "haggai2epub1"));
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
        }
        else
        {
            System.out.println("osis2epub1 workflow: Backend '" + backend + "' isn't supported.");
            System.exit(1);
        }
    }
}
