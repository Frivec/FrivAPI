package fr.frivec.spigot.commands.dev;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.frivec.spigot.API;
import fr.frivec.spigot.commands.AbstractCommand;
import fr.frivec.spigot.effects.gloweffect.GlowEffect.EffectColor;
import fr.frivec.spigot.item.ItemCreator;
import fr.frivec.spigot.packets.NMSTeam;
import fr.frivec.spigot.packets.PacketUtils;
import fr.frivec.spigot.tags.TagsManager;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MobEffect;
import net.minecraft.server.v1_16_R3.MobEffectList;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumTeamPush;

public class DevCommand extends AbstractCommand {
	
	private final List<String> args = Arrays.asList("test");
	
	public DevCommand() {
		
		super("dev", "/dev", "dev command", "§cYou do not have the permission to use this command", Arrays.asList("developper"));
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			final Player player = (Player) sender;
			
			if(player.isOp()) {
				
				if(args[0].equalsIgnoreCase("tag"))
					
					TagsManager.setNameTag(player, "9A", ChatColor.BLUE, "§aTest ", " §bMeh");
				
				else if(args[0].equalsIgnoreCase("glow")) {
					
//					if(args[1].equalsIgnoreCase("on")) {
//						
//						API.getInstance().getGlowEffect().setGlowColor(player, color, active);
//						
//					}else if(args[1].equalsIgnoreCase("off")) {
//						
//						API.getInstance().getPacketsManager().removePlayer(player);
//						API.getInstance().getGlowEffect().removeGlowEffect(player);
//					
//					}else if(args[1].equalsIgnoreCase("red")) {
//						
////						API.getInstance().getGlowEffect().removeGlowEffect(player);
//						API.getInstance().getGlowEffect().setGlowColor(player, Color.RED);
//						API.getInstance().getPacketsManager().registerPlayer(player, new GlowListener(player));
//					
//					}
						
				}else if(args[0].equalsIgnoreCase("nmsteam")) {
					
					final NMSTeam team = new NMSTeam("Test", EffectColor.RED, "Salut", "Meh", EnumNameTagVisibility.ALWAYS, EnumTeamPush.NEVER);
					
					API.getInstance().getGlowEffect().getTeams().put(EffectColor.RED, team);
					
					player.sendMessage("§aC'est bon.");
					
				}else if(args[0].equalsIgnoreCase("teams")) {
					
//					if(args[1].equalsIgnoreCase("join")) {
//						
//						if(args[2].equalsIgnoreCase("red")) {
//							
//							final NMSTeam team = NMSTeam.getTeams().get(Color.RED);
//							
//							team.createPacket(player, false, false, true, false, null);
//							
//							player.sendMessage("§aTu as rejoint les rouges c'est bon.");
//							
//							return true;
//							
//						}else if(args[2].equalsIgnoreCase("blue")) {
//							
//							final NMSTeam team = NMSTeam.getTeams().get(Color.CYAN);
//							
//							team.createPacket(player, false, false, true, false, null);
//							
//							player.sendMessage("§aTu as rejoint les bleus c'est bon.");
//							
//						}
//						
//					}else if(args[1].equalsIgnoreCase("remove")) {
//						
//						if(args[2].equalsIgnoreCase("red")) {
//							
//							final NMSTeam team = NMSTeam.getTeams().get(Color.RED);
//							
//							team.createPacket(player, false, false, false, true, null);
//							
//							player.sendMessage("§aTu as quitté les rouges c'est bon.");
//							
//							return true;
//							
//						}else if(args[2].equalsIgnoreCase("blue")) {
//							
//							final NMSTeam team = NMSTeam.getTeams().get(Color.CYAN);
//							
//							team.createPacket(player, false, false, false, true, null);
//							
//							player.sendMessage("§aTu as quitté les bleus c'est bon.");
//							
//						}
//						
//					}
					
				}else if(args[0].equalsIgnoreCase("glowpotion")) {
					
					final EntityPlayer entityPlayer = PacketUtils.getEntityPlayer(player);
					final PacketPlayOutEntityEffect effect = new PacketPlayOutEntityEffect(entityPlayer.getId(), new MobEffect(MobEffectList.fromId(24), 99999, 255, false, false, true));
					
					for(final Player players : Bukkit.getOnlinePlayers())
						
						PacketUtils.sendPacket(players, effect);
					
				}else if(args[0].equalsIgnoreCase("jump")) {
					
					player.sendMessage("§aSaut");
					player.getInventory().setChestplate(new ItemCreator(Material.ELYTRA, 1).build());
					player.setVelocity(new Vector(0, 5, 0));
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(API.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							enableGliding(player);
							player.sendMessage("§aEnvol");
							
						}
						
					}, 30l);
					
				}else if(args[0].equalsIgnoreCase("biome")) {
					
					final Location location = player.getLocation();
					final World world = location.getWorld();
					final Biome biome = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
					
				}
				
			}else {
				
				player.sendMessage(this.permMessage);
				return true;
				
			}
			
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		for(String names : this.alias)
			
			if(cmd.getName().equalsIgnoreCase(names) || cmd.getName().equalsIgnoreCase(this.command))
				
				if(sender instanceof Player)
					
					return this.args;
		
		return super.onTabComplete(sender, cmd, label, args);
	}
	
	private void enableGliding(final Player player) {
		
    	final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    	final DataWatcherObject<Byte> object = new DataWatcherObject<>(0, DataWatcherRegistry.a);
    	final DataWatcher current = entityPlayer.getDataWatcher(),
    						dataWatcher = new DataWatcher(entityPlayer);
    	
    	final byte initialBitMask = current.get(object),
    			bitMask = (byte) 0x80,
    			newByte = (byte) (initialBitMask | bitMask);
    	
    	dataWatcher.register(object, newByte);

        final PacketPlayOutEntityMetadata entityMetaData = new PacketPlayOutEntityMetadata(entityPlayer.getId(), dataWatcher, true);
        
        for(Player players : Bukkit.getOnlinePlayers())
                
           ((CraftPlayer) players).getHandle().playerConnection.sendPacket(entityMetaData);
        
        player.setGliding(true);
		
	}

}
