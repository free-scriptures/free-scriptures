/* Copyright (C) 2014-2015  Stephan Kreutzer
 *
 * This file is part of osis2haggai1.
 *
 * osis2haggai1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * osis2haggai1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with osis2haggai1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/osis2haggai1.java
 * @author Stephan Kreutzer
 * @since 2014-02-10
 */



import java.io.File;
import javax.xml.validation.SchemaFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.XMLConstants;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;



class osis2haggai1
{
    public static void main(String args[])
    {
        System.out.print("osis2haggai1  Copyright (C) 2014-2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");

        if (args.length != 1)
        {
            System.out.print("Usage:\n" +
                             "\tosis2haggai1 config-file\n\n");
            System.exit(1);
        }


        ConfigProcessor configuration = new ConfigProcessor(args[0]);
        configuration.run();

        File inOSISFile = configuration.GetInOSISFile();
        File outHaggaiFile = configuration.GetOutHaggaiFile();

        if (configuration.GetOSISSchemaValidation() == true)
        {
            // Note that a syntactical check isn't sufficient for OSIS, since
            // OSIS is based upon the marker concept, so there would be also a
            // semantical check needed.

            System.out.print("osis2haggai1: Validating '" + inOSISFile.getAbsolutePath() + "'.\n");

            String osisSchemaPath = osis2haggai1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            try
            {
                osisSchemaPath = new File(osisSchemaPath).getCanonicalPath() + File.separator;
                osisSchemaPath = URLDecoder.decode(osisSchemaPath, "UTF-8");
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

            osisSchemaPath += "osisCore.2.1.1.xsd";

            File osisSchema = new File(osisSchemaPath);

            if (osisSchema.exists() != true)
            {
                osisSchema = null;
            }

            if (osisSchema != null)
            {
                if (osisSchema.isFile() != true)
                {
                    osisSchema = null;
                }
            }

            if (osisSchema != null)
            {
                if (osisSchema.canRead() != true)
                {
                    osisSchema = null;
                }
            }

            if (osisSchema != null)
            {
                try
                {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Source schemaSource = new StreamSource(osisSchema);
                    Schema schema = schemaFactory.newSchema(schemaSource);
                    Source fileSource = new StreamSource(inOSISFile);
                    Validator validator = schema.newValidator();

                    validator.validate(fileSource);
                }
                catch (SAXException ex)
                {
                    ex.printStackTrace();
                    System.exit(-11);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    System.exit(-12);
                }
            }
            else
            {
                System.out.print("osis2haggai1: Can't validate OSIS file - schema 'osisCore.2.1.1.xsd' is missing or inaccessible.\n");
                System.exit(-13);
            }
        }

        String outTitle = configuration.GetOutTitle();
        boolean xmlReaderDTDValidation = configuration.GetXMLReaderDTDValidation();
        boolean xmlReaderNamespaceProcessing = configuration.GetXMLReaderNamespaceProcessing();
        boolean xmlReaderCoalesceAdjacentCharacterData = configuration.GetXMLReaderCoalesceAdjacentCharacterData();
        boolean xmlReaderReplaceEntityReferences = configuration.GetXMLReaderReplaceEntityReferences();
        boolean xmlReaderResolveExternalParsedEntities = configuration.GetXMLReaderResolveExternalParsedEntities();
        boolean xmlReaderUseDTDFallback = configuration.GetXMLReaderUseDTDFallback();



        OSISProcessor osisProcessor = new OSISProcessor();
        osisProcessor.processFile(inOSISFile,
                                  outHaggaiFile,
                                  outTitle,
                                  xmlReaderDTDValidation,
                                  xmlReaderNamespaceProcessing,
                                  xmlReaderCoalesceAdjacentCharacterData,
                                  xmlReaderReplaceEntityReferences,
                                  xmlReaderResolveExternalParsedEntities,
                                  xmlReaderUseDTDFallback);

        System.exit(0);
    }
}
