package cazzar.mods.permissions;

import static argo.jdom.JsonNodeFactories.array;
import static argo.jdom.JsonNodeFactories.field;
import static argo.jdom.JsonNodeFactories.object;
import static argo.jdom.JsonNodeFactories.string;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;
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
			//JsonRootNode json = object(
			//		field("default", string("GuestGroup")),
			//		field("GuestGroup",
			//				object(field("name", string("Guest")),
			//						field("prefix", string("[Guest]")),
			//						field("permissions",
			//								array(string("mpm.world.placeblocks"), string("mpm.world.placeblocks2"))))));
			FileWriter fw = new FileWriter(new File(
					Permissions.getConfigDirectory(), "groups.json"));
			JSON_FORMATTER.format(makeGroupNodes(), fw);
			
			
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveDefaultGroups(){
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
	
	private JsonRootNode makeGroupNodes(){
		JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
		builder.withField("default", JsonNodeBuilders.aStringBuilder(
											Permissions.instance.getDefaultGroup()));
		
		for (PermissionsGroup gr : PermissionsGroup.getGroups().values()) {
			JsonArrayNodeBuilder permissions = JsonNodeBuilders.anArrayBuilder();
			for (String perm : gr.getPermissions()) {
				permissions.withElement(JsonNodeBuilders.aStringBuilder(perm));
			}
			
			JsonArrayNodeBuilder revokedPermissions = JsonNodeBuilders.anArrayBuilder();
			for (String perm : gr.getRevokedPermissions()) {
				//revokedPermissions.add(string(perm));
				revokedPermissions.withElement(JsonNodeBuilders.aStringBuilder(perm));
			}
			JsonObjectNodeBuilder grBuilder = JsonNodeBuilders.anObjectBuilder()
					.withField("name", JsonNodeBuilders.aStringBuilder(gr.getGroupName()))
					.withField("prefix", JsonNodeBuilders.aStringBuilder(gr.getPrefix()))
					.withField("permissions", permissions)
					.withField("revokedPermissions", revokedPermissions);
			builder.withField(gr.getGroupId(), grBuilder);
		}
		return builder.build();
	}

	public boolean parsePlayers() {
		try {
			FileReader fr = new FileReader(new File(
					Permissions.getConfigDirectory(), "players.json"));
			JsonRootNode json = JDOM_PARSER.parse(fr);
			HashMap<String, JsonNode> fields = new HashMap<String, JsonNode>();
			HashMap<String, PermissionsPlayer> players = new HashMap<String, PermissionsPlayer>();
			
			for (JsonNode n : json.getFields().keySet())
				if(json.isObjectNode(n.getText()))
					players.put(n.getText(), parsePlayer(n.getText(), json.getRootNode(n.getText())));
				else
					fields.put(n.getText(), n);
			
			System.out.println("Players: "+players.size()+" Fields: "+fields.size());
			//System.out.println("Default Group: " + Permissions.instance.getDefaultGroup());
			fr.close();
			
			PermissionsPlayer.setPermissionPlayers(players);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private PermissionsPlayer parsePlayer(String playerID, JsonRootNode jsonRootNode){
		PermissionsPlayer player = new PermissionsPlayer(playerID);
		if(jsonRootNode.isStringValue("prefix"))
			player.setPrefix(jsonRootNode.getStringValue("prefix"));
		if(jsonRootNode.isArrayNode("permissions"))
			for(JsonNode n : jsonRootNode.getArrayNode("permissions"))
				player.addPermission(n.getText());
		if(jsonRootNode.isArrayNode("revokedPermissions"))
			for(JsonNode n : jsonRootNode.getArrayNode("revokedPermissions"))
				player.addRevokedPermission(n.getText());
		if(jsonRootNode.isStringValue("groupId"))
			player.setGroup(jsonRootNode.getStringValue("groupId"));
		if(jsonRootNode.isStringValue("playerId"))
			player.setGroup(jsonRootNode.getStringValue("groupId"));
		return player;
	}
	
	private JsonRootNode makePlayerNodes(){
		JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
		//builder.withField("default", JsonNodeBuilders.aStringBuilder(
		//									Permissions.instance.getDefaultGroup()));
		
		for (PermissionsPlayer pl : PermissionsPlayer.getPlayers().values()) {
			JsonArrayNodeBuilder permissions = JsonNodeBuilders.anArrayBuilder();
			for (String perm : pl.getPermissions()) {
				permissions.withElement(JsonNodeBuilders.aStringBuilder(perm));
			}
			
			JsonArrayNodeBuilder revokedPermissions = JsonNodeBuilders.anArrayBuilder();
			for (String perm : pl.getRevokedPermissions()) {
				//revokedPermissions.add(string(perm));
				revokedPermissions.withElement(JsonNodeBuilders.aStringBuilder(perm));
			}
			JsonObjectNodeBuilder plBuilder = JsonNodeBuilders.anObjectBuilder()
					.withField("prefix", JsonNodeBuilders.aStringBuilder((pl.hasPrefix() ? pl.getPrefix() : "")))
					.withField("permissions", permissions)
					.withField("revokedPermissions", revokedPermissions);
			builder.withField(pl.getPlayerId(), plBuilder);
		}
		return builder.build();
	}
	public boolean savePlayers() {
		try {
			//JsonRootNode json = object(
			//		field("default", string("GuestGroup")),
			//		field("GuestGroup",
			//				object(field("name", string("Guest")),
			//						field("prefix", string("[Guest]")),
			//						field("permissions",
			//								array(string("mpm.world.placeblocks"), string("mpm.world.placeblocks2"))))));
			FileWriter fw = new FileWriter(new File(
					Permissions.getConfigDirectory(), "players.json"));
			JSON_FORMATTER.format(makePlayerNodes(), fw);
			
			
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
