package cazzar.mods.permissions;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionsGroup {
	private String groupId;
	private String groupName;
	private String prefix;
	private boolean hasPrefix;
	private String parent;
	private boolean hasParent;
	private ArrayList<String> permissions, revokedPermissions;
	private static HashMap<String, PermissionsGroup> groups;
	
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
		if (prefix != null){
			hasPrefix = !prefix.isEmpty();
		}
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
		return revokedPermissions.contains(perm);		
	}
	
	public boolean hasPermissionRevoked(String perm){
		return revokedPermissions.contains(perm);		
	}
	
	public void addGroup(){
		if (groups == null){
			groups = new HashMap<String, PermissionsGroup>();
		}
		groups.put(groupId, this);
	}
	
	public void setParent(String groupId) throws Exception{
		if (!groups.containsKey(groupId)){
			throw new Exception("The group has to be added first");
		}
		this.parent = groupId;
		this.hasParent = true;
	}
	
	public String getParent(){
		return parent;
	}
	
	public Boolean hasParent(){
		return hasParent;
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
	
	public static HashMap<String, PermissionsGroup> getGroups(){
		if (groups == null) {
			groups = new HashMap<String, PermissionsGroup>();
		}
		return groups;
	}
	
	public static void setPermissionGroups(HashMap<String, PermissionsGroup> groups){
		PermissionsGroup.groups = groups;
	}
}
