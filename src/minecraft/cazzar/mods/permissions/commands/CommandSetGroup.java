package cazzar.mods.permissions.commands;

import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSetGroup extends Command {

	public CommandSetGroup() {
		super("setgroup");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 2) throw showUsage(sender);
		
		if (sender instanceof EntityPlayerMP){
			if (!PermissionsPlayer.getPlayers()
					.get(((EntityPlayerMP)sender).username)
					.has("permissions.player.setgroup")){
				
				sender.sendChatToPlayer(PERMISSION_DENIED);
				return;
			}
		}
		
		PermissionsPlayer pl = PermissionsPlayer.getPlayers().get(args[0]);
		if (pl == null) {throw new PlayerNotFoundException();}
		
		PermissionsGroup gr = PermissionsGroup.getGroups().get(args[1]);
		if (gr == null) throw new PlayerNotFoundException("Group ID is invalid");
		
		pl.setGroup(gr.getGroupId());
		sender.sendChatToPlayer(pl.getPlayerId() + " is now in the group: " 
							+ gr.getGroupName() + "(" + gr.getGroupId() + ")");
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + name + "[playername] [groupId]";
	}
}
