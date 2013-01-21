package cazzar.mods.permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ConfigPermissions {

    static final String GROUP = "group";
    static final String ID = "ID";
    static final String NAME = "name";
    static final String CHATFORMAT = "chatFormat";
    static final String PERMISSIONS = "permissions";
    static final String INHERITED = "inheritedGroups";

    static Permissions perms = Permissions.instance;

    public static boolean parseGroups() {
        try {
            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(perms.modConfigDirectory
                    + "/permissions.xml");
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // Read the XML document
            PermissionsGroup group = null;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a item element we create a new item
                    if (startElement.getName().getLocalPart() == GROUP) {
                        group = new PermissionsGroup();
                        // attribute for the group ID
                        Iterator<Attribute> attributes = startElement
                                .getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(ID)) {
                                group.setID(Integer.getInteger(attribute
                                        .getValue()));
                            }
                            if (group.getID() == null) {
                                group.newID();
                            }
                        }
                    }
                }

                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(NAME)) {
                        event = eventReader.nextEvent();
                        group.setName(event.asCharacters().getData());
                        continue;
                    }
                }
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(CHATFORMAT)) {
                        event = eventReader.nextEvent();
                        String data = event.asCharacters().getData();
                        if (!data.trim().isEmpty())
                            group.setChatFormat(data);
                        continue;
                    }
                }
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PERMISSIONS)) {
                        event = eventReader.nextEvent();
                        String data = event.asCharacters().getData();
                        String[] perms = data.split(",");
                        for (String perm : perms)
                            if (!perm.trim().isEmpty()) {
                                Permissions.instance.logger.log(Level.INFO,
                                        perm + " " + perms.length);
                                group.addPermission(perm);
                            }
                        continue;
                    }
                }
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(INHERITED)) {
                        event = eventReader.nextEvent();
                        String data = event.asCharacters().getData();
                        String[] perms = data.split(",");
                        for (String perm : perms)
                            if (!perm.trim().isEmpty())
                                group.addInheritedGroup(Integer.parseInt(perm));
                        continue;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == (GROUP)) {
                        perms.groups.add(group);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // load default groups
            return false;
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveGroups() {
        try {
            File f = new File(perms.modConfigDirectory + "/permissions.xml");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

            XMLStreamWriter xmlStream = outputFactory
                    .createXMLStreamWriter(out);

            xmlStream.writeStartDocument();
            xmlStream.writeCharacters("\n");
            xmlStream.writeStartElement(PERMISSIONS);
            xmlStream.writeCharacters("\n\t");
            for (PermissionsGroup group : perms.groups) {
                xmlStream.writeStartElement(GROUP);
                xmlStream.writeAttribute(ID, String.valueOf(group.getID()));
                xmlStream.writeCharacters("\n\t\t");
                xmlStream.writeStartElement(NAME);
                xmlStream.writeCharacters(group.getName());
                xmlStream.writeEndElement();
                xmlStream.writeCharacters("\n\t\t");
                xmlStream.writeStartElement(CHATFORMAT);
                xmlStream.writeCharacters(group.getChatFormat());
                xmlStream.writeEndElement();
                xmlStream.writeCharacters("\n\t\t");
                xmlStream.writeStartElement(PERMISSIONS);
                xmlStream.writeCharacters(group
                        .getPermissionsAsDelimitedString(","));
                xmlStream.writeEndElement();
                xmlStream.writeCharacters("\n\t\t");
                xmlStream.writeStartElement(INHERITED);
                xmlStream.writeCharacters(group
                        .getInheritedGroupsAsDelimitedString(","));
                xmlStream.writeEndElement();
                xmlStream.writeCharacters("\n\t");
                xmlStream.writeEndElement();
            }
            xmlStream.writeCharacters("\n");
            xmlStream.writeEndElement();
            xmlStream.writeCharacters("\n");
            xmlStream.writeEndDocument();

            xmlStream.flush();
            xmlStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    public static boolean loadDefaultGroups() {
        return false;
    }
}
