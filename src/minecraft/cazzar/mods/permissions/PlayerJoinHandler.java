package cazzar.mods.permissions;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerJoinHandler implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
    	if (!PermissionsPlayer.getPlayers().containsKey(player.username)){
    		PermissionsPlayer pl = new PermissionsPlayer(player.username, player);
    		pl.setGroup(Permissions.instance.getDefaultGroup());
    		pl.addPlayer();
    	}
    	else {
    		PermissionsPlayer.getPlayers()
    			.get(player.username)
    			.setPlayer(player);
    	}
    	for (String s : PermissionsPlayer.getPlayers().keySet())
    		System.out.println(s);
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
    }
    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
    }
    @Override
    public void onPlayerRespawn(EntityPlayer player) {
    }
}
