package cazzar.mods.permissions;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerJoinHandler implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        // TODO Auto-generated method stub
        PermissionsPlayer tmp;
        if (PermissionsPlayer.findPlayer(player.username) == null) {
            tmp = new PermissionsPlayer(player);
            tmp.addPermission("!/time");
            tmp.addPermission("!/help");
        }

    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
        // TODO Auto-generated method stub

    }

}
