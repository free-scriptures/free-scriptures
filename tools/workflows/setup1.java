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
    

        CopyEntitiesXHTML_1_0_Strict(programPath, ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities");
        CopyEntitiesXHTML_1_0_Strict(programPath, ".." + File.separator + "xml_split" + File.separator + "xml_split1" + File.separator + "entities");
        CopyEntitiesXHTML_1_0_Strict(programPath, ".." + File.separator + "html2epub" + File.separator + "html2epub1");
    
        CopyEntitiesXHTML_1_1(programPath, ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "entities");
        CopyEntitiesXHTML_1_1(programPath, ".." + File.separator + "xml_split" + File.separator + "xml_split1" + File.separator + "entities");

        CopySchemataXHTML_1_0_Strict(programPath, ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "schemata");
        CopySchemataXHTML_1_0_Strict(programPath, ".." + File.separator + "html2epub" + File.separator + "html2epub1");

        CopySchemataXHTML_1_1(programPath, ".." + File.separator + "schemavalidator" + File.separator + "schemavalidator1" + File.separator + "schemata");

        return;
    }



    public static int CopyEntitiesXHTML_1_0_Strict(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }


        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml1-strict.dtd"), 
                            new File(programPath + to + "xhtml1-strict.dtd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-lat1.ent"), 
                            new File(programPath + to + "xhtml-lat1.ent")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-symbol.ent"), 
                            new File(programPath + to + "xhtml-symbol.ent")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-special.ent"), 
                            new File(programPath + to + "xhtml-special.ent")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }

    public static int CopyEntitiesXHTML_1_1(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }


        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11.dtd"), 
                            new File(programPath + to + "xhtml11.dtd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlstyle-1.mod"),
                            new File(programPath + to + "xhtml-inlstyle-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-datatypes-1.mod"),
                            new File(programPath + to + "xhtml-datatypes-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-framework-1.mod"),
                            new File(programPath + to + "xhtml-framework-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-qname-1.mod"),
                            new File(programPath + to + "xhtml-qname-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-events-1.mod"),
                            new File(programPath + to + "xhtml-events-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-attribs-1.mod"),
                            new File(programPath + to + "xhtml-attribs-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11-model-1.mod"),
                            new File(programPath + to + "xhtml11-model-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-charent-1.mod"),
                            new File(programPath + to + "xhtml-charent-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-lat1.ent"),
                            new File(programPath + to + "xhtml-lat1.ent")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-symbol.ent"),
                            new File(programPath + to + "xhtml-symbol.ent")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-special.ent"),
                            new File(programPath + to + "xhtml-special.ent")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-text-1.mod"),
                            new File(programPath + to + "xhtml-text-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlstruct-1.mod"),
                            new File(programPath + to + "xhtml-inlstruct-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlphras-1.mod"),
                            new File(programPath + to + "xhtml-inlphras-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkstruct-1.mod"),
                            new File(programPath + to + "xhtml-blkstruct-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkphras-1.mod"),
                            new File(programPath + to + "xhtml-blkphras-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-hypertext-1.mod"),
                            new File(programPath + to + "xhtml-hypertext-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-list-1.mod"),
                            new File(programPath + to + "xhtml-list-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-edit-1.mod"),
                            new File(programPath + to + "xhtml-edit-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-bdo-1.mod"),
                            new File(programPath + to + "xhtml-bdo-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-ruby-1.mod"),
                            new File(programPath + to + "xhtml-ruby-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-pres-1.mod"),
                            new File(programPath + to + "xhtml-pres-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlpres-1.mod"),
                            new File(programPath + to + "xhtml-inlpres-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkpres-1.mod"),
                            new File(programPath + to + "xhtml-blkpres-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-link-1.mod"),
                            new File(programPath + to + "xhtml-link-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-meta-1.mod"),
                            new File(programPath + to + "xhtml-meta-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-base-1.mod"),
                            new File(programPath + to + "xhtml-base-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-script-1.mod"),
                            new File(programPath + to + "xhtml-script-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-style-1.mod"),
                            new File(programPath + to + "xhtml-style-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-image-1.mod"),
                            new File(programPath + to + "xhtml-image-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-csismap-1.mod"),
                            new File(programPath + to + "xhtml-csismap-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-ssismap-1.mod"),
                            new File(programPath + to + "xhtml-ssismap-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-param-1.mod"),
                            new File(programPath + to + "xhtml-param-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-object-1.mod"),
                            new File(programPath + to + "xhtml-object-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-table-1.mod"),
                            new File(programPath + to + "xhtml-table-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-form-1.mod"),
                            new File(programPath + to + "xhtml-form-1.mod")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-struct-1.mod"),
                            new File(programPath + to + "xhtml-struct-1.mod")) != 0)
        {
            System.exit(-1);
        }
        
        return 0;
    }

    public static int CopySchemataXHTML_1_0_Strict(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }


        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml1-strict.xsd"), 
                            new File(programPath + to + "xhtml1-strict.xsd")) != 0)
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

    public static int CopySchemataXHTML_1_1(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }


        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11.xsd"), 
                            new File(programPath + to + "xhtml11.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11-model-1.xsd"), 
                            new File(programPath + to + "xhtml11-model-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-datatypes-1.xsd"), 
                            new File(programPath + to + "xhtml-datatypes-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml11-modules-1.xsd"), 
                            new File(programPath + to + "xhtml11-modules-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-framework-1.xsd"), 
                            new File(programPath + to + "xhtml-framework-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-attribs-1.xsd"), 
                            new File(programPath + to + "xhtml-attribs-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-text-1.xsd"), 
                            new File(programPath + to + "xhtml-text-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkphras-1.xsd"), 
                            new File(programPath + to + "xhtml-blkphras-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkstruct-1.xsd"), 
                            new File(programPath + to + "xhtml-blkstruct-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlphras-1.xsd"), 
                            new File(programPath + to + "xhtml-inlphras-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlstruct-1.xsd"), 
                            new File(programPath + to + "xhtml-inlstruct-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-hypertext-1.xsd"), 
                            new File(programPath + to + "xhtml-hypertext-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-list-1.xsd"), 
                            new File(programPath + to + "xhtml-list-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-struct-1.xsd"), 
                            new File(programPath + to + "xhtml-struct-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-edit-1.xsd"), 
                            new File(programPath + to + "xhtml-edit-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-bdo-1.xsd"), 
                            new File(programPath + to + "xhtml-bdo-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-pres-1.xsd"), 
                            new File(programPath + to + "xhtml-pres-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-blkpres-1.xsd"), 
                            new File(programPath + to + "xhtml-blkpres-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlpres-1.xsd"), 
                            new File(programPath + to + "xhtml-inlpres-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-link-1.xsd"), 
                            new File(programPath + to + "xhtml-link-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-meta-1.xsd"), 
                            new File(programPath + to + "xhtml-meta-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-base-1.xsd"), 
                            new File(programPath + to + "xhtml-base-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-script-1.xsd"), 
                            new File(programPath + to + "xhtml-script-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-style-1.xsd"), 
                            new File(programPath + to + "xhtml-style-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-inlstyle-1.xsd"), 
                            new File(programPath + to + "xhtml-inlstyle-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-image-1.xsd"), 
                            new File(programPath + to + "xhtml-image-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-csismap-1.xsd"), 
                            new File(programPath + to + "xhtml-csismap-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-ssismap-1.xsd"), 
                            new File(programPath + to + "xhtml-ssismap-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-object-1.xsd"), 
                            new File(programPath + to + "xhtml-object-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-param-1.xsd"), 
                            new File(programPath + to + "xhtml-param-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-table-1.xsd"), 
                            new File(programPath + to + "xhtml-table-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-form-1.xsd"), 
                            new File(programPath + to + "xhtml-form-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-ruby-1.xsd"), 
                            new File(programPath + to + "xhtml-ruby-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-events-1.xsd"), 
                            new File(programPath + to + "xhtml-events-1.xsd")) != 0)
        {
            System.exit(-1);
        }

        if (setup1.CopyFile(new File(programPath + ".." + File.separator + "resources" + File.separator + "w3c" + File.separator + "xhtml-target-1.xsd"), 
                            new File(programPath + to + "xhtml-target-1.xsd")) != 0)
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
