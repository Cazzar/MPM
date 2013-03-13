package cazzar.mods.permissions.commands;

import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSetPrefix extends Command {

	public CommandSetPrefix() {
		super("prefix");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 2) throw showUsage(sender);
		
		if (args[0].equalsIgnoreCase("player")){
			if (sender instanceof EntityPlayerMP){
				if (!PermissionsPlayer.getPlayers()
						.get(((EntityPlayerMP)sender).username)
						.has("permissions.prefix.player") || 
					!PermissionsPlayer.getPlayers()
						.get(((EntityPlayerMP)sender).username)
						.has("permissions.prefix.all")){
					
					sender.sendChatToPlayer(PERMISSION_DENIED);
					return;
				}
			}
			
			PermissionsPlayer pl = PermissionsPlayer.getPlayers().get(args[1]);
			if (pl == null) throw new PlayerNotFoundException();
			
			pl.setPrefix(args[1]);
			sender.sendChatToPlayer(pl.getPlayerId() + "'s prefix was set to: " + args[1].replace('&', '\u00A7'));
		}
		else if (args[0].equalsIgnoreCase("group")){
			if (sender instanceof EntityPlayerMP){
				if (!PermissionsPlayer.getPlayers()
						.get(((EntityPlayerMP)sender).username)
						.has("permissions.prefix.group") || 
					!PermissionsPlayer.getPlayers()
						.get(((EntityPlayerMP)sender).username)
						.has("permissions.prefix.all")){
					
					sender.sendChatToPlayer(PERMISSION_DENIED);
					return;
				}
			}
			
			PermissionsGroup pl = PermissionsGroup.getGroups().get(args[1]);
			if (pl == null) throw new PlayerNotFoundException();
			
			pl.setPrefix(args[1]);
			sender.sendChatToPlayer(pl.getGroupId() + "'s prefix was set to: " + args[1].replace('&', '\u00A7'));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + name + " player/group [newprefix]";
	}

}
