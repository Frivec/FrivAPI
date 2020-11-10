package fr.frivec.spigot.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractManager {

	protected JavaPlugin javaPlugin;
	
	public AbstractManager(final JavaPlugin javaPlugin) {
		
		this.javaPlugin = javaPlugin;
		
	}
	
	public abstract void onConnect(final Player player);
	
	public abstract void onDisconnect(final Player player);
	
	public abstract void onDisable();
	
}
