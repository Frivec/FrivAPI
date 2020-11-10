package fr.frivec.spigot.title;

import static fr.frivec.spigot.packets.PacketUtils.sendPacket;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle.EnumTitleAction;

public class ActionBar {
	
	private final String text;
	private final int fadeIn,
				stay,
				fadeOut;
	
	public ActionBar(final String text, final int fadeIn, final int stay, final int fadeOut) {
		
		this.text = text;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
		
	}
	
	public void send(final Player player) {
		
		final PacketPlayOutTitle actionBar = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, ChatSerializer.a("{\"text\":\"" + this.text + "\"}"), this.fadeIn, this.stay, this.fadeOut);
		
		sendPacket(player, actionBar);
		
	}

}
