package cazzar.mods.permissions.addons.chat;

import cazzar.mods.permissions.Permissions;
import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;

public class ChatMessages {
	
	@ForgeSubscribe
	public void onChatMessage(ServerChatEvent evt){
		evt.setCanceled(true);
		
		String chatMessage = Permissions.instance.getChatFormat();
		PermissionsPlayer player = PermissionsPlayer.getPlayers().get(evt.username);
		PermissionsGroup playerGroup = PermissionsGroup.getGroups().get(player.getGroupId());
		
		chatMessage.replace("%p%", (player.hasPrefix()) ? player.getPrefix() : "")
				   .replace("%gp", (playerGroup.hasPrefix()) ? playerGroup.getPrefix() : "")
				   .replace("%n%", evt.username)
				   .replace('&', '\u00A7')//Replace & with section sign
				   .replace("%m%", evt.message);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatMessage);
	}
}
