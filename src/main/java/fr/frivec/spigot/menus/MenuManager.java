package fr.frivec.spigot.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.spigot.API;
import fr.frivec.spigot.managers.AbstractManager;

public class MenuManager extends AbstractManager {
	
	private static Map<UUID, AbstractMenu> menus;
	
	private static MenuManager instance;
	
	public MenuManager(final JavaPlugin javaPlugin) {
		
		super(javaPlugin);
		
		menus = new HashMap<>();
		instance = this;
		
	}
	
    @Override
    public void onDisable() {
    	
        this.javaPlugin.getServer().getOnlinePlayers().forEach(this::onDisconnect);
        
    }

    @Override
    public void onConnect(Player player) { /*Not needed*/ }
    
    @Override
    public void onDisconnect(Player player) {
    	
    	onCloseMenu(player, MenuManager.getMenu(player));
    	
    }
    
    public void onOpenMenu(final Player player, final AbstractMenu menu) {
    	
    	if(menus.containsKey(player.getUniqueId())) {
    		
    		menus.get(player.getUniqueId()).close(player);
    		menus.remove(player.getUniqueId());
    		player.closeInventory();
    		
    	}
    	
    	menus.put(player.getUniqueId(), menu);
    	player.setMetadata("GUI_USE_" + menu.name, new FixedMetadataValue(API.getInstance(), true));
    	menu.open(player);
    	
    }
    
    public void onCloseMenu(final Player player, final AbstractMenu menu) {
    	
    	if(menus.containsKey(player.getUniqueId())) {
    		
    		menu.close(player);
    		InventoryClickEvent.getHandlerList().unregister(menu);
    		menus.remove(player.getUniqueId());
    		
    	}
    	
    }
    
    public static AbstractMenu getMenu(final Player player) {
    	
    	if(menus.containsKey(player.getUniqueId()))
    		
    		return menus.get(player.getUniqueId());
    	
    	return null;
    	
    }
    
    public static MenuManager getInstance() {
		return instance;
	}
    
}
