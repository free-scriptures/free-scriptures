/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of xml_split1.
 *
 * xml_split1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xml_split1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xml_split1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/xml_split1.java
 * @brief Splits a XML file into several XML output files.
 * @author Stephan Kreutzer
 * @since 2015-03-14
 */



import org.xml.sax.EntityResolver;
import java.io.File;
import org.xml.sax.InputSource;
import java.util.Map;
import java.util.HashMap;
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
import java.io.IOException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.xml.stream.XMLResolver;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import java.util.Stack;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class xml_split1
{
    public static void main(String args[])
    {
        System.out.print("xml_split1  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/publishing-systems/automated_digital_publishing/\n" +
                         "and the project website http://www.publishing-systems.org.\n\n");

        if (args.length < 4)
        {
            System.out.print("Usage:\n" +
                             "\txml_split1 in-file entities-resolver-config-file configuration-file out-directory\n\n" +
                             "Please note that xml_split1 will overwrite existing files in\n" +
                             "out-directory.\n\n");
            System.exit(1);
        }


        String programPath = xml_split1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        File entitiesDirectory = new File(programPath + "entities");
        
        if (entitiesDirectory.exists() != true)
        {
            if (entitiesDirectory.mkdir() != true)
            {
                System.out.print("xml_split1: Can't create entities directory '" + entitiesDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (entitiesDirectory.isDirectory() != true)
            {
                System.out.print("xml_split1: Entities path '" + entitiesDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-2);
            }
        }


        File inFile = new File(args[0]);

        if (inFile.exists() != true)
        {
            System.out.print("xml_split1: XML input file '" + inFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-3);
        }

        if (inFile.isFile() != true)
        {
            System.out.print("xml_split1: XML input path '" + inFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-4);
        }

        if (inFile.canRead() != true)
        {
            System.out.print("xml_split1: XML input file '" + inFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-5);
        }

        File resolverConfigFile = new File(args[1]);

        if (resolverConfigFile.exists() != true)
        {
            System.out.print("xml_split1: Entity resolver configuration file '" + resolverConfigFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-6);
        }

        if (resolverConfigFile.isFile() != true)
        {
            System.out.print("xml_split1: Entity resolver configuration path '" + resolverConfigFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-7);
        }

        if (resolverConfigFile.canRead() != true)
        {
            System.out.print("xml_split1: Entity resolver configuration file '" + resolverConfigFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-8);
        }

        File configFile = new File(args[2]);

        if (configFile.exists() != true)
        {
            System.out.print("xml_split1: Configuration file '" + configFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-6);
        }

        if (configFile.isFile() != true)
        {
            System.out.print("xml_split1: Configuration path '" + configFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-7);
        }

        if (configFile.canRead() != true)
        {
            System.out.print("xml_split1: Configuration file '" + configFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-8);
        }

        File outDirectory = new File(args[3]);
        
        if (outDirectory.exists() == true)
        {
            if (outDirectory.isDirectory() == true)
            {
                if (outDirectory.canWrite() != true)
                {
                    System.out.println("xml_split1: Can't write to '" + outDirectory.getAbsolutePath() + "'.");
                    System.exit(-9);
                }
            }
            else
            {
                System.out.println("xml_split1: '" + outDirectory.getAbsolutePath() + "' isn't a directory.");
                System.exit(-10);
            }
        }
        else
        {
            if (outDirectory.mkdir() != true)
            {
                System.out.println("xml_split1: Can't create directory '" + outDirectory.getAbsolutePath() + "'.");
                System.exit(-11);
            }
        }
        

        List<HierarchyDefinition> hierarchyDefinitions = new ArrayList<HierarchyDefinition>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

            XMLEvent event = null;


            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();
                
                if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String elementNameString = elementName.getLocalPart();

                    if (elementNameString.equalsIgnoreCase("split") == true)
                    {
                        Attribute attributeElement = event.asStartElement().getAttributeByName(new QName("element"));
                        Attribute attributeAttribute = event.asStartElement().getAttributeByName(new QName("attribute"));
                        Attribute attributeValue = event.asStartElement().getAttributeByName(new QName("value"));
                        
                        if (attributeElement == null)
                        {
                            System.out.println("xml_split1: Hierarchy definition is missing the 'element' attribute in configuration file '" + configFile.getAbsolutePath() + "'.");                            
                            continue;
                        }

                        String element = attributeElement.getValue();
                        String attribute = null;
                        String value = null;
                        
                        if (attributeAttribute != null)
                        {
                            if (attributeValue == null)
                            {
                                System.out.println("xml_split1: Hierarchy definition has 'attribute' attribute but is missing the 'value' attribute in configuration file '" + configFile.getAbsolutePath() + "'.");                            
                                continue;
                            }
                        
                            attribute = attributeAttribute.getValue();
                            value = attributeValue.getValue();
                        }

                        /**
                         * @todo Avoid that an identical definition gets inserted more than once.
                         */

                        hierarchyDefinitions.add(new HierarchyDefinition(element, attribute, value));
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-12);
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            System.exit(-13);
        }

        if (hierarchyDefinitions.isEmpty() == true)
        {
            System.out.println("xml_split1: No hierarchy definitions for splitting found in configuration file '" + configFile.getAbsolutePath() + "'.");
            System.exit(1);
        }

        try
        {
            EntityResolverLocal localResolver = new EntityResolverLocal(resolverConfigFile, entitiesDirectory);
        
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(localResolver);
            InputStream in = new FileInputStream(inFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

            XMLEvent event = null;


            BufferedWriter metaWriter = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(outDirectory.getAbsolutePath() + File.separator + "info.xml"),
                                        "UTF8"));

            metaWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            metaWriter.write("<!-- This file was generated by xml_split1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/automated_digital_publishing/ and http://www.publishing-systems.org). -->\n");
            metaWriter.write("<xml-split1-out-meta-info>\n");


            BufferedWriter writer = null;
            int currentOutFile = 0;
            
            Stack<StructureStackElement> structureStack = new Stack<StructureStackElement>();

            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String fullElementName = elementName.getLocalPart();
                    
                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }
                
                    if (writer == null)
                    {
                        boolean matched = false;
                    
                        for (HierarchyDefinition hierarchyDefinition : hierarchyDefinitions)
                        {
                            if (hierarchyDefinition.GetElement().equalsIgnoreCase(fullElementName) == true)
                            {
                                if (hierarchyDefinition.GetAttribute() == null)
                                {
                                    matched = true;
                                }
                                else
                                {
                                    String attribute = hierarchyDefinition.GetAttribute();
                                    
                                    // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                                    @SuppressWarnings("unchecked")
                                    Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();
                                    
                                    while (attributes.hasNext() == true)
                                    {  
                                        Attribute attributeFound = attributes.next();
                                        QName attributeFoundName = attributeFound.getName();
                                        String fullAttributeFoundName = attributeFoundName.getLocalPart();
                                        
                                        if (attributeFoundName.getPrefix().length() > 0)
                                        {
                                            fullAttributeFoundName = attributeFoundName.getPrefix() + ":" + fullAttributeFoundName;
                                        }
                 
                                        if (fullAttributeFoundName.equalsIgnoreCase(attribute) == true &&
                                            attributeFound.getValue().equalsIgnoreCase(hierarchyDefinition.GetValue()) == true)
                                        {
                                            matched = true;
                                            break;
                                        }
                                    }
                                }
                                
                                if (matched == true)
                                {
                                    currentOutFile++;
                                    
                                    System.out.println("xml_split1: Splitting to '" + outDirectory.getAbsolutePath() + File.separator + currentOutFile + ".xml'.");
                                    
                                    writer = new BufferedWriter(
                                             new OutputStreamWriter(
                                             new FileOutputStream(outDirectory.getAbsolutePath() + File.separator + currentOutFile + ".xml"),
                                             "UTF8"));

                                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                                    writer.write("<!-- This file was generated by xml_split1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/automated_digital_publishing/ and http://www.publishing-systems.org). -->\n");
                                    
                                    break;
                                }
                            }
                        }
                    }
                    
                    if (writer != null)
                    {
                        writer.write("<" + fullElementName);
                        
                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();
                        
                        while (attributes.hasNext() == true)
                        {  
                            Attribute attribute = attributes.next();
                            QName attributeName = attribute.getName();
                            String fullAttributeName = attributeName.getLocalPart();

                            if (attributeName.getPrefix().length() > 0)
                            {
                                fullAttributeName = attributeName.getPrefix() + ":" + fullAttributeName;
                            }

                            String attributeValue = attribute.getValue();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            attributeValue = attributeValue.replaceAll("&", "&amp;");
                            attributeValue = attributeValue.replaceAll("\"", "&quot;");
                            attributeValue = attributeValue.replaceAll("'", "&apos;");
                            attributeValue = attributeValue.replaceAll("<", "&lt;");
                            attributeValue = attributeValue.replaceAll(">", "&gt;");

                            writer.write(" " + fullAttributeName + "=\"" + attributeValue + "\"");
                        }
                        
                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Namespace> namespaces = (Iterator<Namespace>)event.asStartElement().getNamespaces();
                        
                        if (namespaces.hasNext() == true)
                        {
                            Namespace namespace = namespaces.next();
                            
                            if (namespace.isDefaultNamespaceDeclaration() == true &&
                                namespace.getPrefix().length() <= 0)
                            {
                                writer.write(" xmlns=\"" + namespace.getNamespaceURI() + "\"");
                            }
                            else
                            {
                                writer.write(" xmlns:" + namespace.getPrefix() + "=\"" + namespace.getNamespaceURI() + "\"");
                            }
                        }
                        
                        writer.write(">");
                        
                        structureStack.push(new StructureStackElement(fullElementName));
                    }
                }
                else if (event.isEndElement() == true)
                {
                    QName elementName = event.asEndElement().getName();
                    String fullElementName = elementName.getLocalPart();
                    
                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }
                    
                    if (writer != null)
                    {
                        writer.write("</" + fullElementName + ">");
                        
                        structureStack.pop();
                        
                        if (structureStack.empty() == true)
                        {
                            writer.flush();
                            writer.close();
                            
                            writer = null;
                            
                            /**
                             * @todo Write to the result meta info file the hierarchy definition
                             *     that lead to the current split portion.
                             */
                            
                            metaWriter.write("  <split-portion path=\"" + currentOutFile + ".xml\"/>\n");
                        }
                    }
                }
                else if (event.isCharacters() == true)
                {
                    if (writer != null)
                    {
                        event.writeAsEncodedUnicode(writer);
                    }
                }
            }

            metaWriter.write("</xml-split1-out-meta-info>\n");
            metaWriter.flush();
            metaWriter.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-14);
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            System.exit(-15);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-16);
        }

        System.exit(0);
    }
    
    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("xml_split1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("xml_split1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("xml_split1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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

class StructureStackElement
{
    public StructureStackElement(String element)
    {
        this.element = element;
    }
    
    public String GetElement()
    {
        return this.element;
    }
    
    protected String element;
}

class HierarchyDefinition
{
    public HierarchyDefinition(String element,
                               String attribute,
                               String value)
    {
        this.element = element;
        this.attribute = attribute;
        this.value = value;
    }                           
    
    public String GetElement()
    {
        return this.element;
    }
    
    public String GetAttribute()
    {
        return this.attribute;
    }
    
    public String GetValue()
    {
        return this.value;
    }

    protected String element;
    protected String attribute;
    protected String value;
}

class EntityResolverLocal implements XMLResolver
{
    public EntityResolverLocal(File configFile, File entitiesDirectory)
    {
        this.configFile = configFile;
        this.entitiesDirectory = entitiesDirectory;
        this.localEntities = new HashMap<String, File>();
        
        boolean success = true;

        if (success == true)
        {
            success = this.configFile.exists();
        }

        if (success == true)
        {
            success = this.configFile.isFile();
        }

        if (success == true)
        {
            success = this.configFile.canRead();
        }

        if (success == true)
        {
            success = this.entitiesDirectory.exists();
        }
        
        if (success == true)
        {
            success = this.entitiesDirectory.isDirectory();
        }
        
        if (success != true)
        {
            this.entitiesDirectory = null;
        }

        if (success == true)
        {
            Document document = null;
        
            try
            {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(this.configFile);
                document.getDocumentElement().normalize();
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


            NodeList entityNodeList = document.getElementsByTagName("entity");
            int entityNodeListCount = entityNodeList.getLength();

            for (int i = 0; i < entityNodeListCount; i++)
            {
                Node entityNode = entityNodeList.item(i);
                NodeList entityChildNodeList = entityNode.getChildNodes();
                int entityChildNodeListCount = entityChildNodeList.getLength();

                for (int j = 0; j < entityChildNodeListCount; j++)
                {
                    Node entityChildNode = entityChildNodeList.item(j);

                    if (entityChildNode.getNodeName().equalsIgnoreCase("resolve") == true)
                    {
                        Element element = (Element) entityChildNode;
                        String identifier = element.getAttribute("identifier");
                        String reference = element.getAttribute("reference");
                        File referencedFile = new File(reference);
                        
                        if (identifier.length() <= 0)
                        {
                            System.out.print("xml_split1: '" + this.configFile.getAbsolutePath() + "' contains a resolve entry with empty identifier.\n");
                            System.exit(-1);
                        }
                        
                        if (referencedFile.isAbsolute() != true)
                        {
                            String relativePath = this.entitiesDirectory.getAbsolutePath();
                            
                            if (relativePath.substring((relativePath.length() - File.separator.length()) - new String(".").length(), relativePath.length()).equalsIgnoreCase(File.separator + "."))
                            {
                                // Remove dot that references the local, current directory.
                                relativePath = relativePath.substring(0, relativePath.length() - new String(".").length());
                            }
                            
                            if (relativePath.substring(relativePath.length() - File.separator.length(), relativePath.length()).equalsIgnoreCase(File.separator) != true)
                            {
                                relativePath += File.separator;
                            }
                            
                            relativePath += reference;
                            referencedFile = new File(relativePath);
                        }
                        
                        if (referencedFile.exists() != true)
                        {
                            System.out.print("xml_split1: '" + referencedFile.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', doesn't exist.\n");
                            System.exit(-1);
                        }
                        
                        if (referencedFile.isFile() != true)
                        {
                            System.out.print("xml_split1: '" + referencedFile.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', isn't a file.\n");
                            System.exit(-1);
                        }
                        
                        if (referencedFile.canRead() != true)
                        {
                            System.out.print("xml_split1: '" + referencedFile.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', isn't readable.\n");
                            System.exit(-1);
                        }
                        
                        if (this.localEntities.containsKey(identifier) != true)
                        {
                            this.localEntities.put(identifier, referencedFile);
                        }
                        else
                        {
                            System.out.print("xml_split1: Identifier '" + identifier + "' configured twice in '" + this.configFile.getAbsolutePath() + "'.\n");
                            System.exit(-1);
                        }
                    }
                }
            }
        }
    }

    public Object resolveEntity(String publicID,
                                String systemID,
                                String baseURI,
                                String namespace)
    {
        if (this.entitiesDirectory == null)
        {
            System.out.print("xml_split1: Can't resolve entity, no local entities directory.\n");
            System.exit(-1);
        }
        
        if (this.configFile == null)
        {
            System.out.print("xml_split1: Can't resolve entity, no entities configured.\n");
            System.exit(-1);
        }
    
        File localEntity = null;
    
        if (localEntity == null)
        {
            if (this.localEntities.containsKey(publicID) == true)
            {
                localEntity = this.localEntities.get(publicID);
            }
        }
        
        if (localEntity == null)
        {
            if (this.localEntities.containsKey(systemID) == true)
            {
                localEntity = this.localEntities.get(systemID);
            }
        }
        
        if (localEntity == null)
        {
            System.out.print("xml_split1: Can't resolve entity with public ID '" + publicID + "', system ID '" + systemID + "', no local copy configured in '" + this.configFile.getAbsolutePath() + "'.\n");
            System.exit(-1);
        }
    
        if (localEntity.exists() != true)
        {
            System.out.print("xml_split1: '" + localEntity.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', doesn't exist.\n");
            System.exit(-1);
        }
        
        if (localEntity.isFile() != true)
        {
            System.out.print("xml_split1: '" + localEntity.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', isn't a file.\n");
            System.exit(-1);
        }
        
        if (localEntity.canRead() != true)
        {
            System.out.print("xml_split1: '" + localEntity.getAbsolutePath() + "', referenced in '" + this.configFile.getAbsolutePath() + "', isn't readable.\n");
            System.exit(-1);
        }
        
        FileInputStream fileInputStream = null;
        
        try
        {
            fileInputStream = new FileInputStream(localEntity);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        return fileInputStream;
    }

    protected File configFile;
    protected File entitiesDirectory;
    protected Map<String, File> localEntities;
}

