package fr.frivec.spigot.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.spigot.pluginmessages.PluginMessageManager;

public class ListenerManager {
	
	private JavaPlugin javaPlugin;
	private PluginMessageManager manager;
	
	/**
	 * Class that register all the listeners of the server.
	 * @param javaPlugin: the class extends JavaPlugin
	 */
	public ListenerManager(final JavaPlugin javaPlugin) {
		
		this.javaPlugin = javaPlugin;
		this.manager = new PluginMessageManager();
		
	}
	
	/**
	 * Register a listener on the api plugin. Use it to regroup all the listeners on one plugin.
	 * @param listener: Listener you want to register
	 */
	public void registerListener(final Listener listener) {
		
		this.javaPlugin.getServer().getPluginManager().registerEvents(listener, this.javaPlugin);
		
	}
	
	/**
	 * Register a plugin message listener. Use it to regroup all the plugin messages on one plugin.
	 * @param channel: The channel of the plugin messages
	 * @param listener: class implements PMListener: the listener you want to register.
	 */
	public void registerPluginMessageListener(final String channel, final PMListener listener) {
		
		Set<PMListener> listeners = new HashSet<PMListener>();
		
		if(this.manager.getListeners().get(channel) != null)
			
			listeners = this.manager.getListeners().get(channel);
			
		Bukkit.getMessenger().registerIncomingPluginChannel(this.javaPlugin, channel, this.manager);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this.javaPlugin, channel);
		
		listeners.add(listener);
		
		this.manager.getListeners().remove(channel);
		this.manager.getListeners().put(channel, listeners);
		
	}

}
