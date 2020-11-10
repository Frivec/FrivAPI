package fr.frivec.spigot.listeners;

import org.bukkit.entity.Player;

public interface PMListener {

	public abstract void onPluginMessageReceiving(final Player sender, final byte[] message);
	
}
