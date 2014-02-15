/* Copyright (C) 2014  Stephan Kreutzer
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
 * @file $/ConfigProcessor.java
 * @brief Processor to read the configuration file.
 * @author Stephan Kreutzer
 * @since 2014-02-10
 */



import java.io.File;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;



class ConfigProcessor
{
    public ConfigProcessor(String configFilePath)
    {
        this.configFile = new File(configFilePath);
        this.inOSISFile = null;
        this.outHaggaiFile = null;
        this.osisSchemaValidation = true;
        this.haggaiSchemaValidation = true;
        this.outTitle = "Bible text in Haggai XML";
        this.xmlReaderDTDValidation = true;
        this.xmlReaderNamespaceProcessing = true;
        this.xmlReaderCoalesceAdjacentCharacterData = false;
        this.xmlReaderResolveExternalParsedEntities = true;
        this.xmlReaderUseDTDFallback = false;
    }

    public int run()
    {
        if (this.configFile.exists() != true)
        {
            System.out.print("osis2hag1: '" + this.configFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (this.configFile.isFile() != true)
        {
            System.out.print("osis2hag1: '" + this.configFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-2);
        }

        if (this.configFile.canRead() != true)
        {
            System.out.print("osis2hag1: '" + this.configFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-3);
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("inOSISFile") == true)
                    {
                        event = eventReader.nextEvent();
                        
                        if (event.isCharacters() == true)
                        {
                            if (this.inOSISFile != null)
                            {
                                System.out.print("osis2hag1: Multiple OSIS input files specified. Last one wins.\n");
                            }
                        
                            File inFile = new File(this.configFile.getAbsoluteFile().getParent() +
                                                   System.getProperty("file.separator") +
                                                   event.asCharacters().getData());

                            if (inFile.exists() != true)
                            {
                                System.out.print("osis2hag1: '" + inFile.getAbsolutePath() + "' doesn't exist.\n");
                                System.exit(-4);
                            }

                            if (inFile.isFile() != true)
                            {
                                System.out.print("osis2hag1: '" + inFile.getAbsolutePath() + "' isn't a file.\n");
                                System.exit(-5);
                            }

                            if (inFile.canRead() != true)
                            {
                                System.out.print("osis2hag1: '" + inFile.getAbsolutePath() + "' isn't readable.\n");
                                System.exit(-6);
                            }

                            this.inOSISFile = inFile;
                        }
                    }
                    else if (tagName.equalsIgnoreCase("outHaggaiFile") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (this.outHaggaiFile != null)
                            {
                                System.out.print("osis2hag1: Multiple Haggai XML output files specified. Last one wins.\n");
                            }

                            this.outHaggaiFile = new File(this.configFile.getAbsoluteFile().getParent() +
                                                          System.getProperty("file.separator") +
                                                          event.asCharacters().getData());
                        }
                    }
                    else if (tagName.equalsIgnoreCase("title") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            this.outTitle = event.asCharacters().getData();
                        }
                    }
                    else if (tagName.equalsIgnoreCase("osisSchemaValidation") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String osisSchemaValidation = event.asCharacters().getData();

                            if (osisSchemaValidation.equalsIgnoreCase("false") == true ||
                                osisSchemaValidation.equalsIgnoreCase("0") == true)
                            {
                                this.osisSchemaValidation = false;
                            }
                            else
                            {
                                this.osisSchemaValidation = true;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("haggaiSchemaValidation") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String haggaiSchemaValidation = event.asCharacters().getData();

                            if (haggaiSchemaValidation.equalsIgnoreCase("false") == true ||
                                haggaiSchemaValidation.equalsIgnoreCase("0") == true)
                            {
                                this.haggaiSchemaValidation = false;
                            }
                            else
                            {
                                this.haggaiSchemaValidation = true;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("xmlReaderDTDValidation") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String xmlReaderDTDValidation = event.asCharacters().getData();

                            if (xmlReaderDTDValidation.equalsIgnoreCase("false") == true ||
                                xmlReaderDTDValidation.equalsIgnoreCase("0") == true)
                            {
                                this.xmlReaderDTDValidation = false;
                            }
                            else
                            {
                                this.xmlReaderDTDValidation = true;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("xmlReaderNamespaceProcessing") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String xmlReaderNamespaceProcessing = event.asCharacters().getData();

                            if (xmlReaderNamespaceProcessing.equalsIgnoreCase("false") == true ||
                                xmlReaderNamespaceProcessing.equalsIgnoreCase("0") == true)
                            {
                                this.xmlReaderNamespaceProcessing = false;
                            }
                            else
                            {
                                this.xmlReaderNamespaceProcessing = true;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("xmlReaderCoalesceAdjacentCharacterData") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String xmlReaderCoalesceAdjacentCharacterData = event.asCharacters().getData();

                            if (xmlReaderCoalesceAdjacentCharacterData.equalsIgnoreCase("true") == true ||
                                xmlReaderCoalesceAdjacentCharacterData.equalsIgnoreCase("1") == true)
                            {
                                this.xmlReaderCoalesceAdjacentCharacterData = true;
                            }
                            else
                            {
                                this.xmlReaderCoalesceAdjacentCharacterData = false;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("xmlReaderResolveExternalParsedEntities") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String xmlReaderResolveExternalParsedEntities = event.asCharacters().getData();

                            if (xmlReaderResolveExternalParsedEntities.equalsIgnoreCase("false") == true ||
                                xmlReaderResolveExternalParsedEntities.equalsIgnoreCase("0") == true)
                            {
                                this.xmlReaderResolveExternalParsedEntities = false;
                            }
                            else
                            {
                                this.xmlReaderResolveExternalParsedEntities = true;
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("xmlReaderUseDTDFallback") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            String xmlReaderUseDTDFallback = event.asCharacters().getData();

                            if (xmlReaderUseDTDFallback.equalsIgnoreCase("false") == true ||
                                xmlReaderUseDTDFallback.equalsIgnoreCase("0") == true)
                            {
                                this.xmlReaderUseDTDFallback = false;
                            }
                            else
                            {
                                this.xmlReaderUseDTDFallback = true;
                            }
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-7);
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            System.exit(-8);
        }


        if (this.inOSISFile == null)
        {
            System.out.print("osis2hag1: No OSIS input file specified.\n");
            System.exit(-9);
        }

        if (this.outHaggaiFile == null)
        {
            System.out.print("osis2hag1: No Haggai XML output file specified.\n");
            System.exit(-10);
        }

        return 0;
    }


    public File GetInOSISFile()
    {
        return this.inOSISFile;
    }

    public File GetOutHaggaiFile()
    {
        return this.outHaggaiFile;
    }
    
    public String GetOutTitle()
    {
        return this.outTitle;
    }
    
    public boolean GetOSISSchemaValidation()
    {
        return this.osisSchemaValidation;
    }
    
    public boolean GetHaggaiSchemaValidation()
    {
        return this.haggaiSchemaValidation;
    }
    
    public boolean GetXMLReaderDTDValidation()
    {
        return this.xmlReaderDTDValidation;
    }
    
    public boolean GetXMLReaderNamespaceProcessing()
    {
        return this.xmlReaderNamespaceProcessing;
    }
    
    public boolean GetXMLReaderCoalesceAdjacentCharacterData()
    {
        return this.xmlReaderCoalesceAdjacentCharacterData;
    }
    
    public boolean GetXMLReaderReplaceEntityReferences()
    {
        // Always true, XML input and output should always use UTF-8
        // character encodings.
        return true;
    }
    
    public boolean GetXMLReaderResolveExternalParsedEntities()
    {
        return this.xmlReaderResolveExternalParsedEntities;
    }
    
    public boolean GetXMLReaderUseDTDFallback()
    {
        return this.xmlReaderUseDTDFallback;
    }


    private File configFile;
    private File inOSISFile;
    private File outHaggaiFile;
    private String outTitle;
    private boolean osisSchemaValidation;
    private boolean haggaiSchemaValidation;
    private boolean xmlReaderDTDValidation;
    private boolean xmlReaderNamespaceProcessing;
    private boolean xmlReaderCoalesceAdjacentCharacterData;
    private boolean xmlReaderResolveExternalParsedEntities;
    private boolean xmlReaderUseDTDFallback;
}
