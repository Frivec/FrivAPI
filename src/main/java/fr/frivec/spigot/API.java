package fr.frivec.spigot;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.core.json.GsonManager;
import fr.frivec.core.logger.Logger;
import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.core.storage.Credentials;
import fr.frivec.core.storage.database.DatabaseManager;
import fr.frivec.core.storage.redis.Redis;
import fr.frivec.spigot.commands.CommandManager;
import fr.frivec.spigot.commands.administrator.HologramCommand;
import fr.frivec.spigot.commands.administrator.RankCommand;
import fr.frivec.spigot.commands.dev.DevCommand;
import fr.frivec.spigot.effects.gloweffect.GlowEffect;
import fr.frivec.spigot.listeners.ListenerManager;
import fr.frivec.spigot.listeners.dev.DevListener;
import fr.frivec.spigot.listeners.player.PlayerConnection;
import fr.frivec.spigot.packets.manager.PacketsManager;
import fr.frivec.spigot.pluginmessages.BungeeCordPMListener;
import fr.frivec.spigot.pluginmessages.PluginMessageManager;

public class API extends JavaPlugin {
	
	/*
	 * 
	 * FrivAPI de Antoine Letessier est mis à disposition selon les termes de la licence Creative Commons Attribution - Pas d'Utilisation Commerciale - Pas de Modification 4.0 International.
	 * Fondé(e) sur une œuvre à https://gitlab.com/Frivec/frivapi.
	 * 
	 */
	
	private static API instance;
	
	private Logger logger;
	private ListenerManager listenerManager;
	private CommandManager commandManager;
	private PluginMessageManager pluginMessageManager;
	private PacketsManager packetsManager;
	private GsonManager gsonManager;
	
	private DatabaseManager databaseManager;
	private Redis redis;
	
	private GlowEffect glowEffect;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		//Config
		saveDefaultConfig();
		
		//Utils
		this.logger = new Logger(this);
		this.listenerManager = new ListenerManager(this);
		this.commandManager = new CommandManager();
		this.glowEffect = new GlowEffect();
		this.pluginMessageManager = new PluginMessageManager();
		this.packetsManager = new PacketsManager();
		this.gsonManager = new GsonManager();
		log(LogLevel.INFO, "Managers started");
		
		//MySQL
		if(this.getConfig().getBoolean("mySQL.enable")) {
			
			this.databaseManager = new DatabaseManager(new Credentials(this.getConfig().getString("mySQL.host"), this.getConfig().getString("mySQL.user"), this.getConfig().getString("mySQL.database"), this.getConfig().getString("mySQL.password"), this.getConfig().getInt("mySQL.port")));
			this.databaseManager.initAllDatabaseConnections();
			
			log(LogLevel.INFO, "Connected to the database");
			
		}
		
		//REDIS
		if(this.getConfig().getBoolean("redis.enable")) {
			
			this.redis = new Redis(new Credentials(this.getConfig().getString("redis.host"), this.getConfig().getString("redis.clientName"), this.getConfig().getString("redis.password"), this.getConfig().getInt("redis.port")));
			log(LogLevel.INFO, "Connected to Redis");
			
		}
		
		this.listenerManager.registerPluginMessageListener("BungeeCord", new BungeeCordPMListener());
		log(LogLevel.INFO, "Plugin Messages registered.");
			
		//Commands
		new DevCommand();
		new RankCommand();
		new HologramCommand();
		
		log(LogLevel.INFO, "Commands registered");
		
		this.getListenerManager().registerListener(new PlayerConnection());
		this.getListenerManager().registerListener(new DevListener());
		
		log(LogLevel.INFO, "Listeners registered");
		
		//Devs
		
		for(World world : Bukkit.getWorlds())
			
			log(LogLevel.INFO, "world " + world.getName() + " loaded");
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
		this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
		
		try {
		
			if(this.databaseManager != null && !this.databaseManager.getConnection().isClosed())
				
				this.databaseManager.closeAllDatabaseConnections();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(this.redis != null && !this.redis.getClient().isShutdown())
			
			this.redis.close();
		
		super.onDisable();
	}
	
	//Getters and setters
	
	/**
	 * Get the main instance of the API
	 * @return the instance
	 */
	public static API getInstance() {
		return instance;
	}
	
	/**
	 * Get the listener manager's instance.
	 * Can be used to register a new listener
	 * @return the listener manager's instance
	 */
	public ListenerManager getListenerManager() {
		return listenerManager;
	}
	
	/**
	 * Get the commands manager's instance
	 * Can be used to register a new command
	 * @return the instance of the command manager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	/**
	 * Get the redis instance
	 * @return redis instance
	 */
	public Redis getRedis() {
		return redis;
	}
	
	/**
	 * Get the database instance
	 * @return database instance
	 */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	/**
	 * Get the GlowEffect manager 
	 * @return glow effect manager instance
	 */
	public GlowEffect getGlowEffect() {
		return glowEffect;
	}
	
	/**
	 * Get the plugin messages manager
	 * @return PM manager instance
	 */
	public PluginMessageManager getPluginMessageManager() {
		return pluginMessageManager;
	}
	
	public PacketsManager getPacketsManager() {
		return packetsManager;
	}
	
	public GsonManager getGsonManager() {
		return gsonManager;
	}
	
	/**
	 * Send a log in the console of the server
	 * The color of the text will change according of the importance of the message
	 * @param level: The importance of the message.
	 * @param message; The message that will be send in the console.
	 */
	public static void log(final LogLevel level, final String message) {
		
		API.getInstance().logger.log(level, message);
		
	}
	
	/**
	 * Send a plugin message on the bungeecord instance of this server.
	 * To send a message to all the bungeecord, if there are more than 2 proxy, use Redis.
	 * @param channel: The channel you want to send in the message
	 * @param sender: The player that will be the sender. If null, the console will send the message
	 * @param message: The message in a table of byte. Use the ByteArrayDataOutPut class to create the message.
	 */
	public static void sendPluginMessage(final String channel, final Player sender, final byte[] message) {
		
		if(sender != null && sender.isOnline())
			
			sender.sendPluginMessage(API.getInstance(), channel, message);
		
		else
			
			API.getInstance().getServer().sendPluginMessage(API.getInstance(), channel, message);
		
	}

}
