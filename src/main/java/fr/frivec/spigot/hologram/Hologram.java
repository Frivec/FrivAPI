package fr.frivec.spigot.hologram;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import fr.frivec.spigot.scoreboard.ScoreboardPattern;

public class Hologram {
	
	private static Map<String, Hologram> holograms = new HashMap<>();
	
	private String name;
	private Location location;
	private ScoreboardPattern pattern;
	private Map<Integer, String> lines;
	private Map<Integer, ArmorStand> as;
	
	public Hologram(final String name, final Location location) {
		
		this.name = name;
		this.location = location;
		this.lines = new HashMap<>();
		this.as = new HashMap<>();
		
		holograms.put(name, this);
		
	}
	
	public Hologram(final Location location, final ScoreboardPattern pattern) {
		
		this.name = pattern.getObjectiveName();
		this.location = location;
		this.lines = new HashMap<>();
		this.as = new HashMap<>();
		this.pattern = pattern;
		
		holograms.put(name, this);
		
		this.spawn();
		
		for(String lines : this.pattern.getLines().values())
			
			addLine(lines);
		
	}
	
	public void spawn() {
		
		for(Entry<Integer, String> line : this.lines.entrySet())
			
			createArmorStand(line.getKey(), line.getValue());
		
	}
	
	public void teleport(final Location newLocation) {
		
		this.location = newLocation;
		deleteArmorStands();
		spawn();
		
	}
	
	public void addLine(final String text) {
		
		int index = 0;
		
		for(Entry<Integer, String> lines : this.lines.entrySet())
			
			if(lines.getKey() >= index)
				
				index = lines.getKey() + 1;
		
		this.lines.put(index, text);
		createArmorStand(index, text);
		
	}
	
	public boolean setLine(final int index, final String text) {
		
		if(this.lines.containsKey(index)) {
			
			this.lines.remove(index);
			this.lines.put(index, text);
			
			this.as.get(index).setCustomName(text);
			
			return true;
			
		}
		
		return false;
		
	}
	
	public boolean removeLine(final int index) {
		
		if(!this.lines.containsKey(index))
			
			return false;
		
		this.lines.remove(index);
		
		for(Entry<Integer, String> entries : this.lines.entrySet()) {
			
			final int id = entries.getKey();
			
			if(id > index) {
				
				final String text = entries.getValue();
				
				this.lines.remove(id);
				this.lines.put((id - 1), text);
				
			}
			
		}
		
		deleteArmorStands();
		spawn();
		
		return true;
		
	}
	
	public void delete() {
		
		this.lines.clear();
		deleteArmorStands();
		
		getHolograms().remove(this.name);
		
	}
	
	private void createArmorStand(final int index, final String text) {
		
		final ArmorStand as = (ArmorStand) this.location.getWorld().spawnEntity(this.location.clone().subtract(0, (index + 1) * 0.3, 0), EntityType.ARMOR_STAND);
		
		as.setAI(false);
		as.setGravity(false);
		as.setArms(false);
		as.setInvulnerable(true);
		as.setBasePlate(false);
		as.setVisible(false);
		as.setCustomName(text);
		as.setCustomNameVisible(true);
		
		this.as.put(index, as);
		
	}
	
	private void deleteArmorStands() {
		
		for(Entry<Integer, ArmorStand> as : this.as.entrySet())
			
			as.getValue().remove();
		
		this.as.clear();
		
	}
	
	public static Map<String, Hologram> getHolograms() {
		return holograms;
	}
	
	public Map<Integer, String> getLines() {
		return lines;
	}
	
	public Map<Integer, ArmorStand> getAs() {
		return as;
	}
	
	public Location getLocation() {
		return location;
	}

}
