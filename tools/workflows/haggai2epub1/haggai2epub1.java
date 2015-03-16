/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of haggai2epub1 workflow.
 *
 * haggai2epub1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * haggai2epub1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with haggai2epub1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/haggai2epub1.java
 * @brief Workflow to automatically convert a Haggai XML file to EPUB2.
 * @author Stephan Kreutzer
 * @since 2015-03-06
 */



import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;



public class haggai2epub1
{
    public static void main(String args[])
    {
        System.out.print("haggai2epub1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        String programPath = haggai2epub1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

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
            System.out.println("haggai2epub1 workflow: No input Haggai XML file specified.");
            System.exit(-1);
        }

        if (haggaiFile.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        // haggaiFile will get overwritten, so keep the parent directory of
        // the input Haggai XML file as a suggestion.
        File outDirectory = haggaiFile.getAbsoluteFile().getParentFile();

        File haggaiSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "free-scriptures.org" + File.separator + "haggai_20130620.xsd");

        if (haggaiSchema.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (haggaiSchema.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML Schema Path '" + haggaiSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiSchema.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("haggai2epub1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() == true)
            {
                if (haggai2epub1.DeleteFileRecursively(tempDirectory) == 0)
                {
                    if (tempDirectory.mkdir() != true)
                    {
                        System.out.print("haggai2epub1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                        System.exit(-1);
                    }
                }
                else
                {
                    System.out.println("haggai2epub1 workflow: Can't clean '" + tempDirectory.getAbsolutePath() + "'.");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.print("haggai2epub1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
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
            System.out.print("haggai2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (haggaiFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Path '" + haggaiFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (haggaiFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Haggai XML file '" + haggaiFile.getAbsolutePath() + "' isn't readable.\n");
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
            System.out.println("haggai2epub1 workflow: '" + haggaiFile.getAbsolutePath() + "' isn't a valid Haggai XML file according to the Haggai XML Schema file '" + haggaiSchema.getAbsolutePath() + "'.");
            System.exit(1);
        }


        builder = new ProcessBuilder("java", "xml_split1", haggaiFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "xml_split" + File.separator + "xml_split1" + File.separator + "entities" + File.separator + "config.xml", programPath + "config_xml_split1.xml", tempDirectory.getAbsolutePath() + File.separator + "components" + File.separator);
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "xml_split" + File.separator + "xml_split1"));
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

        File splitPortionDirectory = new File(tempDirectory.getAbsolutePath() + File.separator + "components" + File.separator);
        
        if (splitPortionDirectory.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result directory '" + splitPortionDirectory.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (splitPortionDirectory.isDirectory() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result directory path '" + splitPortionDirectory.getAbsolutePath() + "' isn't a directory.\n");
            System.exit(-1);
        }

        if (splitPortionDirectory.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result directory '" + splitPortionDirectory.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }
        
        if (splitPortionDirectory.canWrite() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result directory '" + splitPortionDirectory.getAbsolutePath() + "' isn't writable.\n");
            System.exit(-1);
        }
        
        File splitResultInfoFile = new File(splitPortionDirectory.getAbsolutePath() + File.separator + "info.xml");

        if (splitResultInfoFile.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result file '" + splitResultInfoFile.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (splitResultInfoFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result path '" + splitResultInfoFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (splitResultInfoFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Split result file '" + splitResultInfoFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        List<File> bookFilesList = new ArrayList<File>();

        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(splitResultInfoFile);
            document.getDocumentElement().normalize();


            Element root = document.getDocumentElement();

            if (root != null)
            {
                NodeList portionNodeList = root.getElementsByTagName("split-portion");
                int portionNodeListLength = portionNodeList.getLength();

                if (portionNodeListLength > 0)
                {
                    for (int i = 0; i < portionNodeListLength; i++)
                    {
                        Node portionNode = portionNodeList.item(i);

                        NamedNodeMap portionNodeAttributes = portionNode.getAttributes();
                        
                        if (portionNodeAttributes == null)
                        {
                            System.out.print("haggai2epub1 workflow: Portion entry #" + (i + 1) + " in split result file '" + splitResultInfoFile.getAbsolutePath() + "' has no attributes.\n");
                            System.exit(-1);
                        }

                        Node pathAttribute = portionNodeAttributes.getNamedItem("path");
                        
                        if (pathAttribute == null)
                        {
                            System.out.print("haggai2epub1 workflow: Portion entry #" + (i + 1) + " in split result file '" + splitResultInfoFile.getAbsolutePath() + "' is missing the 'path' attribute.\n");
                            System.exit(-1);
                        }

                        String splitPortionFileName = pathAttribute.getTextContent();
                        File splitPortionFile = new File(splitPortionDirectory.getAbsolutePath() + File.separator + splitPortionFileName);

                        if (splitPortionFile.getParentFile().getCanonicalPath().equals(splitPortionDirectory.getCanonicalPath()) != true)
                        {
                            System.out.println("haggai2epub1 workflow: The split result file '" + splitPortionFile.getAbsolutePath() + "' isn't located in directory '" + splitPortionDirectory.getCanonicalPath() + "'.");
                            System.exit(-1);
                        }

                        if (splitPortionFile.exists() != true)
                        {
                            System.out.print("haggai2epub1 workflow: Split result file '" + splitPortionFile.getAbsolutePath() + "' doesn't exist, but is referenced in '" + splitResultInfoFile.getAbsolutePath() + "'.\n");
                            System.exit(-1);
                        }

                        if (splitPortionFile.isFile() != true)
                        {
                            System.out.print("haggai2epub1 workflow: Split result path '" + splitPortionFile.getAbsolutePath() + "' isn't a file.\n");
                            System.exit(-1);
                        }

                        if (splitPortionFile.canRead() != true)
                        {
                            System.out.print("haggai2epub1 workflow: Split result file '" + splitPortionFile.getAbsolutePath() + "' isn't readable.\n");
                            System.exit(-1);
                        }

                        bookFilesList.add(splitPortionFile);
                    }
                }
                else
                {
                    System.out.print("haggai2epub1 workflow: Splitted to zero Bible books according to split result file '" + splitResultInfoFile.getAbsolutePath() + "'.\n");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.print("haggai2epub1 workflow: No root element in split result file '" + splitResultInfoFile.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (SAXException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (bookFilesList.isEmpty() == true)
        {
            System.out.println("haggai2epub1 workflow: No splitted Bible book files were generated.");
            System.exit(-1);
        }


        File transformationDirectory = null;

        if (args.length >= 2)
        {
            transformationDirectory = new File(args[1]);
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
                            transformationDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2epub" + File.separator + tokenizer.nextToken());
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

            if (transformationDirectory == null)
            {
                System.out.println("haggai2epub1 workflow: No transformation directory specified.");
                System.exit(-1);
            }

            try
            {
                File transformationParentDirectory = new File(programPath + ".." + File.separator + ".." + File.separator + "haggai2epub" + File.separator);

                if (transformationDirectory.getParentFile().getCanonicalPath().equals(transformationParentDirectory.getCanonicalPath()) != true)
                {
                    System.out.println("haggai2epub1 workflow: The selected transformation directory '" + transformationDirectory.getAbsolutePath() + "' isn't located in directory '" + transformationParentDirectory.getCanonicalPath() + "'.");
                    System.exit(-1);
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        if (transformationDirectory.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Transformation directory '" + transformationDirectory.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (transformationDirectory.isDirectory() != true)
        {
            System.out.print("haggai2epub1 workflow: Transformation path '" + transformationDirectory.getAbsolutePath() + "' isn't a directory.\n");
            System.exit(-1);
        }

        if (transformationDirectory.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Transformation directory '" + transformationDirectory.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File titleTransformationFile = new File(transformationDirectory.getAbsolutePath() + File.separator + "title.xsl");

        if (titleTransformationFile.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible title transformation file '" + titleTransformationFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (titleTransformationFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible title transformation path '" + titleTransformationFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (titleTransformationFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible title transformation file '" + titleTransformationFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File biblebookTransformationFile = new File(transformationDirectory.getAbsolutePath() + File.separator + "biblebook.xsl");

        if (biblebookTransformationFile.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible book transformation file '" + biblebookTransformationFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (biblebookTransformationFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible book transformation path '" + biblebookTransformationFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (biblebookTransformationFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: Bible book transformation file '" + biblebookTransformationFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        File html2epub1ConfigTransformationFile = new File(transformationDirectory.getAbsolutePath() + File.separator + "html2epub1_config.xsl");

        if (html2epub1ConfigTransformationFile.exists() != true)
        {
            System.out.print("haggai2epub1 workflow: html2epub1 configuration transformation file '" + html2epub1ConfigTransformationFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (html2epub1ConfigTransformationFile.isFile() != true)
        {
            System.out.print("haggai2epub1 workflow: html2epub1 configuration path '" + html2epub1ConfigTransformationFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (html2epub1ConfigTransformationFile.canRead() != true)
        {
            System.out.print("haggai2epub1 workflow: html2epub1 configuration file '" + html2epub1ConfigTransformationFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        builder = new ProcessBuilder("java", "xsltransformator1", haggaiFile.getAbsolutePath(), titleTransformationFile.getAbsolutePath(), tempDirectory.getAbsolutePath() + File.separator + "components" + File.separator + "title.xhtml");
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

        for (File bookFile : bookFilesList)
        {
            String xhtmlFileName = bookFile.getName();

            if (xhtmlFileName.lastIndexOf(".") > 0)
            {
                xhtmlFileName = xhtmlFileName.substring(0, xhtmlFileName.lastIndexOf("."));
            }

            xhtmlFileName += ".xhtml";
            File xhtmlBookFile = new File(tempDirectory.getAbsolutePath() + File.separator + "components" + File.separator + xhtmlFileName);

            builder = new ProcessBuilder("java", "xsltransformator1", bookFile.getAbsolutePath(), biblebookTransformationFile.getAbsolutePath(), xhtmlBookFile.getAbsolutePath());
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
        }

        File html2epub1ConfigFile = new File(tempDirectory.getAbsolutePath() + File.separator + "config_html2epub1.xml");

        builder = new ProcessBuilder("java", "xsltransformator1", haggaiFile.getAbsolutePath(), html2epub1ConfigTransformationFile.getAbsolutePath(), html2epub1ConfigFile.getAbsolutePath());
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


        builder = new ProcessBuilder("java", "html2epub1", html2epub1ConfigFile.getAbsolutePath());
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "html2epub" + File.separator + "html2epub1"));
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


        File outFile = null;

        if (args.length >= 3)
        {
            outFile = new File(args[2]);
        }
        else
        {
            builder = new ProcessBuilder("java", "file_picker1", programPath + "config_file_picker1_out.xml", outDirectory.getAbsolutePath());
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
            System.out.println("haggai2epub1 workflow: No output file specified.");
            System.exit(-1);
        }

        CopyFileBinary(new File(tempDirectory.getAbsolutePath() + File.separator + "out.epub"), outFile);
    }

    public static int DeleteFileRecursively(File file)
    {
        if (file.isDirectory() == true)
        {
            for (File child : file.listFiles())
            {
                if (haggai2epub1.DeleteFileRecursively(child) != 0)
                {
                    return -1;
                }
            }
        }
        
        if (file.delete() != true)
        {
            System.out.println("haggai2epub1 workflow: Can't delete '" + file.getAbsolutePath() + "'.");
            return -1;
        }

        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("haggai2epub1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("haggai2epub1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("haggai2epub1 workflow: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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
