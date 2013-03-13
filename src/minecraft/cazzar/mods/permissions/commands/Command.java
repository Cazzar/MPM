package cazzar.mods.permissions.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public abstract class Command extends CommandBase {
	protected final String name;
	protected final String PERMISSION_DENIED = "\u00a7cYou do not have permission to use this command.";
	
	public Command(String name){
		this.name = name;
	}
	
	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return name;
	}

	public abstract String getCommandUsage(ICommandSender var1);

	WrongUsageException showUsage(ICommandSender var1) {
		return new WrongUsageException(getCommandUsage(var1));
	}

}
