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

public class PermissionsParser {

	static final String GROUP = "group";
	static final String ID = "ID";
	static final String NAME = "name";
	static final String CHATFORMAT = "chatFormat";
	static final String PERMISSIONS = "permissions";
	static final String INHERITED = "inheritedGroups";

	static Permissions perms = Permissions.instance;

	public static boolean parsePermissions() {
		try{
			InputStream in = new FileInputStream(perms.modConfigDirectory
					+ "/permissions.xml");
		}catch(Exception e){
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
