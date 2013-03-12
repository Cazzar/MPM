package cazzar.mods.permissions;

import static argo.jdom.JsonNodeFactories.array;
import static argo.jdom.JsonNodeFactories.field;
import static argo.jdom.JsonNodeFactories.object;
import static argo.jdom.JsonNodeFactories.string;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.SajParser;

public class PermissionsParser {
	private static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	private static final JdomParser JDOM_PARSER = new JdomParser();
	private static final SajParser SAJ_PARSER = new SajParser();

	public boolean parseGroups() {
		try {
			FileReader fr = new FileReader(new File(
					Permissions.getConfigDirectory(), "groups.json"));
			JsonRootNode json = JDOM_PARSER.parse(fr);
			Permissions.instance.setDefaultGroup(json.getStringValue("default"));
			HashMap<String, JsonNode> fields = new HashMap<String, JsonNode>();
			HashMap<String, PermissionsGroup> groups = new HashMap<String, PermissionsGroup>();
			
			for (JsonNode n : json.getFields().keySet())
				if(json.isObjectNode(n.getText()))
					groups.put(n.getText(), parseGroup(n.getText(), json.getRootNode(n.getText())));
				else
					fields.put(n.getText(), n);
			
			System.out.println("Groups: "+groups.size()+" Fields: "+fields.size());
			System.out.println("Default Group: " + Permissions.instance.getDefaultGroup());
			fr.close();
			
			PermissionsGroup.setPermissionGroups(groups);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private PermissionsGroup parseGroup(String groupID, JsonRootNode jsonRootNode){
		PermissionsGroup group = new PermissionsGroup(groupID, jsonRootNode.getStringValue("name"));
		if(jsonRootNode.isStringValue("prefix"))
			group.setPrefix(jsonRootNode.getStringValue("prefix"));
		if(jsonRootNode.isArrayNode("permissions"))
			for(JsonNode n : jsonRootNode.getArrayNode("permissions"))
				group.addPermission(n.getText());
		if(jsonRootNode.isArrayNode("revokedPermissions"))
			for(JsonNode n : jsonRootNode.getArrayNode("revokedPermissions"))
				group.addRevokedPermission(n.getText());
		return group;
	}

	public boolean saveGroups() {
		try {
			JsonRootNode json = object(
					field("default", string("GuestGroup")),
					field("GuestGroup",
							object(field("name", string("Guest")),
									field("prefix", string("[Guest]")),
									field("permissions",
											array(string("mpm.world.placeblocks"), string("mpm.world.placeblocks2"))))));
			FileWriter fw = new FileWriter(new File(
					Permissions.getConfigDirectory(), "groups.json"));
			JSON_FORMATTER.format(json, fw);
			
			
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
