package fr.frivec.spigot.packets.listeners;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.Packet;

public interface PacketListener {
	
	public abstract void onPacketRead(final Player player, final Packet<?> packet) throws Exception;
	
	public abstract void onPacketWrite(final Player player, final Packet<?> packet) throws Exception;

}
