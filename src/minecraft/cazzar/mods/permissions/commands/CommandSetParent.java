package cazzar.mods.permissions.commands;

import cazzar.mods.permissions.PermissionsGroup;
import cazzar.mods.permissions.PermissionsPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSetParent extends Command {

	public CommandSetParent() {
		super("setparentgroup");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 2) throw showUsage(sender);
		
		if (sender instanceof EntityPlayerMP){
			if (!PermissionsPlayer.getPlayers()
					.get(((EntityPlayerMP)sender).username)
					.has("permissions.group.setgroupparent")){
				
				sender.sendChatToPlayer(PERMISSION_DENIED);
				return;
			}
		}
		
		PermissionsGroup group = PermissionsGroup.getGroups().get(args[0]);
		PermissionsGroup parent = PermissionsGroup.getGroups().get(args[1]);
		
		if (group == null) throw new PlayerNotFoundException("Group ID is invalid");
		if (parent == null) throw new PlayerNotFoundException("Parent group ID is invalid");
		
		try {
			group.setParent(parent.getGroupId());
			sender.sendChatToPlayer("Successfully set the parent");
		} catch (Exception e) {
			sender.sendChatToPlayer("Error.");
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		// TODO Auto-generated method stub
		return "/" + name + "[Group] [new Parent]";
	}

}
