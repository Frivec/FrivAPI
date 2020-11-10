package fr.frivec.spigot.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.frivec.spigot.API;
import fr.frivec.spigot.effects.gloweffect.GlowEffect;
import fr.frivec.spigot.packets.NMSTeam;

public class PlayerConnection implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(final PlayerJoinEvent event) {
		
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTask(API.getInstance(), ()->{
			
			for(NMSTeam teams : API.getInstance().getGlowEffect().getTeams().values())
				
				teams.createPacket(null, true, false, false, false, player);
			
		});
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(final PlayerQuitEvent event) {
		
		final Player player = event.getPlayer();
		final GlowEffect effect = API.getInstance().getGlowEffect();
		
		if(!effect.getListeners().containsKey(player))
			
			return;
		
		for(Player viewer : Bukkit.getOnlinePlayers())
			
			if(effect.getListeners().containsKey(player) && effect.getListeners().get(player).getViewers().contains(viewer))
				
				effect.removeViewer(player, viewer);
		
		effect.setGlowColor(player, effect.getTeam(player).getColor(), false);
			
		for(NMSTeam teams : API.getInstance().getGlowEffect().getTeams().values())
				
			teams.createPacket(player, false, true, false, false, player);
		
	}

}
