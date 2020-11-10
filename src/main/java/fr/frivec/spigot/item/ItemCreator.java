package fr.frivec.spigot.item;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public class ItemCreator {
	
    private static final Base64 base64 = new Base64();
	
	private ItemStack itemStack;
	private ItemMeta itemMeta;
	
	/**
	 * 
	 * Create item by using a material and how many items you want
	 * @param set the material of the item
	 * @param set the amount of the itemstack
	 * 
	 */
	
	public ItemCreator(Material material, int amount) {
		this.itemStack = new ItemStack(material, amount);
		this.itemMeta = this.itemStack.getItemMeta();
	}
	
	public ItemCreator(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemMeta = this.itemStack.getItemMeta();
	}
	
	/**
	 * 
	 * Create a skull itemstack
	 * @param set the owner's name
	 * @return a new itemcreator
	 * 
	 */
	
	public ItemCreator leatherArmor(final Material material, final Color color) {
		
		final ItemStack itemStack = new ItemStack(material, 1);
		final LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		
		itemMeta.setColor(color);
		
		if(this.itemMeta.getDisplayName() != null && !this.itemMeta.getDisplayName().equalsIgnoreCase(""))
			
			itemMeta.setDisplayName(this.itemMeta.getDisplayName());
			
		itemStack.setItemMeta(itemMeta);
		
		this.itemStack = itemStack;
		this.itemMeta = itemMeta;
		
		return this;
		
	}
	
	public ItemCreator firework(final int power, final FireworkEffect... effects) {
		
		final ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET, 1);
		final FireworkMeta meta = (FireworkMeta) itemStack.getItemMeta();
		meta.addEffects(effects);
		meta.setPower(power);
		
		itemStack.setItemMeta(meta);
		
		this.itemStack = itemStack;
		this.itemMeta = meta;
		
		return this;
		
	}
	
	@SuppressWarnings("deprecation")
	public ItemCreator skull(String owner) {
		
		final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
		final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
		itemStack.setItemMeta(meta);
		
		return new ItemCreator(itemStack);
		
	}
	
	/**
	 * 
	 * Create a skull itemstack
	 * @param set the owner's uuid
	 * @return a new itemcreator
	 * 
	 */
	
	public ItemCreator skull(UUID owner) {
		
		final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
		final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
		itemStack.setItemMeta(meta);
		
		return new ItemCreator(itemStack);
		
	}
	
	public ItemCreator skullByUrl(String url) {
		
		return new ItemCreator(getCustomSkull(url));
		
	}
	
	public ItemCreator unbreakable(final boolean unbreakable) {
		
		this.itemMeta.setUnbreakable(unbreakable);
		
		return this;
		
	}
	
	public ItemCreator addItemFlag(final ItemFlag...flags) {
		
		this.itemMeta.addItemFlags(flags);
		
		return this;
		
	}
	
	/**
	 * 
	 * Method by TigerHix
	 * https://github.com/TigerHix/Hex-Utils/blob/master/hex/util/Skull.java
	 * 
	 */
	
	public ItemStack getCustomSkull(String url) {
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        
        byte[] encodedData = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta headMeta = head.getItemMeta();
        
        Class<?> headMetaClass = headMeta.getClass();
        
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        
        head.setItemMeta(headMeta);
        
        return head;
    }
	
	public ItemCreator setAmount(int amount) {
		this.itemStack.setAmount(amount);
		return this;
	}
	
	public ItemCreator setDisplayName(String name) {
		this.itemMeta.setDisplayName(name);
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public ItemCreator setDurability(short durability) {
		this.itemStack.setDurability(durability);
		return this;
	}
	
	public ItemCreator setLores(String... lores) {
		this.itemMeta.setLore(Arrays.asList(lores));
		return this;
	}
	
	public ItemCreator setLores(List<String> lores) {
		this.itemMeta.setLore(lores);
		return this;
	}
	
	public ItemCreator addUnsafeEnchantment(Enchantment enchantment, int level) {
		this.itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}
	
	public ItemCreator addEnchantment(Enchantment enchantment, int level) {
		this.itemMeta.addEnchant(enchantment, level, false);
		return this;
	}
	
	public ItemCreator addItemFlag(ItemFlag flag) {
		this.itemMeta.addItemFlags(flag);
		return this;
	}
	
	public ItemStack build() {
		this.itemStack.setItemMeta(itemMeta);
		return this.itemStack;
	}
	
	public ItemMeta getItemMeta() {
		return itemMeta;
	}
	
}
