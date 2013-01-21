package cazzar.mods.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Permissions.ID, version = Permissions.VERSION, name = Permissions.ID)
public class Permissions {
	public static final String ID = "Permissions";
	public static final String VERSION = "0.1a";

	@Instance(ID)
	public static Permissions instance;
	public static Logger logger;
	public Configuration config;

	public static File modConfigDirectory;

	ArrayList<PermissionsGroup> groups = new ArrayList<PermissionsGroup>();
	ArrayList<PermissionsPlayer> players = new ArrayList<PermissionsPlayer>();
	ArrayList<ICommand> supportedPermissionCommands = new ArrayList<ICommand>();
	Integer defaultGroupID = 0;

	@PreInit
	public void preInit(FMLPreInitializationEvent evt) {
		logger = Logger.getLogger(ID);
		logger.setParent(FMLLog.getLogger());

		File f = new File(evt.getModConfigurationDirectory().toString()
				+ "/permissions.conf");

		modConfigDirectory = evt.getModConfigurationDirectory();
		instance.config = new Configuration(f);
		instance.config.load();
		defaultGroupID = config.get("main", "defaultGroup", 0).getInt();
		config.save();

		MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		GameRegistry.registerPlayerTracker(new PlayerJoinHandler());
	}

	@Init
	public void init(FMLInitializationEvent evt) {
		if (!ConfigPermissions.parseGroups())
			ConfigPermissions.loadDefaultGroups();
		else if (groups.size() == 0) {
			ConfigPermissions.loadDefaultGroups();
		}
		PlayerPermissions.parsePlayers();
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent evt) {
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {

	}

	public ArrayList<PermissionsGroup> getGroups() {
		return instance.groups;
	}
	public void addSupporedCommand(ICommand cmd){
		if (!supportedPermissionCommands.contains(cmd)) {
			supportedPermissionCommands.add(cmd);
		}
	}
}
