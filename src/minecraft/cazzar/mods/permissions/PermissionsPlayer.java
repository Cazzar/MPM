package cazzar.mods.permissions;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

public class PermissionsPlayer {

	private String playerId;
	private EntityPlayer player;
	private String prefix;
	private boolean hasPrefix, hasGroup;
	private String groupId;
	private ArrayList<String> permissions, revokedPermissions;
	private static HashMap<String, PermissionsPlayer> players;
	
	public PermissionsPlayer(String playerId, EntityPlayer player){
		this.playerId = playerId;
		this.player = player;
		hasPrefix = false;
		permissions = new ArrayList<String>();
		revokedPermissions = new ArrayList<String>();
	}
	
	public String getPlayerId(){
		return playerId;
	}
	
	public EntityPlayer getPlayer(){
		return player;
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
		hasPrefix = true;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public boolean hasPrefix(){
		return hasPrefix;
	}
	
	public void setGroup(String groupId){
		this.groupId = groupId;
		hasGroup = true;
	}
	
	public String getGroupId(){
		return groupId;
	}
	
	public boolean hasGroup(){
		return hasGroup;
	}
	
	public void setPermissions(ArrayList<String> permissions){
		this.permissions = permissions;
	}
	
	public ArrayList<String> getPermissions(){
		return permissions;
	}
	
	public void addPermission(String perm){
		permissions.add(perm);
	}
	
	public void removePermission(String perm){
		permissions.remove(perm);
	}
	
	public void setRevokedPermissions(ArrayList<String> revokedPermissions){
		this.revokedPermissions = revokedPermissions;
	}
	
	public ArrayList<String> getRevokedPermissions(){
		return revokedPermissions;
	}
	
	public void addRevokedPermission(String perm){
		revokedPermissions.add(perm);
	}
	
	public void removeRevokedPermissions(String perm){
		revokedPermissions.remove(perm);
	}
	
	public boolean hasPermission(String perm){
		return revokedPermissions.contains(perm);		
	}
	
	public boolean hasPermissionRevoked(String perm){
		return revokedPermissions.contains(perm);		
	}
	
	public void addPlayer(){
		if (players == null){
			players = new HashMap<String, PermissionsPlayer>();
		}
		players.put(playerId, this);
	}
	
	/**
	 *@param perm The permission node 
	 */
	public boolean has(String perm){
		if (hasPermission(perm)){
			return true;
		}
		else if (hasPermissionRevoked(perm)){
			return false;
		}
		
		String nextGroup = getGroupId();
		PermissionsGroup group = PermissionsGroup.getGroups().get(nextGroup);
		Boolean hasNextGroup = true;
		
		while (hasNextGroup) {
			
			if (group.hasPermission(perm)){
				return true;
			}
			else if (group.hasPermissionRevoked(perm)){
				return false;
			}
			
			if (!group.hasParent()){
				hasNextGroup = false;
				break;
			}
			
			nextGroup = group.getGroupId();
			group = PermissionsGroup.getGroups().get(nextGroup);
		}
		
		return false;
	}

	
	public static HashMap<String, PermissionsPlayer> getPlayers(){
		return players;
	}
}
