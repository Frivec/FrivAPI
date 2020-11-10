package fr.frivec.spigot.scoreboard.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.frivec.spigot.scoreboard.ScoreboardPattern;

public class ServerScoreboard implements ScoreboardManager {
	
	private static Map<UUID, ServerScoreboard> scoreboards = new HashMap<UUID, ServerScoreboard>();
	
	private Player player;
	private UUID uuid;
	private Scoreboard scoreboard;
	private Objective objective;
	
	private String scoreboardName;
	
	private ScoreboardPattern pattern;
	
	private String[] lines = new String[16];
	
	public ServerScoreboard(final Player player, final ScoreboardPattern pattern) {
		
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.player = player;
		this.uuid = player.getUniqueId();
		this.pattern = pattern;
		this.scoreboardName = "sb." + new Random().nextInt(9999);
		
		if(scoreboards.containsKey(this.player.getUniqueId()))
			
			return;
		
		scoreboards.put(this.player.getUniqueId(), this);
		
		this.objective = this.scoreboard.registerNewObjective(this.scoreboardName, "dummy", pattern.getObjectiveName());
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(Entry<Integer, String> entries : this.pattern.getLines().entrySet())
			
			sendLine(entries.getValue(), entries.getKey());
		
	}
	
	public void removeLine(final int line) {
		
		final String text = lines[line];
		
		if(text != null) {
			
			lines[line] = null;
			
			this.scoreboard.resetScores(text);
			
		}
			
	}
	
	public void resetLines() {
		
		for(String lines : this.lines)
			
			if(lines != null)
			
				this.scoreboard.resetScores(lines);
		
	}
	
	public void sendLine(final String text, final int line) {
		
		lines[line] = text;
		
		this.objective.getScore(text).setScore(16 - line);
		
	}
	
	public void refreshScoreboard() {
		
		this.resetLines();
		
		for(Entry<Integer, String> entries : this.pattern.getLines().entrySet())
			
			sendLine(entries.getValue(), entries.getKey());
		
		set();
				
	}
	
	public void set() {
		
		final Player player = Bukkit.getPlayer(this.uuid);
		
		if(player != null)
		
			player.setScoreboard(this.scoreboard);
		
	}
	
	public String getLine(final int line) {
		
		return lines[line];
		
	}

	@Override
	public Scoreboard getMainScoreboard() {
		return this.scoreboard;
	}

	@Override
	public Scoreboard getNewScoreboard() {
		return null;
	}
	
	public void setPattern(ScoreboardPattern pattern) {
		
		this.pattern = pattern;
		
	}
	
	public ScoreboardPattern getPattern() {
		return pattern;
	}
	
	public static ServerScoreboard getScoreboard(final Player player) {
		
		if(scoreboards.containsKey(player.getUniqueId()))
			
			return scoreboards.get(player.getUniqueId());
		
		return null;
		
	}

}
