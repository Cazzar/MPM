package cazzar.mods.permissions;

import java.util.ArrayList;

/**
 * @author Cayde The group system of the permissions
 */
public class PermissionsGroup {
    Integer ID;
    String name;
    String chatFormat;
    ArrayList<String> permissionNodes;
    ArrayList<Integer> inheritedPermissionGroups;
    // ArrayList<String> players;

    static Permissions perms = Permissions.instance;

    public PermissionsGroup(String Name) {
        int i = 0;
        for (PermissionsGroup group : perms.groups) {
            if (group.ID > i)
                // make it so it is always above the current found group ID
                i = group.ID + 1;
        }

        this.ID = i;
        this.name = Name;
        this.chatFormat = ""; // currently unused
        this.permissionNodes = new ArrayList<String>();
        this.inheritedPermissionGroups = new ArrayList<Integer>();
        // this.players = new ArrayList<String>();

        perms.groups.add(this);
    }

    public PermissionsGroup() {

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

        for (Integer i : inheritedPermissionGroups) {
            if (getGroupByID(i).getPermissionNodes().contains(permission))
                return true;
            if (getGroupByID(i).getPermissionNodes().contains("-" + permission))
                return false;
        }
        return false;
    }

    public static PermissionsGroup getGroupByID(Integer ID) {
        for (PermissionsGroup group : perms.groups) {
            if (group.ID == ID)
                return group;
        }

        return null;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public ArrayList<String> getPermissionNodes() {
        return permissionNodes;
    }

    public ArrayList<Integer> getInheritedGroups() {
        return inheritedPermissionGroups;
    }

    public void addInheritedGroup(Integer gID) {
        inheritedPermissionGroups.add(gID);
    }

    public void removeInheritedGroup(Integer gID) {
        inheritedPermissionGroups.remove(gID);
    }

    public void addPermission(String permission) {
        permissionNodes.add(permission);
    }

    public void removePermission(String permission) {
        permissionNodes.remove(permission);
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setChatFormat(String newFormat) {
        this.chatFormat = newFormat;
    }

    public void setID(Integer newID) throws Exception {
        if (this.ID == null)
            this.ID = newID;
        else
            throw new Exception("ID already assigned");
    }

    public void newID() throws Exception {
        if (this.ID != null)
            throw new Exception("ID already assigned");

        int i = 0;
        for (PermissionsGroup group : perms.groups) {
            if (group.ID > i)
                // make it so it is always above the current found group ID
                i = group.ID + 1;
        }
        this.ID = i;
    }

    public String getPermissionsAsDelimitedString(String delim) {
        StringBuffer buffer = new StringBuffer();
        int i = 1; // there is probably a better way to do this.
        for (String perm : getPermissionNodes()) {
            buffer.append(perm);
            i++;
            if (getPermissionNodes().size() != i)
                buffer.append(delim);
        }

        return buffer.toString();
    }

    public String getInheritedGroupsAsDelimitedString(String delim) {
        StringBuffer buffer = new StringBuffer();
        int i = 1; // there is probably a better way to do this.
        for (Integer perm : getInheritedGroups()) {
            buffer.append(perm);
            i++;
            if (getInheritedGroups().size() != i)
                buffer.append(delim);
        }

        return buffer.toString();
    }
}
