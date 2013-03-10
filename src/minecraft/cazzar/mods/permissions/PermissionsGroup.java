package cazzar.mods.permissions;

import java.util.ArrayList;

public class PermissionsGroup {
	private String groupId;
	private String groupName;
	private String prefix;
	private boolean hasPrefix;
	private String parent;
	private boolean hasParent;
	private ArrayList<String> permissions, revokedPermissions;
	
	public PermissionsGroup(String groupId, String groupName){
		this.groupId = groupId;
		this.groupName = groupName;
		hasPrefix = false;
		permissions = new ArrayList<String>();
		revokedPermissions = new ArrayList<String>();
	}
	
	public String getGroupId(){
		return groupId;
	}
	
	public String getGroupName(){
		return groupName;
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
		return true; //just to fix the build issues for now.		
	}
	
	public boolean hasPermissionRevoked(String perm){
		return false; //just to fix the build issues for now.		
	}
}
