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
 * @file $/OSISProcessor.java
 * @brief Processor for OSIS files.
 * @author Stephan Kreutzer
 * @since 2014-02-12
 */



import java.io.File;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import java.util.Map;
import java.util.HashMap;
import javax.xml.stream.events.Characters;



class OSISProcessor
{
    public OSISProcessor()
    {
    
    }

    public boolean processFile(File inOSISFile,
                               File outHaggaiFile,
                               String outTitle,
                               boolean xmlReaderDTDValidation,
                               boolean xmlReaderNamespaceProcessing,
                               boolean xmlReaderCoalesceAdjacentCharacterData,
                               boolean xmlReaderReplaceEntityReferences,
                               boolean xmlReaderResolveExternalParsedEntities,
                               boolean xmlReaderUseDTDFallback)
    {
        /**
         * @todo Maybe check besides exception handler, if in/out files
         *     are existing, readable, etc.?
         */


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            inputFactory.setProperty("javax.xml.stream.isValidating", xmlReaderDTDValidation);
            inputFactory.setProperty("javax.xml.stream.isNamespaceAware", xmlReaderNamespaceProcessing);
            inputFactory.setProperty("javax.xml.stream.isCoalescing", xmlReaderCoalesceAdjacentCharacterData);
            inputFactory.setProperty("javax.xml.stream.isReplacingEntityReferences", xmlReaderReplaceEntityReferences);
            inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", xmlReaderResolveExternalParsedEntities);
            inputFactory.setProperty("javax.xml.stream.supportDTD", xmlReaderUseDTDFallback);

            InputStream in = new FileInputStream(inOSISFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

            XMLEvent event = null;
            
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(outHaggaiFile.getAbsolutePath()),
                                    "UTF8"));

            
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<XMLBIBLE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"haggai_20130620.xsd\" biblename=\"" + outTitle + "\" status=\"v\" version=\"haggai_3.0.0.9.1\" revision=\"0\">\n");
            writer.write("  <!-- This file was created by osis2hag1 (see http://www.free-scriptures.org), which is free software licensed under the GNU Affero General Public License 3 or any later version. -->\n");
            

            String osisIDWork = null;
            boolean firstOSISText = false;
            boolean header = false;
            boolean metadata = false;
            HashMap<String, String> metadataMap = new HashMap<String, String>();
            boolean readingMetadata = false;
            String metadataMapKey = "";
            boolean body = false;
            boolean readingChapter = false;

            int bookNumber = 0;
            String chapterID = "";
            int chapterNumberCurrent = 0;
            int chapterNumberLast = 0;
            String verseID = "";
            int verseNumberCurrent = 0;
            int verseNumberLast = 0;
            
            boolean paragraphSpan = false;

            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();
                    
                    if (tagName.equalsIgnoreCase("osisText") == true)
                    {
                        if (firstOSISText == false)
                        {
                            Attribute attributeOSISIDWork = event.asStartElement().getAttributeByName(new QName("osisIDWork"));
                                
                            if (attributeOSISIDWork != null)
                            {
                                osisIDWork = attributeOSISIDWork.getValue();
                            }
                            else
                            {
                                System.out.print("osis2hag1: 'osisIDWork' of first text is missing.\n");
                                System.exit(-14);
                            }
                            
                            firstOSISText = true;
                        }
                        else
                        {
                            // No multi-text files.
                            break;
                        }
                    }
                    else if (tagName.equalsIgnoreCase("header") == true)
                    {
                        header = true;
                    }
                    else if (tagName.equalsIgnoreCase("work") == true)
                    {
                        if (header == true)
                        {
                            Attribute attributeOSISWork = event.asStartElement().getAttributeByName(new QName("osisWork"));

                            if (attributeOSISWork != null &&
                                osisIDWork != null)
                            {
                                if (attributeOSISWork.getValue().equals(osisIDWork) == true)
                                {
                                    if (metadataMap.size() > 0)
                                    {
                                        System.out.print("osis2hag1: Multiple metadata for osisIDWork '" + osisIDWork + "'.\n");
                                    }
                                
                                    metadata = true;
                                }
                            }
                        }
                    }
                    else if (metadata == true)
                    {
                        if (tagName.equalsIgnoreCase("title") == true ||
                            tagName.equalsIgnoreCase("creator") == true ||
                            tagName.equalsIgnoreCase("description") == true ||
                            tagName.equalsIgnoreCase("publisher") == true ||
                            tagName.equalsIgnoreCase("identifier") == true ||
                            tagName.equalsIgnoreCase("language") == true ||
                            tagName.equalsIgnoreCase("rights") == true ||
                            tagName.equalsIgnoreCase("scope") == true)
                        {
                            metadataMapKey = tagName;
                            readingMetadata = true;
                        }
                        else if (tagName.equalsIgnoreCase("refSystem") == true ||
                                 tagName.equalsIgnoreCase("type") == true)
                        {
                            // Ignore.
                        }
                        else
                        {
                            System.out.print("osis2hag1: Metadata element '" + tagName + "' omitted.\n");
                        }
                    }
                    else if (tagName.equalsIgnoreCase("div") == true)
                    {
                        if (body == true)
                        {
                            Attribute attributeType = event.asStartElement().getAttributeByName(new QName("type"));

                            if (attributeType != null)
                            {
                                if (attributeType.getValue().equals("book") == true)
                                {
                                    verseNumberCurrent = 0;
                                    verseNumberLast = 0;
                                    chapterNumberCurrent = 0;
                                    chapterNumberLast = 0;
                                    bookNumber += 1;
                                
                                    if (bookNumber == 1)
                                    {
                                        writer.write("  <BIBLEBOOK bnumber=\"" + bookNumber + "\">\n");
                                    }
                                    else
                                    {
                                        writer.write("  </BIBLEBOOK>\n");
                                        writer.write("  <BIBLEBOOK bnumber=\"" + bookNumber + "\">\n");
                                    }
                                }
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("chapter") == true)
                    {
                        verseNumberCurrent = 0;
                        verseNumberLast = 0;
                    
                        Attribute attributeStartID = event.asStartElement().getAttributeByName(new QName("sID"));
                        Attribute attributeEndID = event.asStartElement().getAttributeByName(new QName("eID"));
                        
                        if (attributeStartID != null)
                        {
                            Attribute attributeNumber = event.asStartElement().getAttributeByName(new QName("n"));

                            if (attributeNumber != null)
                            {
                                String attributeNumberString = attributeNumber.getValue();

                                if (attributeNumberString.contains("-") == true)
                                {
                                    String[] span = attributeNumberString.split("-");
                                    
                                    writer.write("    <!-- This chapter spans from " + span[0] + " to " + span[1] + " -->\n");
                                    
                                    attributeNumberString = span[0];
                                }
                                
                                chapterNumberCurrent = Integer.parseInt(attributeNumberString);
                            }
                            else
                            {
                                chapterNumberCurrent = chapterNumberLast + 1;
                            }

                            if (chapterNumberLast < chapterNumberCurrent)
                            {
                                chapterNumberLast = chapterNumberCurrent;
                            }
                            else
                            {
                                System.out.print("osis2hag1: Book " + bookNumber + ": chapter " + chapterNumberCurrent + " detected after chapter " + chapterNumberLast + " - output continues with " + (chapterNumberLast + 1) + ".\n");

                                chapterNumberCurrent = chapterNumberLast + 1;
                                chapterNumberLast = chapterNumberCurrent;
                            }

                            chapterID = attributeStartID.getValue();
                            
                            writer.write("    <CHAPTER cnumber=\"" + chapterNumberCurrent + "\">\n");
                        }
                        else if (attributeEndID != null)
                        {
                            chapterID = "";
                            chapterNumberCurrent = 0;
                        
                            writer.write("    </CHAPTER>\n");
                        }
                        else
                        {
                            readingChapter = true;
                            
                            Attribute attributeNumber = event.asStartElement().getAttributeByName(new QName("n"));

                            if (attributeNumber != null)
                            {
                                String attributeNumberString = attributeNumber.getValue();

                                if (attributeNumberString.contains("-") == true)
                                {
                                    String[] span = attributeNumberString.split("-");
                                    
                                    writer.write("    <!-- This chapter spans from " + span[0] + " to " + span[1] + " -->\n");
                                    
                                    attributeNumberString = span[0];
                                }
                                
                                chapterNumberCurrent = Integer.parseInt(attributeNumberString);
                            }
                            else
                            {
                                chapterNumberCurrent = chapterNumberLast + 1;
                            }
                            
                            if (chapterNumberLast < chapterNumberCurrent)
                            {
                                chapterNumberLast = chapterNumberCurrent;
                            }
                            else
                            {
                                System.out.print("osis2hag1: Book " + bookNumber + ": chapter " + chapterNumberCurrent + " detected after chapter " + chapterNumberLast + " - output continues with " + (chapterNumberLast + 1) + ".\n");

                                chapterNumberCurrent = chapterNumberLast + 1;
                                chapterNumberLast = chapterNumberCurrent;
                            }

                            writer.write("    <CHAPTER cnumber=\"" + chapterNumberCurrent + "\">\n");
                        }
                    }
                    else if (tagName.equalsIgnoreCase("p") == true)
                    {
                        if (verseID.isEmpty() == true)
                        {
                            if (paragraphSpan == true)
                            {
                                writer.write("      </PARAGRAPH>\n");
                            }
                            
                            paragraphSpan = false;

                            writer.write("      <PARAGRAPH>\n");
                        }
                        else
                        {
                            paragraphSpan = true;

                            writer.write("<!-- Paragraph beginning within a verse. -->");
                        }
                    }
                    else if (tagName.equalsIgnoreCase("verse") == true)
                    {
                        Attribute attributeStartID = event.asStartElement().getAttributeByName(new QName("sID"));
                        Attribute attributeEndID = event.asStartElement().getAttributeByName(new QName("eID"));
                        
                        if (attributeStartID != null)
                        {
                            Attribute attributeNumber = event.asStartElement().getAttributeByName(new QName("n"));

                            if (attributeNumber != null)
                            {
                                String attributeNumberString = attributeNumber.getValue();
                                
                                if (attributeNumberString.contains("-") == true)
                                {
                                    String[] span = attributeNumberString.split("-");
                                    
                                    writer.write("        <!-- This verse spans from " + span[0] + " to " + span[1] + " -->\n");
                                    
                                    attributeNumberString = span[0];
                                }

                                verseNumberCurrent = Integer.parseInt(attributeNumberString);
                            }
                            else
                            {
                                verseNumberCurrent = verseNumberLast + 1;
                            }
                            
                            if (verseNumberLast < verseNumberCurrent)
                            {
                                verseNumberLast = verseNumberCurrent;
                            }
                            else
                            {
                                System.out.print("osis2hag1: Book " + bookNumber + ", chapter " + chapterNumberCurrent + ": verse " + verseNumberCurrent + " detected after verse " + verseNumberLast + " - output continues with " + (verseNumberLast + 1) + ".\n");

                                verseNumberCurrent = verseNumberLast + 1;
                                verseNumberLast = verseNumberCurrent;
                            }
                            
                            verseID = attributeStartID.getValue();
                            
                            writer.write("        <VERSE vnumber=\"" + verseNumberCurrent + "\">");
                        }
                        else if (attributeEndID != null)
                        {
                            verseID = "";
                            verseNumberCurrent = 0;
                        
                            writer.write("</VERSE>\n");
                        }
                        else
                        {
                        
                        }
                    }
                    else if (tagName.equalsIgnoreCase("note") == true)
                    {
                        writer.write("<NOTE>");
                        
                        Attribute attributeType = event.asStartElement().getAttributeByName(new QName("type"));
                        
                        if (attributeType != null)
                        {
                            String type = attributeType.getValue();
                            
                            /** @todo Map footnote type to Haggai XML footnote types. */
                        }
                    }
                }
                else if (event.isEndElement() == true)
                {
                    String tagName = event.asEndElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("osisText") == true)
                    {
                        // No multi-text files.
                        firstOSISText = false;
                        break;
                    }
                    else if (tagName.equalsIgnoreCase("header") == true)
                    {
                        header = false;
                        body = true;
                    }
                    else if (tagName.equalsIgnoreCase("work") == true)
                    {
                        if (header == true)
                        {
                            if (metadata == true)
                            {
                                if (metadataMap.size() <= 0)
                                {
                                    System.out.print("osis2hag1: No metadata.\n");
                                    System.exit(-15);
                                }
                                

                                writer.write("  <INFORMATION>\n");
                                
                                if (metadataMap.containsKey("title") == true)
                                {
                                    writer.write("    <title>" + metadataMap.get("title") + "</title>\n");
                                }
                                
                                if (metadataMap.containsKey("creator") == true)
                                {
                                    writer.write("    <creator>" + metadataMap.get("creator") + "</creator>\n");
                                }
                                
                                if (metadataMap.containsKey("description") == true)
                                {
                                    writer.write("    <description>" + metadataMap.get("description") + "</description>\n");
                                }
                                
                                if (metadataMap.containsKey("publisher") == true)
                                {
                                    writer.write("    <publisher>" + metadataMap.get("publisher") + "</publisher>\n");
                                }
                                
                                // date
                                
                                writer.write("    <type>Text</type>\n");
                                writer.write("    <format>Haggai XML Bible Markup Language</format>\n");
                                
                                if (metadataMap.containsKey("identifier") == true)
                                {
                                    writer.write("    <identifier>" + metadataMap.get("identifier") + "</identifier>\n");
                                }
                                
                                if (metadataMap.containsKey("language") == true)
                                {
                                    writer.write("    <language>" + metadataMap.get("language") + "</language>\n");
                                }
                                
                                if (metadataMap.containsKey("scope") == true)
                                {
                                    writer.write("    <coverage>" + metadataMap.get("scope") + "</coverage>\n");
                                }
                                
                                if (metadataMap.containsKey("rights") == true)
                                {
                                    writer.write("    <rights>" + metadataMap.get("rights") + "</rights>\n");
                                }

                                writer.write("  </INFORMATION>\n");
                            }
                            
                            metadata = false;
                        }
                    }
                    else if (metadata == true)
                    {
                        if (readingMetadata == true &&
                            tagName.equalsIgnoreCase(metadataMapKey) == true)
                        {
                            readingMetadata = false;
                            metadataMapKey = "";
                        } 
                    }
                    else if (tagName.equalsIgnoreCase("chapter") == true)
                    {
                        if (readingChapter == true)
                        {
                            readingChapter = false;

                            verseID = "";
                            verseNumberCurrent = 0;
                            verseNumberLast = 0;
                            chapterID = "";
                            chapterNumberCurrent = 0;
                        
                            writer.write("    </CHAPTER>\n");
                        }
                    }
                    else if (tagName.equalsIgnoreCase("p") == true)
                    {
                        if (verseID.isEmpty() == true)
                        {
                            paragraphSpan = false;
                        
                            writer.write("      </PARAGRAPH>\n");
                        }
                        else
                        {
                            paragraphSpan = true;

                            writer.write("<!-- Paragraph ending within a verse. -->");
                        }
                    }
                    else if (tagName.equalsIgnoreCase("note") == true)
                    {
                        writer.write("</NOTE>");
                    }
                }
                else if (event.isCharacters() == true)
                {
                    if (readingMetadata == true)
                    {
                        if (metadataMap.containsKey(metadataMapKey) == true)
                        {
                            //metadataMap.put(metadataMapKey, metadataMap.get(metadataMapKey) + "<separator/>" + event.asCharacters().getData());
                            metadataMap.put(metadataMapKey, metadataMap.get(metadataMapKey) + "; " + event.asCharacters().getData());
                        }
                        else
                        {
                            metadataMap.put(metadataMapKey, event.asCharacters().getData());
                        }
                    }
                    else if (body == true)
                    {
                        if (verseID.isEmpty() == false)
                        {
                            event.writeAsEncodedUnicode(writer);
                        }
                    }
                }
            }


            if (bookNumber > 0)
            {
                writer.write("  </BIBLEBOOK>\n");
            }

            writer.write("</XMLBIBLE>\n");
            
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-16);
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            System.exit(-17);
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-18);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-19);
        }

        return true;
    }
}

