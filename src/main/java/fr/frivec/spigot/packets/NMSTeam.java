package fr.frivec.spigot.packets;

import static fr.frivec.spigot.packets.PacketUtils.sendPacket;
import static fr.frivec.spigot.packets.PacketUtils.setField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.Player;

import fr.frivec.spigot.effects.gloweffect.GlowEffect.EffectColor;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumTeamPush;

public class NMSTeam {
	
	private String name;
	private EffectColor color;
	private String prefix,
					suffix;
	private EnumNameTagVisibility nameTagVisibility;
	private EnumTeamPush collision;
	private List<Player> players;
	
	public NMSTeam(String name, EffectColor color, String prefix, String suffix, EnumNameTagVisibility nameTagVisibility, EnumTeamPush collision, List<Player> players) {
		
		this.name = name;
		this.color = color;
		this.prefix = prefix;
		this.suffix = suffix;
		this.nameTagVisibility = nameTagVisibility;
		this.collision = collision;
		this.players = players;
		
	}
	
	public NMSTeam(String name, EffectColor color, String prefix, String suffix, EnumNameTagVisibility nameTagVisibility, EnumTeamPush collision) {
		
		this.name = name;
		this.color = color;
		this.prefix = prefix;
		this.suffix = suffix;
		this.nameTagVisibility = nameTagVisibility;
		this.collision = collision;
		this.players = new ArrayList<>();
		
	}
	
	@SuppressWarnings("unchecked")
	public void createPacket(final Player player, final boolean createTeam, final boolean deleteTeam, final boolean addPlayer, final boolean removePlayer, Player receiver) {
		
		PacketPlayOutScoreboardTeam team;
		
		try {
			
			team = PacketPlayOutScoreboardTeam.class.newInstance();
			
			setField(team, "i", createTeam ? 0 : addPlayer ? 3 : deleteTeam ? 1 : 4);
			setField(team, "a", this.name);
			setField(team, "e", this.nameTagVisibility.e);
			setField(team, "f", this.collision.e);
			
			if(createTeam) {
				
				setField(team, "g", this.color.getChatFormat());
				setField(team, "b", new ChatComponentText(""));
				setField(team, "c", this.prefix == null ? null : new ChatComponentText(this.prefix));
				setField(team, "d", this.suffix == null ? null : new ChatComponentText(this.suffix));
				
				if(!this.players.isEmpty()) {
					
					final Collection<String> names = new ArrayList<String>();
					
					for(Player players : this.players)
						
						names.add(players.getName());
					
					setField(team, "h", names);
					setField(team, "j", this.players.size());
					
				}else
				
					setField(team, "j", 0);
				
			}else {
				
				try {
					
					final Field field = team.getClass().getDeclaredField("h");
					
					field.setAccessible(true);
					
					if(addPlayer) {
						
						if(!this.players.contains(player))
						
							this.players.add(player);
						
						((Collection<String>) field.get(team)).add(player.getName());
					
					}else if(removePlayer) {
						
						if(this.players.contains(player))
							
							this.players.remove(player);
						
						((Collection<String>) field.get(team)).add(player.getName());
						
					}
					
					field.setAccessible(false);
					
				} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
				
			}
			
			if(receiver == null)
				
				receiver = player;
			
			sendPacket(receiver, team);
		
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public boolean isPlayerInTeam(final Player player) {
		
		if(this.players.contains(player))
			
			return true;
		
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<String> getPlayersInTeam() {
		
		try {
		
			final PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
			
			setField(team, "a", this.name);
			setField(team, "i", 2);
			
			final Collection<String> entities = (Collection<String>) FieldUtils.readDeclaredField(team, "h", true);
			
			return entities;
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EffectColor getColor() {
		return color;
	}

	public void setColor(EffectColor color) {
		this.color = color;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public EnumNameTagVisibility getNameTagVisibility() {
		return nameTagVisibility;
	}

	public void setNameTagVisibility(EnumNameTagVisibility nameTagVisibility) {
		this.nameTagVisibility = nameTagVisibility;
	}

	public EnumTeamPush getCollision() {
		return collision;
	}

	public void setCollision(EnumTeamPush collision) {
		this.collision = collision;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

}

