package cazzar.mods.permissions.commands;

import cazzar.mods.permissions.Permissions;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandReload extends Command {

	public CommandReload() {
		super("reloadperms");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayerMP){
			if (!PermissionsPlayer.getPlayers()
					.get(((EntityPlayerMP)sender).username)
					.has("permissions.reload")){
				
				sender.sendChatToPlayer(PERMISSION_DENIED);
				return;
			}
		}
		
		Permissions.instance.reload();
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + name + "";
	}

}
