/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of Free Scriptures.
 *
 * Free Scriptures is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * Free Scriptures is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with Free Scriptures. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/setup1.java
 * @brief Sets up the various tools that get called by the processing workflows.
 * @author Stephan Kreutzer
 * @since 2015-02-03
 */



import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;



public class setup1
{
    public static void main(String args[])
    {
        System.out.print("setup1  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "or the project website http://www.free-scriptures.org.\n\n");
    
        String programPath = setup1.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    
    
        // CopySchemataOSISCore_2_1_1(programPath, ".." + File.separator + "workflows" + File.separator + "osis2html1" + File.separator + "schemata" + File.separator);

        return;
    }

    public static int CopySchemataOSISCore_2_1_1(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true)
        {
            programPath += "/";
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true)
        {
            to += "/";
        }


        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "bibletechnologies.org" + File.separator + "osisCore.2.1.1.xsd"), 
                            new File(programPath + to + "osisCore.2.1.1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xml.xsd"), 
                            new File(programPath + to + "xml.xsd")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }

    public static int CopyFile(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
            return -3;
        }


        char[] buffer = new char[1024];

        try
        {
            to.createNewFile();
        
            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(
                                    new FileInputStream(from),
                                    "UTF8"));
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(to),
                                    "UTF8"));
            int charactersRead = reader.read(buffer, 0, buffer.length);

            while (charactersRead > 0)
            {
                writer.write(buffer, 0, charactersRead);
                charactersRead = reader.read(buffer, 0, buffer.length);
            }
            
            writer.close();
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-19);
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-20);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-21);
        }
    
        return 0;
    }
}
