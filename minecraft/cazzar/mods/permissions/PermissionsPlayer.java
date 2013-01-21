package cazzar.mods.permissions;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class PermissionsPlayer {

	EntityPlayer player;
	String username;
	PermissionsGroup primaryGroup;
	ArrayList<PermissionsGroup> secondaryGroups;
	ArrayList<String> permissionNodes;

	private Permissions perms = Permissions.instance;

	public PermissionsPlayer(EntityPlayer player) {
		this.player = player;
		this.username = getUsername();
		this.permissionNodes = new ArrayList<String>();
		this.secondaryGroups = new ArrayList<PermissionsGroup>();
		if (PermissionsGroup.getGroupByID(0) == null) {
			primaryGroup = new PermissionsGroup("Guest");
		}
		else { 
			primaryGroup = PermissionsGroup.getGroupByID(0);
		}
		perms.players.add(this);
	}

	public PermissionsPlayer() {
	}

	public String getUsername() {
		if (player != null)
			return player.username;
		return username;
	}

	public void setUsername(String newUsername) throws Exception {
		if (this.username == null)
			throw new Exception("Username not null");
		this.username = newUsername;
	}

	/*
	 * Can return null if the player is not found.
	 */
	public static PermissionsPlayer findPlayer(String name) {
		for (PermissionsPlayer player : Permissions.instance.players) {
			if (player.username.equalsIgnoreCase(name))
				return player;
		}

		return null;
	}

	public void setChatFormat(String data) {
		// TODO: Add chat formats
	}

	public void addPermission(String perm) {
		permissionNodes.add(perm);
	}

	public void addGroup(Integer gID) {
		if (this.primaryGroup == null) {
			this.primaryGroup = PermissionsGroup.getGroupByID(gID);
		} else {
			this.secondaryGroups.add(PermissionsGroup.getGroupByID(gID));
		}
	}

	public String getPermissionsAsDelimitedString(String delim) {
		StringBuffer buffer = new StringBuffer();
		int i = 1; // there is probably a better way to do this.
		for (String perm : permissionNodes) {
			buffer.append(perm);
			i++;
			if (permissionNodes.size() != i)
				buffer.append(delim);
		}

		return buffer.toString();
	}

	public String getGroupsAsDelimitedString(String delim) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(primaryGroup.ID);
		if (secondaryGroups.size() == 0)
			return buffer.toString();
		buffer.append(delim);
		int i = 1; // there is probably a better way to do this.
		for (PermissionsGroup perm : secondaryGroups) {
			buffer.append(perm.ID);
			i++;
			if (secondaryGroups.size() != i)
				buffer.append(delim);
		}

		return buffer.toString();
	}
	
	public boolean has(String permission) {
		if (permission == null)
			return true;
		else if (permission.equals(""))
			return true;
		if (permissionNodes.contains(permission))
			return true;
		else if (permissionNodes.contains("-" + permission))
			// make the ability to deny permissions
			return false;

		// TODO:Make it search down the herachy
		if (primaryGroup.has(permission))
			return true;
		//very hackish but it will do :)
		else if (primaryGroup.has("-" + permission))
			return false;
		for (PermissionsGroup i : secondaryGroups) {
			return i.has(permission);
		}
		return false;
	}
}
