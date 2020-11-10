package fr.frivec.spigot.scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.spigot.API;
import fr.frivec.spigot.scoreboard.client.ScoreboardSign;

public class ScoreboardAlternate extends BukkitRunnable {
	
	private Map<Integer, ScoreboardPattern> orderList;
	private Set<Player> playerList;
	
	private int currentID = 0;
	
	/**
	 * Set an alternation between a few scoreboards
	 * The scoreboards will be saw by the players added in the list while they are online
	 * 
	 * @param scoreboards: list of the scoreboards you want to alternate
	 * @param order: order of apparition of these scoreboards
	 * @param delay: by default, set 0
	 * @param period: Period in ticks between two lap (20 for one second)
	 *
	 */
	public ScoreboardAlternate(final ScoreboardPattern[] scoreboards, final int[] order, final long delay, final long period) {
		
		this.orderList = new HashMap<>();
		this.playerList = new HashSet<>();
		
		if(scoreboards.length != order.length) {
			
			API.log(LogLevel.WARNING, "Error. The scoreboard task can't be applied because the lenghts of the scoreboard list and order ist aren't the same.");
			
			return;
			
		}
		
		for(int i = 0; i < scoreboards.length; i++)
			
			this.orderList.put(order[i] - 1, scoreboards[i]);
		
		this.runTaskTimer(API.getInstance(), delay, period);
		
	}
	
	/**
	 * Add a player to the list of viewers of the scoreboards
	 * @param player: Player you want to add
	 */
	public void addPlayer(final Player player) {
		
		this.playerList.add(player);
		
	}
	
	/**
	 * Remove a player from the list of viewers of the scoreboards
	 * @param player: Player you want to remove
	 */
	public void removePlayer(final Player player) {
		
		this.playerList.remove(player);
		
	}
	
	@Override
	public void run() {
		
		final ScoreboardPattern pattern = this.orderList.get(currentID);
		
		for(Player player : this.playerList) {
			
			if(player.isOnline()) {
				
				API.log(LogLevel.INFO, "test");
				
				if(ScoreboardSign.getCurrentScoreboard(player) != null)
					
					ScoreboardSign.getCurrentScoreboard(player).destroy();
				
				new ScoreboardSign(player, pattern).create();
				
			}
			
		}
		
		currentID++;
		
		if(currentID > this.orderList.size() - 1)
			
			this.currentID = 0;
		
	}

}
