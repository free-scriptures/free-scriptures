/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of osis2html1 workflow.
 *
 * osis2html1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * osis2html1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with osis2html1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/osis2html1/osis2html1.java
 * @brief Workflow to automatically process a OSIS file to HTML.
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
/*
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
*/



public class osis2html1
{
    public static void main(String args[])
    {
        System.out.print("osis2html1 workflow  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "or the project website http://www.free-scriptures.org.\n\n");

        String programPath = osis2html1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        File osisFile = null;

        if (args.length >= 1)
        {
            osisFile = new File(args[0]);
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
            System.out.println("osis2html1 workflow: No input OSIS file.");
            System.exit(-1);
        }

        if (osisFile.exists() != true)
        {
            System.out.print("osis2html1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (osisFile.isFile() != true)
        {
            System.out.print("osis2html1 workflow: Path '" + osisFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisFile.canRead() != true)
        {
            System.out.print("osis2html1 workflow: OSIS file '" + osisFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        File osisSchema = new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "bibletechnologies.org" + File.separator + "osisCore.2.1.1.xsd");
        
        if (osisSchema.exists() != true)
        {
            System.out.print("osis2html1 workflow: OSIS Schema file '" + osisSchema.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (osisSchema.isFile() != true)
        {
            System.out.print("osis2html1 workflow: OSIS Schema Path '" + osisSchema.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (osisSchema.canRead() != true)
        {
            System.out.print("osis2html1 workflow: OSIS Schema file '" + osisSchema.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }


        boolean validOSIS = false;

        ProcessBuilder builder = new ProcessBuilder("java", "schemavalidator1", osisFile.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities" + File.separator + "config_empty.xml", osisSchema.getAbsolutePath(), programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "bibletechnologies.org" + File.separator + "config_schemata_osis_core_2_1_1.xml");
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
            System.out.println("osis2html1 workflow: '" + osisFile.getAbsolutePath() + "' isn't a valid OSIS file according to the OSIS Schema file '" + osisSchema.getAbsolutePath() + "'.");
            System.exit(1);
        }


        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("osis2html1 workflow: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() != true)
            {
                System.out.print("osis2html1 workflow: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-1);
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(new File(tempDirectory.getAbsolutePath() + File.separator + "config_osis2hag1.xml")),
                                    "UTF8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was generated by osis2html1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/free-scriptures/free-scriptures/ and http://www.free-scriptures.org). -->\n");
            writer.write("<osis2hag1-config>\n");
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
            writer.write("</osis2hag1-config>\n");

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


        builder = new ProcessBuilder("java", "osis2hag1", tempDirectory.getAbsolutePath() + File.separator + "config_osis2hag1.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "osis2hag" + File.separator + "osis2hag1"));
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
}