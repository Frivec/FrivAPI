package fr.frivec.spigot.packets;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class PacketUtils {
	
	public static void sendPacket(final Player player, final Packet<?> packet) {
		
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		
	}
	
	public static void setField(Object edit, String fieldName, Object value) {
		
		try {
			Field field = edit.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(edit, value);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Object readField(final Object packet, final Class<?> clazz, final String fieldName) {
		
		try {
			
			final Field field = clazz.getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return field.get(packet);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static Object readField(final Object packet, final String fieldName) {
		
		try {
			
			final Field field = packet.getClass().getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return field.get(packet);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
			
		return null;
			
	}
	
	public static PlayerConnection getConnection(final Player player) {
		
		return ((CraftPlayer) player).getHandle().playerConnection;
		
	}
	
	public static EntityPlayer getEntityPlayer(final Player player) {
		
		return ((CraftPlayer) player).getHandle();
		
	}

}
