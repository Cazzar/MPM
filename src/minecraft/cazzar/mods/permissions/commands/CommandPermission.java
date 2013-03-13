package cazzar.mods.permissions.commands;

import java.util.Arrays;
import java.util.List;

import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandPermission extends Command {

	public CommandPermission() {
		super("permission");
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList("perm");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 4) throw showUsage(sender);
		if (args[0].equalsIgnoreCase("group")){
			PermissionsGroup gr = PermissionsGroup.getGroups().get(args[1]);
			if (gr == null) throw new PlayerNotFoundException("Group ID is invalid");
			
			if(args[2].equalsIgnoreCase("give")){
				if (sender instanceof EntityPlayerMP){
					if (!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.givepermission.group") || 
						!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.givepermission.all")){
						
						sender.sendChatToPlayer(PERMISSION_DENIED);
						return;
					}
				}
				
				if (gr.hasPermissionRevoked(args[3]))
					gr.removeRevokedPermissions(args[3]);
				gr.addPermission(args[3]);
				
				sender.sendChatToPlayer("Node " + args[3] + " is now allowed for " + gr.getGroupName() + "(" + gr.getGroupId() + ")");
			}
			else if(args[2].equalsIgnoreCase("take")){
				if (sender instanceof EntityPlayerMP){
					if (!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.revokepermission.group") || 
						!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.revokepermission.all")){
						
						sender.sendChatToPlayer(PERMISSION_DENIED);
						return;
					}
				}
				
				if (gr.hasPermission(args[3]))
					gr.removePermission(args[3]);
				gr.addRevokedPermission(args[3]);
				sender.sendChatToPlayer("Node " + args[3] + " is now revoked from " +  gr.getGroupName() + "(" + gr.getGroupId() + ")");
			}
			else 
				throw showUsage(sender);
		}
		if (args[0].equalsIgnoreCase("player")){
			PermissionsPlayer pl = PermissionsPlayer.getPlayers().get(args[1]);
			if (pl == null) throw new PlayerNotFoundException();
			
			if(args[2].equalsIgnoreCase("give")){
				if (sender instanceof EntityPlayerMP){
					if (!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.givepermission.player") || 
						!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.givepermission.all")){
						
						sender.sendChatToPlayer(PERMISSION_DENIED);
						return;
					}
				}
				
				if (pl.hasPermissionRevoked(args[3]))
					pl.removeRevokedPermissions(args[3]);
				pl.addPermission(args[3]);
				sender.sendChatToPlayer("Node " + args[3] + " is now allowed for " + pl.getPlayerId());
			}
			else if(args[2].equalsIgnoreCase("take")){
				if (sender instanceof EntityPlayerMP){
					if (!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.revokepermission.player") || 
						!PermissionsPlayer.getPlayers()
							.get(((EntityPlayerMP)sender).username)
							.has("permissions.revokepermission.all")){
						
						sender.sendChatToPlayer(PERMISSION_DENIED);
						return;
					}
				}
				
				if (pl.hasPermission(args[3]))
					pl.removePermission(args[3]);
				pl.addRevokedPermission(args[3]);
				sender.sendChatToPlayer("Node " + args[3] + " is now revoked from " + pl.getPlayerId());
			}
			else 
				throw showUsage(sender);
		}
		else 
			throw showUsage(sender);
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + name + " group/player [ID] give\take [node]";
	}

}
