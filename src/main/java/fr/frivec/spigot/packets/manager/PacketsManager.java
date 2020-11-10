package fr.frivec.spigot.packets.manager;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.frivec.spigot.packets.listeners.PacketAdapter;
import fr.frivec.spigot.packets.listeners.PacketListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

public class PacketsManager {
	
	private HashMap<Player, PacketAdapter> packetAdapters;
	
	public PacketsManager() {
		
		this.packetAdapters = new HashMap<>();
		
	}
	
	public void registerPlayer(final Player player, final PacketListener listener) {
		
		if(!this.packetAdapters.containsKey(player)) {
		
			final ChannelPipeline channelPipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
			final PacketAdapter adapter = new PacketAdapter(player);
			
			channelPipeline.addBefore("packet_handler", player.getName(), adapter);
			this.packetAdapters.put(player, adapter);
			
		}
		
		this.packetAdapters.get(player).getListeners().add(listener);
		
	}
	
	public void removePlayer(final Player player) {
		
        final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        
        channel.eventLoop().submit(() -> {
        	
            channel.pipeline().remove(player.getName());
            
            return null;
            
        });
        
        this.packetAdapters.get(player).getListeners().clear();
        this.packetAdapters.remove(player);
		
	}

}
