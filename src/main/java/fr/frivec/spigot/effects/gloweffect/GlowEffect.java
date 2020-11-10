package fr.frivec.spigot.effects.gloweffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Player;

import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.core.utils.tests.PingTest;
import fr.frivec.spigot.API;
import fr.frivec.spigot.packets.NMSTeam;
import fr.frivec.spigot.packets.PacketUtils;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcher.Item;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumTeamPush;

public class GlowEffect {
	
	private Map<EffectColor, NMSTeam> teams;
	private Map<Player, GlowListener> listeners;
	public static Map<UUID, PingTest> pingTests = new HashMap<UUID, PingTest>();
	
	public GlowEffect() {
		
		this.teams = new HashMap<>();
		this.listeners = new HashMap<Player, GlowListener>();
		
		for(EffectColor color : EffectColor.values()) {
			
			final NMSTeam newTeam = new NMSTeam(color.getCode(), color, "", "", EnumNameTagVisibility.ALWAYS, EnumTeamPush.NEVER);
			
			this.teams.put(color, newTeam);
			
		}
		
		API.log(LogLevel.INFO, "Teams for glowing effect created. Total created: " + this.teams.size());
		
	}
	
	public void setGlowColor(final Player player, final EffectColor color, final boolean active) {
		
    	/*
    	 * 
    	 * Team Packet
    	 * 
    	 */
		
		final NMSTeam team = this.teams.get(color);	
			
		/**
		 * 
		 * Glow Effect
		 * 
		 */
		
		if(active) {
			
			if(!team.getPlayers().contains(player))
			
				for(Player players : Bukkit.getOnlinePlayers())
					
					team.createPacket(player, false, false, true, false, players);
		
		}else {
			
			if(team.getPlayers().contains(player))
			
	            for(Player players : Bukkit.getOnlinePlayers())
		            
	            	team.createPacket(player, false, false, false, true, players);
			
		}
            	
		/*	
		 * 
		 * GlowListener
		 * 
		 */
		
		GlowListener listener;
		
		if(active) {
			
			listener = new GlowListener(player);
			
			this.getListeners().put(player, listener);
			//Register the packet listener
			API.getInstance().getPacketsManager().registerPlayer(player, listener);
		
		}else {
			
			listener = this.listeners.get(player);
			
			final Set<UUID> uuids = new HashSet<UUID>();
			
			for(Player players : listener.getViewers())
				
				uuids.add(players.getUniqueId());
			
			removeViewers(player, uuids);
			listener.getViewers().clear();
			API.getInstance().getPacketsManager().removePlayer(player);
			this.listeners.remove(player);
			
		}
		
	}
	
	public void addViewerByNames(final Player player, final Collection<String> viewers) {
		
		for(String viewer : viewers)
		
			addViewer(player, Bukkit.getPlayer(viewer));
		
	}
	
	public void addViewer(final Player player, final Collection<Player> viewers) {
		
		for(Player viewer : viewers)
		
			addViewer(player, viewer);
		
	}
	
	public void addViewer(final Player player, final Player viewer) {
		
		if(viewer == null || player == null || this.listeners.get(player) == null)
			
			return;
		
		if(this.listeners.get(player).getViewers().contains(viewer))
			
			return;
		
		this.listeners.get(player).getViewers().add(viewer);
		
		PacketUtils.sendPacket(viewer, createPacket(player, true));
		
	}
	
	public void removeViewer(final Player player, final Player viewer) {
		
		if(!this.listeners.get(player).getViewers().contains(viewer))
			
			return;
		
		this.listeners.get(player).getViewers().remove(viewer);
		
		PacketUtils.sendPacket(viewer, createPacket(player, false));
			
	}
	
	public void removeViewers(final Player player, final Collection<UUID> viewers) {
		
		for(UUID uuids : viewers) {
			
			final Player viewer = Bukkit.getPlayer(uuids);
		
			if(!this.listeners.get(player).getViewers().contains(viewer))
				
				return;
			
			this.listeners.get(player).getViewers().remove(viewer);
			
			PacketUtils.sendPacket(viewer, createPacket(player, false));
		
		}
		
	}
	
	public void removeViewersByName(final Player player, final Collection<String> viewers) {
		
		for(String uuids : viewers) {
			
			final Player viewer = Bukkit.getPlayer(uuids);
		
			if(!this.listeners.get(player).getViewers().contains(viewer))
				
				return;
			
			this.listeners.get(player).getViewers().remove(viewer);
			
			PacketUtils.sendPacket(viewer, createPacket(player, false));
		
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private PacketPlayOutEntityMetadata createPacket(final Player player, final boolean enable) {
		
		final EntityPlayer entityPlayer = PacketUtils.getEntityPlayer(player);
		
		final DataWatcher toCloneDataWatcher = entityPlayer.getDataWatcher(),
				newDataWatcher = new DataWatcher(entityPlayer);
		
		try {

			// The map that stores the DataWatcherItems is private within the DataWatcher Object.
			// We need to use Reflection to access it from Apache Commons and change it.
			final Int2ObjectOpenHashMap<Item<?>> currentMap = (Int2ObjectOpenHashMap<Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "entries", true),
					newMap = new Int2ObjectOpenHashMap<DataWatcher.Item<?>>();

			// We need to clone the DataWatcher.Items because we don't want to point to those values anymore.
			for (Integer integer : currentMap.keySet())
				newMap.put(integer, currentMap.get(integer).d()); // Puts a copy of the DataWatcher.Item in newMap

			// Get the 0th index for the BitMask value. http://wiki.vg/Entities#Entity
			Item<Byte> item = (Item<Byte>) newMap.get(0);
			byte initialBitMask = (Byte) item.b(); // Gets the initial bitmask/byte value so we don't overwrite anything.
			byte bitMaskIndex = (byte) 6; // The index as specified in wiki.vg/Entities
			byte newByte = initialBitMask;
			
			if(enable)
			
				newByte = (byte) (initialBitMask | 1 << bitMaskIndex);
			
			else
				
	            item.a((byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.
				
		
			item.a(newByte);

			// Set the newDataWatcher's (unlinked) map data
			
			FieldUtils.writeDeclaredField(newDataWatcher, "entries", newMap, true);
		
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(player.getEntityId(), newDataWatcher, true);
		
		return metadataPacket;
		
	}
	
	public Map<Player, GlowListener> getListeners() {
		return listeners;
	}
	
	public Map<EffectColor, NMSTeam> getTeams() {
		return teams;
	}
	
	public NMSTeam getTeam(final Player player) {
		
		NMSTeam team = null;
		
		/*
		 * 
		 * Team
		 * 
		 */
		
		for(final NMSTeam teams : this.teams.values()) {
			
			if(teams.getPlayers().contains(player)) {
				
				team = teams;
				break;
				
			}
			
		}
		
		return team;
		
	}
	
	public enum EffectColor {
		
		BLACK("0", EnumChatFormat.BLACK),
		DARK_BLUE("1", EnumChatFormat.DARK_BLUE),
		DARK_GREEN("2", EnumChatFormat.DARK_GREEN),
		DARK_CYAN("3", EnumChatFormat.DARK_AQUA),
		DARK_RED("4", EnumChatFormat.DARK_RED),
		DARK_PURPLE("5", EnumChatFormat.DARK_PURPLE),
		GOLD("6", EnumChatFormat.GOLD),
		LIGHT_GRAY("7", EnumChatFormat.GRAY),
		DARK_GRAY("8", EnumChatFormat.DARK_GRAY),
		INDIGUO("9", EnumChatFormat.LIGHT_PURPLE),
		LIME_GREEN("a", EnumChatFormat.GREEN),
		CYAN("b", EnumChatFormat.BLUE),
		RED("c", EnumChatFormat.RED),
		PINK("d", EnumChatFormat.LIGHT_PURPLE),
		YELLOW("e", EnumChatFormat.YELLOW),
		WHITE("f", EnumChatFormat.WHITE),
		RESET("r", EnumChatFormat.RESET),
		BOLD("l", EnumChatFormat.BOLD),
		STRIKE("m", EnumChatFormat.STRIKETHROUGH),
		UNDERLINE("n", EnumChatFormat.UNDERLINE),
		ITALIC("o", EnumChatFormat.ITALIC);
		
		private String code;
		private EnumChatFormat chatFormat;
		
		private EffectColor(final String code, final EnumChatFormat chatFormat) {
			
			this.code = code;
			this.chatFormat = chatFormat;
			
		}
		
		public EnumChatFormat getChatFormat() {
			return chatFormat;
		}
		
		public String getCode() {
			return code;
		}
		
	}

}
