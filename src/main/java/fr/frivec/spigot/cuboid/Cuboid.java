package fr.frivec.spigot.cuboid;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Cuboid {
	
	private int maxX;
	private int maxY;
	private int maxZ;

	private int minX;
	private int minY;
	private int minZ;

	public Cuboid(Location l, Location l2) {

		if (l.getBlockX() > l2.getBlockX()) {
		
			maxX = l.getBlockX();
			minX = l2.getBlockX();
		
		} else {
		
			maxX = l2.getBlockX();
			minX = l.getBlockX();
		
		}
		
		if (l.getBlockY() > l2.getBlockY()) {
		
			maxY = l.getBlockY();
			minY = l2.getBlockY();
		
		} else {
		
			maxY = l2.getBlockY();
			minY = l.getBlockY();
		
		}
		
		if (l.getBlockZ() > l2.getBlockZ()) {
			
			maxZ = l.getBlockZ();
			minZ = l2.getBlockZ();
		
		} else {
		
			maxZ = l2.getBlockZ();
			minZ = l.getBlockZ();
		
		}

	}

	public boolean isInCube(Player p) {

		Block b = p.getLocation().getBlock();
		
		if ((b.getX() <= maxX) && (b.getX() >= minX) 
				&& (b.getY() <= maxY) && (b.getY() >= minY)
				&& (b.getZ() <= maxZ) && (b.getZ() >= minZ)) {
			
			return true;
		
		}
		
		return false;
	
	}

}