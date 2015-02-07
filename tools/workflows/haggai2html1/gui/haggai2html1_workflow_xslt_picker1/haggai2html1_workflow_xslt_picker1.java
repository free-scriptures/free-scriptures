/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of haggai2html1 workflow.
 *
 * haggai2html1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * haggai2html1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with haggai2html1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/gui/haggai2html1_workflow_xslt_picker1/haggai2html1_workflow_xslt_picker1.java
 * @brief Select a Haggai XML to XHTML XSLT stylesheet.
 * @author Stephan Kreutzer
 * @since 2015-02-07
 */



import java.io.File;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;



public class haggai2html1_workflow_xslt_picker1
  extends JFrame
  implements ActionListener
{
    public static void main(String[] args)
    {
        System.out.print("haggai2html1_workflow_xslt_picker1 Copyright (C) 2015 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/free-scriptures/free-scriptures/\n" +
                         "and the project website http://www.free-scriptures.org.\n\n");


        if (args.length < 1)
        {
            System.out.print("Usage:\n" +
                             "\thaggai2html1_workflow_xslt_picker1 xslt-list-file\n\n");

            System.exit(1);
        }

        File configFile = new File(args[0]);

        if (configFile.exists() != true)
        {
            System.out.print("haggai2html1_workflow_xslt_picker1: Configuration file '" + configFile.getAbsolutePath() + "' doesn't exist.\n");
            System.exit(-1);
        }

        if (configFile.isFile() != true)
        {
            System.out.print("haggai2html1_workflow_xslt_picker1: Path '" + configFile.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (configFile.canRead() != true)
        {
            System.out.print("haggai2html1_workflow_xslt_picker1: Configuration file '" + configFile.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        haggai2html1_workflow_xslt_picker1 frame = new haggai2html1_workflow_xslt_picker1(configFile);
        frame.setLocation(100, 100);
        frame.pack();
        frame.setVisible(true);
    }

    public haggai2html1_workflow_xslt_picker1(File configFile)
    {
        super("Select Transformator");

        this.programPath = haggai2html1_workflow_xslt_picker1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        this.optionsList = new ArrayList<ChooseOption>();

        readConfigurationFile(configFile);


        JPanel panelMain = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
 
        panelMain.setLayout(gridbag);

        GridBagConstraints gridbagConstraints = new GridBagConstraints();

        gridbagConstraints.anchor = GridBagConstraints.NORTH;
        gridbagConstraints.weightx = 1.0;
        gridbagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbagConstraints.fill = GridBagConstraints.HORIZONTAL;
 
        int optionsListSize = this.optionsList.size();

        if (optionsListSize <= 0)
        {
            System.out.print("haggai2html1_workflow_xslt_picker1: No options configured in '" + configFile.getAbsolutePath() + "'.\n");
            System.exit(-1);
        }

        String[] optionStrings = new String[optionsListSize];
 
        for (int i = 0; i < optionsListSize; i++)
        {
            optionStrings[i] = this.optionsList.get(i).GetDisplayName();      
        }

        this.optionsComboBox = new JComboBox(optionStrings);
        this.optionsComboBox.setSelectedIndex(0);
        this.optionsComboBox.addActionListener(this);
 
        panelMain.add(this.optionsComboBox, gridbagConstraints);
        panelMain.setBorder(BorderFactory.createEtchedBorder()); 

        getContentPane().add(panelMain, BorderLayout.NORTH); 


        JPanel panelDescription = new JPanel();
        gridbag = new GridBagLayout();
 
        panelDescription.setLayout(gridbag);
  
        gridbagConstraints = new GridBagConstraints();

        gridbagConstraints.anchor = GridBagConstraints.NORTH;
        gridbagConstraints.weightx = 1.0;
        gridbagConstraints.weighty = 1.0;
        gridbagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbagConstraints.gridheight = GridBagConstraints.REMAINDER;
        gridbagConstraints.fill = GridBagConstraints.BOTH;
         
        this.textAreaDescription = new JTextArea();
        this.textAreaDescription.setEditable(false);
        this.textAreaDescription.setLineWrap(true);
        this.textAreaDescription.setWrapStyleWord(true);
        this.textAreaDescription.setText(this.optionsList.get(0).GetDescription());

        panelDescription.setPreferredSize(new Dimension(200, 150));

        panelDescription.add(this.textAreaDescription, gridbagConstraints);

        JScrollPane scrollPane = new JScrollPane(this.textAreaDescription);

        panelDescription.add(scrollPane, gridbagConstraints);
        
        panelDescription.setBorder(BorderFactory.createEtchedBorder()); 
        getContentPane().add(panelDescription, BorderLayout.CENTER);


        JPanel panelButtons = new JPanel();
        
        JButton buttonAbout = new JButton("About");
        buttonAbout.addActionListener(this);
        panelButtons.add(buttonAbout);
        
        JButton buttonAbort = new JButton("Abort");
        buttonAbort.addActionListener(this);
        panelButtons.add(buttonAbort);
        
        JButton buttonSelect = new JButton("Select");
        buttonSelect.addActionListener(this);
        panelButtons.add(buttonSelect);
        
        panelButtons.setBorder(BorderFactory.createEtchedBorder()); 
        getContentPane().add(panelButtons, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event)
            {
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() instanceof JComboBox)
        {
            JComboBox comboBox = (JComboBox)event.getSource();
            String selection = (String)comboBox.getSelectedItem();
            
            int optionsListSize = this.optionsList.size();
            
            for (int i = 0; i < optionsListSize; i++)
            {
                if (this.optionsList.get(i).GetDisplayName().equals(selection) == true)
                {
                    this.textAreaDescription.setText(this.optionsList.get(i).GetDescription());
                    return;
                }
            }
            
            this.textAreaDescription.setText("?");
            
            return;
        }
    
        String clickedButton = event.getActionCommand();
        
        if (clickedButton.equalsIgnoreCase("Abort") == true)
        {
            System.exit(2);
        }
        else if (clickedButton.equalsIgnoreCase("About") == true)
        {
            AboutDialog aboutDialog = new AboutDialog(this);
            aboutDialog.setVisible(true);
        }
        else if (clickedButton.equalsIgnoreCase("Select") == true)
        {
            String selection = (String)this.optionsComboBox.getSelectedItem();

            int optionsListSize = this.optionsList.size();
            
            for (int i = 0; i < optionsListSize; i++)
            {
                if (this.optionsList.get(i).GetDisplayName().equals(selection) == true)
                {
                    System.out.println("haggai2html1_workflow_xslt_picker1: '" + this.optionsList.get(i).GetXSLTFile().getAbsolutePath() + "' selected.");
                    System.exit(0);
                }
            }
            
            System.out.println("haggai2html1_workflow_xslt_picker1: Can't find corresponding option entry for display name '" + selection + "'.");
        }
        else
        {
        
        }
    }

    protected int readConfigurationFile(File configFile)
    {
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(configFile);
            document.getDocumentElement().normalize();

            {
                NodeList optionsNodeList = document.getElementsByTagName("option");
                int optionsNodeListLength = optionsNodeList.getLength();

                if (optionsNodeListLength > 0)
                {
                    for (int i = 0; i < optionsNodeListLength; i++)
                    {
                        Node optionNode = optionsNodeList.item(i);

                        NamedNodeMap optionNodeAttributes = optionNode.getAttributes();
                        
                        if (optionNodeAttributes == null)
                        {
                            System.out.print("haggai2html1_workflow_xslt_picker1: Option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "' has no attributes.\n");
                            System.exit(-1);
                        }

                        Node pathAttribute = optionNodeAttributes.getNamedItem("path");
                        
                        if (pathAttribute == null)
                        {
                            System.out.print("haggai2html1_workflow_xslt_picker1: Option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "' is missing the 'path' attribute.\n");
                            System.exit(-1);
                        }

                        File xsltFile = new File(pathAttribute.getTextContent());
                        
                        if (xsltFile.isAbsolute() != true)
                        {
                            xsltFile = new File(configFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getTextContent());
                        }
                           
                        if (xsltFile.exists() != true)
                        {
                            System.out.print("haggai2html1_workflow_xslt_picker1: XSLT file '" + xsltFile.getAbsolutePath() + "', configured in option entry #" + (i + 1) + " of '" + configFile.getAbsolutePath() + "', doesn't exist.\n");
                            System.exit(-1);
                        }

                        if (xsltFile.isFile() != true)
                        {
                            System.out.print("haggai2html1_workflow_xslt_picker1: XSLT path '" + xsltFile.getAbsolutePath() + "', configured in option entry #" + (i + 1) + " of '" + configFile.getAbsolutePath() + "', isn't a file.\n");
                            System.exit(-1);
                        }

                        if (xsltFile.canRead() != true)
                        {
                            System.out.print("haggai2html1_workflow_xslt_picker1: XSLT file '" + xsltFile.getAbsolutePath() + "', configured in option entry #" + (i + 1) + " of '" + configFile.getAbsolutePath() + "', isn't readable.\n");
                            System.exit(-1);
                        }

                        String displayName = null;

                        {
                            NodeList displayNameNodeList = ((Element)optionNode).getElementsByTagName("display-name");
                            int displayNameNodeListLength = displayNameNodeList.getLength();
                            
                            if (displayNameNodeListLength == 1)
                            {
                                displayName = displayNameNodeList.item(0).getTextContent();
                            }
                            else if (displayNameNodeListLength > 1)
                            {
                                System.out.println("haggai2html1_workflow_xslt_picker1: More than one 'display-name' specified at option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "'.");
                                System.exit(-1);
                            }
                            else
                            {
                                System.out.println("haggai2html1_workflow_xslt_picker1: 'display-name' of option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "' is missing.");
                                System.exit(-1);
                            }
                        }
                        
                        {
                            int optionsListSize = this.optionsList.size();
                        
                            for (int j = 0; j < optionsListSize; j++)
                            {
                                if (this.optionsList.get(j).GetDisplayName().equals(displayName) == true)
                                {
                                    System.out.println("haggai2html1_workflow_xslt_picker1: 'display-name' '" + displayName + "' of option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "' was already specified by a previous option entry.");
                                    System.exit(-1);
                                }
                            }
                        }
                        
                        String description = null;

                        {
                            NodeList descriptionNodeList = ((Element)optionNode).getElementsByTagName("description");
                            int descriptionNodeListLength = descriptionNodeList.getLength();
                            
                            if (descriptionNodeListLength == 1)
                            {
                                description = descriptionNodeList.item(0).getTextContent();
                            }
                            else if (descriptionNodeListLength > 1)
                            {
                                System.out.println("haggai2html1_workflow_xslt_picker1: More than one 'description' specified at option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "'.");
                                System.exit(-1);
                            }
                            else
                            {
                                System.out.println("haggai2html1_workflow_xslt_picker1: 'description' of option entry #" + (i + 1) + " in '" + configFile.getAbsolutePath() + "' is missing.");
                                System.exit(-1);
                            }
                        }

                        this.optionsList.add(new ChooseOption(xsltFile, displayName, description));
                    }
                }
                else
                {
                    System.out.print("haggai2html1_workflow_xslt_picker1: No options configured in '" + configFile.getAbsolutePath() + "'.\n");
                    System.exit(-1);
                }
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

        if (optionsList.size() <= 0)
        {
            System.out.println("haggai2html1_workflow_xslt_picker1: No options configured in '" + configFile.getAbsolutePath() + "'.");
            System.exit(-1);
        }

        return 0;
    }
    
    
    protected ArrayList<ChooseOption> optionsList;

    private JComboBox optionsComboBox;
    private JTextArea textAreaDescription;
    
    protected String programPath;
}

class ChooseOption
{
    public ChooseOption(File xsltFile, String displayName, String description)
    {
        this.xsltFile = xsltFile;
        this.displayName = displayName;
        this.description = description;
    }

    public File GetXSLTFile()
    {
        return this.xsltFile;
    }

    public String GetDisplayName()
    {
        return this.displayName;
    }
    
    public String GetDescription()
    {
        return this.description;
    }

    protected File xsltFile;
    protected String displayName;
    protected String description;
}

class AboutDialog extends JDialog
{
    public AboutDialog(JFrame parent)
    {
        generateGUI(parent);
    }

    public final void generateGUI(JFrame parent)
    {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        ImageIcon icon = new ImageIcon("free_scriptures_logo.png");
        JLabel label = new JLabel(icon);
        label.setAlignmentX(0.5f);
        add(label);

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel notice = new JLabel("<html><head><title>About haggai2html1_workflow_xslt_picker1</title></head><body>" +
                                   "haggai2html1_workflow_xslt_picker1 Copyright (C) 2015 Stephan Kreutzer<br/><br/>" +
                                   "This program comes with ABSOLUTELY NO WARRANTY.<br/>" +
                                   "This is free software, and you are welcome to redistribute it<br/>" +
                                   "under certain conditions. See the GNU Affero General Public<br/>" +
                                   "License 3 or any later version for details. Also, see the source code<br/>" +
                                   "repository https://github.com/free-scriptures/free-scriptures/<br/>" +
                                   "or the project website http://www.free-scriptures.org.<br/>" +
                                   "</body></html>");
                                 
        notice.setFont(notice.getFont().deriveFont(notice.getFont().getStyle() & ~Font.BOLD));
        notice.setAlignmentX(0.5f);
        add(notice);

        add(Box.createRigidArea(new Dimension(0, 10)));

        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        closeButton.setAlignmentX(0.5f);
        add(closeButton);

        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("About");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setSize(520, 415);
    }
}

