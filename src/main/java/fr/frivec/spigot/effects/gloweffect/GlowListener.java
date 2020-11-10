package fr.frivec.spigot.effects.gloweffect;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import fr.frivec.spigot.packets.PacketUtils;
import fr.frivec.spigot.packets.listeners.PacketListener;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;

public class GlowListener implements PacketListener {
	
	private Set<Player> viewers;
	
	private byte bitMask = 1 << 6;
	
	private DataWatcherObject<Byte> object;
	private DataWatcher dataWatcher;
	
	private boolean firstPacket = true;
	
	public GlowListener(final Player player) {
		
		this(player, new HashSet<>());
		
	}
	
	public GlowListener(final Player player, final Set<Player> viewers) {
		
		this.viewers = viewers;
		
		this.object = new DataWatcherObject<>(0, DataWatcherRegistry.a);
		this.dataWatcher = new DataWatcher(PacketUtils.getEntityPlayer(player));
		
	}
	
	@Override
	public void onPacketRead(Player player, Packet<?> packet) {/**/}
	
    @Override
    public void onPacketWrite(final Player player, final Packet<?> packet) throws IllegalAccessException {
		
        if(packet instanceof PacketPlayOutEntityMetadata) {
        	
        	final int id = (int) PacketUtils.readField(packet, "a");
        	
        	if(id < 0) //My Packet
        		
        		PacketUtils.setField(packet, "a", -id);
        		
        	else {//Modify the real packet
        	
	        	final EntityPlayer entityPlayer = PacketUtils.getEntityPlayer(player);
	        	final DataWatcher current = entityPlayer.getDataWatcher();
	        	
	        	final byte initialBitMask = current.get(this.object),
	        			newByte = (byte) (initialBitMask | this.bitMask);
	        	
	        	if(this.firstPacket) {
	            
	        		this.dataWatcher.register(this.object, newByte);
	        		this.firstPacket = false;
	        		
	        	}else
	        		
	        		this.dataWatcher.set(this.object, newByte);
	
	            final PacketPlayOutEntityMetadata entityMetaData = new PacketPlayOutEntityMetadata(-entityPlayer.getId(), this.dataWatcher, true);
	            
	            for(Player players : this.viewers)
	                    
	                PacketUtils.sendPacket(players, entityMetaData);
	            
        	}
            
        }
        
    }
	
	public Set<Player> getViewers() {
		return viewers;
	}

}
