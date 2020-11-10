package fr.frivec.spigot.scoreboard;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardPattern {
	
	/**
	 * 
	 * @author Frivec
	 * 
	 */
	
	private String objectiveName;
	private Map<Integer, String> lines;
	
	/**
	 * Create a scoreboard pattern with all lines and objective name. It will be reusable later
	 * @param objectiveName of the scoreboard
	 */
	public ScoreboardPattern(final String objectiveName) {
		
		this.objectiveName = objectiveName;
		this.lines = new HashMap<>();
		
	}
	
	/**
	 * Add a line with an index. Exemple, first line of the scoreboard can be 0
	 * @param line: id of the line in the scoreboard. (0-16)
	 * @param text: text that will be set on the line on the scoreboard
	 */
	public void setLine(final int line, final String text) {
		
		if(this.lines.containsKey(line))
			
			removeLine(line);
		
		this.lines.put(line, text);
		
	}
	
	/**
	 * Remove a line from the pattern
	 * @param line: id of the line that will be removed
	 */
	public void removeLine(final int line) {
		
		if(this.lines.containsKey(line))
			
			this.lines.remove(line);
		
	}
	
	public String getObjectiveName() {
		return objectiveName;
	}
	
	public Map<Integer, String> getLines() {
		return lines;
	}

}
