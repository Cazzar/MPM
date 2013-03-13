package cazzar.mods.permissions.commands;

import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandAddGroup extends Command {
	
	public CommandAddGroup(){
		super("addgroup");
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayerMP){
			if (!PermissionsPlayer.getPlayers()
					.get(((EntityPlayerMP)sender).username)
					.has("permissions.addgroup"))
				sender.sendChatToPlayer(PERMISSION_DENIED);
		}

		if (args.length != 2) throw showUsage(sender);
		
		PermissionsGroup gr = new PermissionsGroup(args[0], args[1]);
		try {
			gr.addGroup();
			sender.sendChatToPlayer("Adding group was sucessful!");
		} catch (Exception e) {
			sender.sendChatToPlayer("Adding group failed, probably because the group already exists with that ID");
		}
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + name + " [new group ID] [group name]" ;
	}

}
