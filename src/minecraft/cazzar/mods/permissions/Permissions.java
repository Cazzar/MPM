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

	private File configDirectory;
	private PermissionsParser permissionsParser;
	private String chatFormat;
	private String defaultGroupId;
	private Boolean permissionsForVanilla;
	private Boolean useChatHandler;

	@PreInit
	public void preInit(FMLPreInitializationEvent evt) {
		logger = Logger.getLogger(ID);
		logger.setParent(FMLLog.getLogger());
		
		configDirectory = new File(evt.getModConfigurationDirectory(), "MPM");
		config = new Configuration(new File(configDirectory, "config.conf"));
		config.load();
		
		chatFormat = config.get("general", "chatFormat", "%p% %gp% %n%:", "The format for the chat\n" +
														 "%p%: The player's prefix\n" +
														 "%gp%: The player's group prefix\n" +
														 "%n%: the player's name").value.trim();
		permissionsForVanilla = config.get("general", "permissionsForVanilla", 
				true, "Should we add permissions to the vanilla commands?\n" +
					  "If true: permissions of vanilla.command are added for each command\n" +
					  "If false: No permissions are added, OP needed for OP commands.").getBoolean(true);
		useChatHandler = config.get("addons", "enableChatHandler", true, "Enable the chat handler").getBoolean(true);
		
		config.save();
		
		permissionsParser = new PermissionsParser();
		permissionsParser.parseGroups();
		permissionsParser.saveGroups();
		

		if (useChatHandler) {
			MinecraftForge.EVENT_BUS.register(new cazzar.mods.permissions.addons.chat.ChatMessages());
		}
		
		MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		GameRegistry.registerPlayerTracker(new PlayerJoinHandler());
	}

	@Init
	public void init(FMLInitializationEvent evt) {
		
	}
	
	public static File getConfigDirectory(){
		return instance.configDirectory;
	}
	
	public static PermissionsParser getPermissionsParser(){
		return instance.permissionsParser;
	}
	
	public String getDefaultGroup(){
		return defaultGroupId;
	}
	
	public void setDefaultGroup(String defaultGroupId){
		this.defaultGroupId = defaultGroupId;
	}
	
	/**
	 * @return Whether or not to generate <i><b>all</b></i> vanilla permission nodes
	 */
	public Boolean getPermissionsForVanilla(){
		return permissionsForVanilla;
	}
	
	/**
	 * @return The current chat format</br>
	 * 
	 * <p><b>%p%:</b> The player's prefix</br>
	 * <b>%gp%:</b> The player's group prefix</br>
	 * <b>%n%:</b> the player's name</br>
	 */
	public String getChatFormat(){
		return chatFormat;
	}
	
	/**
	 * The format for the chat
	 * @param chatFormat The new Chat format</br>
	 * <b>%p%:</b> The player's prefix</br>
	 * <b>%gp%:</b> The player's group prefix</br>
	 * <b>%n%:</b> the player's name</br>
	 */
	public void setChatFormat(String chatFormat){
		this.chatFormat = chatFormat;
	}
}
