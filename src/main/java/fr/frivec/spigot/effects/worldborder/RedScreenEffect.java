package fr.frivec.spigot.effects.worldborder;

import static fr.frivec.spigot.packets.PacketUtils.getEntityPlayer;
import static fr.frivec.spigot.packets.PacketUtils.sendPacket;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R3.WorldBorder;

public class RedScreenEffect {
	
	public static void redScreenEffect(final Player player, final boolean enable) {
		
		final WorldBorder border = getEntityPlayer(player).world.getWorldBorder();
		
		border.setDamageAmount(0);
		border.setDamageBuffer(0);
		
		if(enable) {
			
			border.setSize(1);
			border.setCenter(player.getLocation().getX() + 10000, player.getLocation().getZ() + 10000);
			
		}else {
			
			border.setSize(30000000);
			border.setCenter(player.getLocation().getX(), player.getLocation().getZ());
			
		}
		
		sendPacket(player, new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
		
	}

}
