package fr.frivec.bungeecord;

import fr.frivec.bungeecord.config.Config;
import fr.frivec.core.logger.Logger;
import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.core.storage.Credentials;
import fr.frivec.core.storage.database.DatabaseManager;
import fr.frivec.core.storage.redis.Redis;
import fr.frivec.core.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class API extends Plugin {
	
	private static API instance;
	
	private Configuration config;
	private Logger logger;
	
	private DatabaseManager databaseManager;
	private Redis redis;
	
	@Override
	public void onEnable() {
		
		instance = this;
		Utils.isBungee = true;
		
		this.config = new Config().getConfig();
		this.logger = new Logger(this);
		
		//MySQL and Redis
		if(getConfig().getBoolean("mySQL.enable")) {
			
			this.databaseManager = new DatabaseManager(new Credentials(this.getConfig().getString("mySQL.host"), this.getConfig().getString("mySQL.user"), this.getConfig().getString("mySQL.database"), this.getConfig().getString("mySQL.password"), this.getConfig().getInt("mySQL.port")));
			this.databaseManager.initAllDatabaseConnections();
			
			log(LogLevel.INFO, "Connected to the database");
			
		}
		
		if(this.getConfig().getBoolean("redis.enable")) {
			
			this.redis = new Redis(new Credentials(this.getConfig().getString("redis.host"), this.getConfig().getString("redis.clientName"), this.getConfig().getString("redis.password"), this.getConfig().getInt("redis.port")));
			log(LogLevel.INFO, "Connected to Redis");
			
		}
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		
		
		super.onDisable();
	}
	
	public static API getInstance() {
		return instance;
	}
	
	public Configuration getConfig() {
		return config;
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	public Redis getRedis() {
		return redis;
	}
	
	public static void log(final LogLevel level, final String message) {
		
		API.getInstance().logger.log(level, message);
		
	}

}
