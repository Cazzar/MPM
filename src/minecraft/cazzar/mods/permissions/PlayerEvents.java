package cazzar.mods.permissions;

import java.util.logging.Level;

import net.minecraft.command.CommandClearInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerEvents {

    Permissions perms = Permissions.instance;

    @ForgeSubscribe
    public void onCommand(CommandEvent evt) {
        perms.logger.log(Level.INFO, evt.sender.getCommandSenderName());
        if (evt.sender.getCommandSenderName().equalsIgnoreCase("Server")
                || evt.sender.getCommandSenderName().equalsIgnoreCase("Rcon")) {
            evt.setResult(Result.DEFAULT);
            return;
        }
        //should I make it check for vanilla commands?
        if (!Permissions.instance.getPermissionsForVanilla())
        	return;
        
        //A little hack to figure if it is part of Vanilla Minecraft.
        //If commands are overridden with the same name, this will error.
        //I may end up making a config set for all the command nodes
        String permissionNode;
        String cmdName = evt.command.getCommandName();
        
        if (cmdName.equals("clear")){
        	permissionNode = "vanilla." + cmdName;
        } 
        else if(cmdName.equals("debug")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("defaultgamemode")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("difficulty")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("enchant")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("gamemode")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("gamerule")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("give")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("help")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("kill")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("ban")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("banip")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("banlist")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("deop")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("me")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("kick")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("list")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("tell")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("op")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("pardon")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("pardonip")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("publish")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("save-all")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("save-off")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("save-on")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("say")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("stop")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("tp")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("whitelist")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("spawnpoint")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("seed")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("time")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("time")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("toggledownfall")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("weather")){
        	permissionNode = "vanilla." + cmdName;
        }
        else if(cmdName.equals("xp")){
        	permissionNode = "vanilla." + cmdName;
        }
        else{
        	return;
        }
        
        evt.setCanceled(true);
        
        PermissionsPlayer pl = PermissionsPlayer.getPlayers().get(
        		( (EntityPlayer) evt.sender ).username);
        
        if (!pl.has(permissionNode)){
        	pl.getPlayer().sendChatToPlayer(String.valueOf('\u00A7') + "4You cannot use this command!");
        	return;
        }
        
        evt.command.processCommand(evt.sender, evt.parameters);
    }
}
