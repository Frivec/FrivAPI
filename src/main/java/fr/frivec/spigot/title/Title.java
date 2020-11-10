package fr.frivec.spigot.title;

import static fr.frivec.spigot.packets.PacketUtils.sendPacket;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle.EnumTitleAction;

public class Title {
	
	private String title,
					subtitle;
	private int fadeIn,
				stay,
				fadeOut;
	
	public Title(final String title, final String subtitle, final int fadeIn, final int stay, final int fadeOut) {
		
		this.subtitle = subtitle;
		this.title = title;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		this.stay = stay;
		
	}
	
	public void send(final Player player) {
		
		if(this.title != null) {
		
			final PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"" + this.title + "\"}"), this.fadeIn, this.stay, this.fadeOut);
			
			sendPacket(player, title);
			
		}
		
		if(this.subtitle != null) {
			
			final PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"" + this.subtitle + "\"}"), this.fadeIn, this.stay, this.fadeOut);
			
			sendPacket(player, subtitle);
			
		}
		
	}

}
