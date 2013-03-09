package cazzar.mods.permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.minecraft.command.ICommand;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.server.FMLServerHandler;

@Mod(modid = Permissions.ID, version = Permissions.VERSION, name = Permissions.ID)
public class Permissions {
	public static final String ID = "Permissions";
	public static final String VERSION = "0.1a";

	@Instance(ID)
	public static Permissions instance;
	public static Logger logger;
	public Configuration config;

	private static File configDirectory;

	ArrayList<PermissionsGroup> groups = new ArrayList<PermissionsGroup>();
	ArrayList<PermissionsPlayer> players = new ArrayList<PermissionsPlayer>();
	ArrayList<ICommand> supportedPermissionCommands = new ArrayList<ICommand>();

	@PreInit
	public void preInit(FMLPreInitializationEvent evt) {
		logger = Logger.getLogger(ID);
		logger.setParent(FMLLog.getLogger());
		
		configDirectory = new File(evt.getModConfigurationDirectory(), "MPM");
		config = new Configuration(new File(configDirectory, "permissions.conf"));
		config.load();
		config.save();

		MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		GameRegistry.registerPlayerTracker(new PlayerJoinHandler());
	}

	@Init
	public void init(FMLInitializationEvent evt) {
		
	}
	
	
}
