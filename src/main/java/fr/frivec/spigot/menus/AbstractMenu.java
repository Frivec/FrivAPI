package fr.frivec.spigot.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractMenu implements Listener {
	
	/**
	 * @author Frivec
	 * Class inspired by Samagames Development Team work
	 * 
	 */
	
	protected Inventory inventory;
	protected String name;
	
	private JavaPlugin javaPlugin;
	
	protected Map<Integer, String> actions;
	
	/**
	 * 
	 * Create a new inventory
	 * 
	 * @param javaPlugin, the main class of the plugin
	 * @param title of the inventory
	 * @param number of slots
	 */
	
	public AbstractMenu(final JavaPlugin javaPlugin, final String title, int slots) {
		
		this.javaPlugin = javaPlugin;
		this.inventory = Bukkit.createInventory(null, slots, title);
		this.name = title;
		this.actions = new HashMap<>();
		this.javaPlugin.getServer().getPluginManager().registerEvents(this, this.javaPlugin);
		
	}
	
	/**
	 * Action when the inventory is opened by the player
	 * 
	 * defines all the items, their slots and their actions
	 * finish this method by the player.openInventory(this.inventory);
	 * 
	 * @param player who opens the inventory
	 */
	
	public abstract void open(final Player player);
	
	/**
	 * Action when the inventory is closed
	 * 
	 * @param player who closes the inventory
	 */
	
	public abstract void close(final Player player);
	
	/**
	 * Actions executed when the player interact with the inventory
	 * 
	 * 
	 * @param player who interacts with the inventory
	 * @param itemstack: the current item used
	 * @param slot: the current item's slot
	 * @param the action (right click, middle click or left click) of the player on the item
	 */
	
	public abstract void onInteract(final Player player, final ItemStack itemStack, final int slot, final InventoryAction action);
	
	/**
	 * 
	 * check if a player is using the inventory
	 * 
	 * @return a boolean (true if a player is using it, false if not)
	 */
	
	public boolean isPlayerUsingInventory() {
		
		if(this.inventory.getViewers().get(0).hasMetadata("GUI_USE_" + this.name))
			
			return true;
		
		return false;
		
	}
	
	/**
	 * 
	 * add an item to the inventory with an action
	 * 
	 * @param itemStack which is added to the inventory
	 * @param slot where the item is added
	 * @param action which is associate to the item
	 */
	
	public void addItem(final ItemStack itemStack, int slot, String action) {
		
		if(itemStack != null)
		
			this.inventory.setItem(slot, itemStack);
		
		this.actions.put(slot, action);
		
	}
	
	/**
	 * 
	 * inventory click event
	 * 
	 * execute the onInteract method
	 * 
	 */
	
	@EventHandler
	public void onCloseInventory(final InventoryCloseEvent e) {
		
		final Player player = (Player) e.getPlayer();
		final AbstractMenu menu = MenuManager.getMenu(player);
		
		if(menu != null) {
			
			menu.actions.clear();
			
			MenuManager.getInstance().onCloseMenu(player, menu);
			
		}
			
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		final Player player = (Player) e.getWhoClicked();
		final AbstractMenu menu = MenuManager.getMenu(player);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
			
			return;
		
		if(menu != null && this.equals(menu)) {
			
			e.setCancelled(true);
			
			if(this.actions.get(e.getSlot()) == null || this.actions.get(e.getSlot()).equals(""))
				
				return;
			
			MenuManager.getMenu(player).onInteract(player, e.getCurrentItem(), e.getSlot(), e.getAction());
			
		}
		
	}
	
}
