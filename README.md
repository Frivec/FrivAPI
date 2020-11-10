# FrivAPI

FrivAPI is my personnal API to create Spigot/paper plugins for minecraft servers.

**Licence**

[![CC BY-NC-ND 4.0][cc-by-nc-nd-shield]][cc-by-nc-nd]

**This work is licensed under a** [Attribution-NonCommercial-NoDerivatives 4.0 International License][cc-by-nc-nd].

[![CC BY-NC-ND 4.0][cc-by-nc-nd-image]][cc-by-nc-nd]

[cc-by-nc-nd]: http://creativecommons.org/licenses/by-nc-nd/4.0/
[cc-by-nc-nd-image]: https://licensebuttons.net/l/by-nc-nd/4.0/88x31.png
[cc-by-nc-nd-shield]: https://img.shields.io/badge/License-CC%20BY--NC--ND%204.0-lightgrey.svg

## Description

This project is my personnal API.

Today, this plugin contains features to add glowing packets, holograms, scoreboards (server and client side), packets listeners (without ProtocolLib).
You can use it on your servers but you have to keep it like this and to mention my name as the author of the API.

## Documentation

### Glowing packets

This feature helps you to show players with the glowing effect of the color of your choice.
Thanks to packets, the glowing effect can be seen only by some players of your choice.
Can be use for minigames with teams as an example.

#### Code example :

	private void setGlowColor(final Player player, final EffectColor color) {
			
		//Get the instance of GlowEffect
		final GlowEffect effect = API.getInstance().getGlowEffect();
			
		//Apply the glowing effect on the player
		effect.setGlowColor(player, color, true); 
		
		//Your list of players who will see the glowing effect. They're named as "viewers"
		final ArrayList<Player> viewers = Arrays.asList(Bukkit.getPlayer("Frivec"));
		
		//Add the players in the viewers' list. Works too for only one player.
		effect.addViewer(player, viewers);
		
	}

### ScoreboardPattern :

Pattern for your scoreboards. Needed to use the ServerScoreboard class

### Code example :

	private void testPattern() {
		
		//Create the pattern
		final ScoreboardPattern pattern = new ScoreboardPattern("Title of the scoreboard");
		
		//Set the text for the first and second line
		pattern.setLine(0, "First Line");
		pattern.setLine(1, "second Line");
		
		//Remove the first line
		pattern.removeLine(0);
		
	}

### ServerScoreboard (Server-side) :

Create a scoreboard using Bukkit API. Needs an instance of ScoreboardPattern.
**You have to use it if you use the glowing effect !**

### Code example :

	private void setScoreboard(final Player player, final ScoreboardPattern pattern) {
		
		//Enable the scoreboard for the player
		ServerScoreboard scoreboard = new ServerScoreboard(player, pattern).set();
		
		//Remove the first line of the scoreboard
		scoreboard.removeLine(0);
		
		//Set the text for first line
		scoreboard.sendLine("Hello World", 0);
		
		//Reset the text on all lines of the scoreboard
		scoreboard.resetLines();
		
		//Refresh the values on the scoreboard. Refresh all the lines according to pattern values.
		scoreboard.refreshScoreboard();
		
		//Get the player's scoreboard instance
		scoreboard = ServerScoreboard.getScoreboard(player);
		
		//Update the pattern of the scoreboard
		scoreboard.setPattern(pattern);
		
	}
	
### ScoreboardSign (Client-side) :

Create a scoreboard using packets. Needs an instance of ScoreboardPattern.
**Do not use it if you're using the glowing effect of this API**

The original work belong to Zyuiop(https://gist.github.com/zyuiop/8fcf2ca47794b92d7caa). Updated by Frivec.

### Code example :

	private void setScoreboard(final Player player, final ScoreboardPattern pattern) {
		
		//Enable the scoreboard for the player
		Scoreboardsign scoreboard = new ScoreboardSign(player, pattern).create();
		
		//Destroy the scoreboard
		scoreboard.destroy();
		
		//Remove the first line of the scoreboard
		scoreboard.removeLine(0);
		
		//Set the text for first line
		scoreboard.setLine(0, "Hello World");
		
		//Get the first line
		scoreboard.getLine(0);
		
		//Get the player's scoreboard instance
		scoreboard = ScoreboardSign.getCurrentScoreboard(player);
		
		//Update the pattern of the scoreboard
		scoreboard.setPattern(pattern);
		
		//Refresh the scoreboard using the values of the pattern
		scoreboard.applyPattern();
		
	}
	
## ScoreboardAlternate :

Create a timer to change the scoreboard of a player every x seconds.
**Works only with ScoreboardSign at this moment.

### Code Example :
	
	private void createAlternateScoreboard(final Player player) {
		
		//Get your scoreboard patterns
		final ScoreboardPattern infos = this.infos, stats = this.stats;
		
		final ScoreboardPattern[] list = new ScoreboardPattern[] {infos, stats};
		final int[] order = new int[] {1, 2}; //Order of apparition for the patterns
		
		final ScoreboardAlternate alternate = new ScoreboardAlternate(list, order, delayInTicks, periodInTicks);
		
		//Add a player to the alternate.
		alternate.addPlayer(player);
		
		//Remove a player from the alternate.
		alternate.removePlayer(player);
		
	}
	
**This readme file in currently in construction. Please wait for the end.
