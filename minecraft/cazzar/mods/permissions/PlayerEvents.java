package cazzar.mods.permissions;

import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerEvents {

	Permissions perms = Permissions.instance;

	@ForgeSubscribe
	public void OnJoin(EntityJoinWorldEvent evt) {
	}

	@ForgeSubscribe
	public void onCommand(CommandEvent evt) {
		perms.logger.log(Level.INFO, evt.sender.getCommandSenderName());
		if (evt.sender.getCommandSenderName().equalsIgnoreCase("Server")
				|| evt.sender.getCommandSenderName().equalsIgnoreCase("Rcon")) {
			evt.setResult(Result.DEFAULT);
			return;
		}
		
		evt.setCanceled(true);
		
		PermissionsPlayer pl = PermissionsPlayer.findPlayer(evt.sender
				.getCommandSenderName());
		if (pl == null) {
			pl = new PermissionsPlayer((EntityPlayer) evt.sender);
		}
		String permissionNode = "!/" + evt.command.getCommandName();
		if (perms.supportedPermissionCommands.contains(evt.command)) {
			return; // will be handled with the command itself. does not have to
					// be handled with this plugin
		}
		
		//pl.addPermission(permissionNode);
		PlayerPermissions.saveGroups();
		if (pl.has(permissionNode)){
			evt.setResult(Result.ALLOW);
			evt.command.processCommand(evt.sender, evt.parameters);
		}
		else {
			evt.sender.sendChatToPlayer("You are not allowed to use " + evt.command.getCommandName());
			evt.sender.sendChatToPlayer(permissionNode);
		}

	}

	@ForgeSubscribe
	public void onChat(ServerChatEvent evt) {
		// evt.setCanceled(true);
	}
}
