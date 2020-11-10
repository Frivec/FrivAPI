package fr.frivec.spigot.listeners.dev;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.spigot.API;

public class DevListener implements Listener {
	
//	@SuppressWarnings("unchecked")
//	@EventHandler
//	public void onSneak(final PlayerToggleSneakEvent event) {
//		
//		final Player player = event.getPlayer();
//		final EntityPlayer entityPlayer = PacketUtils.getEntityPlayer(player);
//		final DataWatcher dataWatcher = entityPlayer.getDataWatcher();
//		
//		try {
//		
//			final Int2ObjectOpenHashMap<DataWatcher.Item<Byte>> map = (Int2ObjectOpenHashMap<Item<Byte>>) FieldUtils.readDeclaredField(dataWatcher, "entries", true);
//			
//			System.out.println("entity id: " + entityPlayer.getId());
//			
//			for(DataWatcher.Item<?> items : map.values()) {
//				
//				System.out.println("datawatcher object: " + items.a() + " | T: " + items.b() + " | boolean c: " + items.c());
//				
//			}
//			
//			System.out.println("-----------------------------------------------------------------");
//			
//		} catch (IllegalAccessException e) {
//			
//			e.printStackTrace();
//		
//		}
//		
//	}
	
	@EventHandler
	public void onToogleGlideEvent(final EntityToggleGlideEvent event) {
		
		API.log(LogLevel.INFO, "Entity: " + event.getEntity().getName() + " | gliding: " + event.isGliding());
		
	}

}
