package fr.frivec.spigot.pluginmessages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import fr.frivec.spigot.listeners.PMListener;

public class PluginMessageManager implements PluginMessageListener {
	
	private Map<String, Set<PMListener>> listeners;
	
	public PluginMessageManager() {
		
		this.listeners = new HashMap<String, Set<PMListener>>();
		
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player sender, byte[] msg) {
		
		if(this.listeners.containsKey(channel)) {
			
			final Collection<PMListener> listeners = this.listeners.get(channel);
			
			for(PMListener listener : listeners)
				
				listener.onPluginMessageReceiving(sender, msg);
			
		}else
			
			return;
		
	}
	
	public Map<String, Set<PMListener>> getListeners() {
		return listeners;
	}

}
