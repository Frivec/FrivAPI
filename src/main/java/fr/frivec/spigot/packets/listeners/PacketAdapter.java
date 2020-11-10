package fr.frivec.spigot.packets.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_16_R3.Packet;

public class PacketAdapter extends ChannelDuplexHandler {
	
	private String playerName;
	private UUID uuid;
	private Set<PacketListener> listeners;
	
	public PacketAdapter(final Player player) {
		
		this.playerName = player.getName();
		this.listeners = new HashSet<>();
		
		if(Bukkit.getServer().getOnlineMode())
		
			this.uuid = player.getUniqueId();
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext channel, Object packet) throws Exception {
		
		if(packet instanceof Packet<?>)
		
			for(PacketListener listeners : this.getListeners())
				
				listeners.onPacketRead(getPlayer(), (Packet<?>) packet);
		
		super.channelRead(channel, packet);
	}
	
	@Override
	public void write(ChannelHandlerContext channel, Object packet, ChannelPromise promise) throws Exception {
		
		if(packet instanceof Packet<?>)
		
			for(PacketListener listeners : this.getListeners())
				
				listeners.onPacketWrite(getPlayer(), (Packet<?>) packet);
		
		super.write(channel, packet, promise);
	}
	
	public Set<PacketListener> getListeners() {
		return listeners;
	}
	
	public Player getPlayer() {
		
		if(this.uuid != null)
			
			return Bukkit.getPlayer(this.uuid);
		
		return Bukkit.getPlayer(this.playerName);
		
	}
	
}
