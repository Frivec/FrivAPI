package fr.frivec.spigot.commands.administrator;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.frivec.spigot.commands.AbstractCommand;
import fr.frivec.spigot.hologram.Hologram;

import static fr.frivec.core.string.StringUtils.*;
import static fr.frivec.spigot.hologram.Hologram.getHolograms;

public class HologramCommand extends AbstractCommand {
	
	public HologramCommand() {
		
		super("hologram", "§c/hologram <create/addline/setline/removeline/delete/teleport> <nom de l'hologramme> (index du texte) (texte)", "Commande pour crééer des hologrames.", "§cErreur. Vous n'avez pas la permission d'utiliser cette commande.", Arrays.asList("hologramme, holograms"));
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			final Player player = (Player) sender;
			
			if(args.length > 0) {
				
				final String firstArg = args[0];
				
				if(compareStringRelative(firstArg, "addline")) {
					
					/*
					 * 
					 * Add a line to an existing hologram
					 * 
					 */
					
					if(args.length >= 2) {
						
						final String hologram = args[1];
						final StringBuilder text = new StringBuilder();
						
						for(int i = 2; i < args.length; i++)
							
							text.append((i + 1) == args.length ? args[i] : args[i] + " ");
						
						if(getHolograms().containsKey(hologram))
							
							getHolograms().get(hologram).addLine(translateColorSymbol(text.toString()));
						
						else {
							
							player.sendMessage("§cErreur. Cet hologramme n'existe pas.");
							player.sendMessage("§bPour le créer, faites: /hologram create §a" + hologram);
							
							return true;
							
						}
						
						player.sendMessage("§aLa ligne a bien été ajoutée à l'hologramme §b" + hologram);
						return true;
						
					}else {
						
						player.sendMessage("§c/hologram addline <nom de l'hologramme> <texte>");
						return true;
						
					}
					
				}else if(compareStringRelative(firstArg, "create")) {
					
					/*
					 * 
					 * Creation of a new hologram
					 * 
					 */
					
					if(args.length >= 2) {
						
						final String hologram = args[1];
						
						if(getHolograms().containsKey(hologram)) {
							
							player.sendMessage("§cErreur. Cet hologramme existe déjà.");
							player.sendMessage("§aPour le modifier, faites: /hologram <addline/removeline/setine/delete> " + hologram);
							
							return true;
							
						}
						
						new Hologram(hologram, player.getLocation());
						
						player.sendMessage("§aL'hologramme §b" + hologram + " §aa bien été créé.");
						
						return true;
						
					}else {
						
						player.sendMessage("§c/hologram create <nom de l'hologramme>");
						return true;
						
					}
					
				}else if(compareStringRelative(firstArg, "removeLine")) {
					
					/*
					 * 
					 * Remove a line from an existing hologram
					 * 
					 */
					
					if(args.length >= 3) {
						
						final String hologramName = args[1];
						
						try {
							
							final int index = Integer.valueOf(args[2]);
							
							if(index > 0) {
								
								if(getHolograms().containsKey(hologramName)) {
									
									final Hologram hologram = getHolograms().get(hologramName);
									
									if(hologram.getLines().containsKey((index - 1))) {
										
										hologram.removeLine((index - 1));
										player.sendMessage("§aLa ligne §6" + index + "§a a bien été retirée.");
										
										return true;
										
									}else {
										
										player.sendMessage("§cErreur. Cette ligne n'existe pas dans l'hologramme §6" + hologramName + "§c.");
										player.sendMessage("§bPour l'ajouter, faites: /hologram addLine <texte>");
										
										return true;
										
									}
									
								}else {
									
									player.sendMessage("§cErreur. Cet hologramme n'existe pas.");
									player.sendMessage("§bPour le créer, faites: /hologram create §a" + hologramName);
									
									return true;
									
								}
								
							}else {
								
								player.sendMessage("§cErreur. L'index doit être strictement positif.");
								return true;
								
							}
							
						} catch (NumberFormatException e) {
							
							player.sendMessage("§cErreur. La valeur de l'index de la ligne n'est pas un nombre entier.");
							
							return true;
							
						}						
						
					}else {
						
						player.sendMessage("§c/hologram removeLine <nom de l'hologramme> <index>");
						
						return true;
						
					}
					
				}else if(compareStringRelative(firstArg, "setline")) {
					
					/*
					 * 
					 * Set an existing line in a existing hologram
					 * 
					 */
					
					if(args.length >= 4) {
						
						final String hologramName = args[1];
						final StringBuilder text = new StringBuilder();
						
						try {
							
							final int index = Integer.valueOf(args[2]);
							
							if(getHolograms().containsKey(hologramName)) {
								
								final Hologram hologram = getHolograms().get(hologramName);
								
								if(hologram.getLines().containsKey((index - 1))) {
									
									for(int i = 3; i < args.length; i++)
										
										text.append((i + 1) == args.length ? args[i] : args[i] + " ");
									
									hologram.setLine((index - 1), translateColorSymbol(text.toString()));
									
									player.sendMessage("§aLa ligne §6" + index + " §aa bien été modifiée.");
									
									return true;
									
								}else {
									
									player.sendMessage("§cErreur. Cette ligne n'existe pas dans l'hologramme §6" + hologramName + "§c.");
									player.sendMessage("§bPour l'ajouter, faites: /hologram addline <nom de l'hologramme> <texte>");
									
									return true;
									
								}
								
							}else {
								
								player.sendMessage("§cErreur. Cet hologramme n'existe pas.");
								player.sendMessage("§bPour le créer, faites: /hologram create §a" + hologramName);
								
								return true;
								
							}
							
						} catch (NumberFormatException e) {
							
							player.sendMessage("§cErreur. La valeur de l'index de la ligne n'est pas un nombre entier.");
							
							return true;
							
						}
						
					}else {
						
						player.sendMessage("§c/hologram setline <nom de l'hologramme> <index> <texte>");
						
						return true;
						
					}
					
				}else if(compareStringRelative(firstArg, "delete")) {
					
					if(args.length >= 2) {
						
						final String hologramName = args[1];
						
						if(getHolograms().containsKey(hologramName)) {
							
							getHolograms().get(hologramName).delete();
							
							player.sendMessage("§aL'hologramme §6" + hologramName + " §aa bien été supprimé !");
							
							return true;
							
						}else {
							
							player.sendMessage("§cErreur. Cet hologramme n'existe pas.");
							player.sendMessage("§bPour le créer, faites: /hologram create §a" + hologramName);
							
							return true;
							
						}
						
					}else {
						
						player.sendMessage("§c/hologram delete <nom de l'hologramme>");
						
						return true;
						
					}
					
				}else if(compareStringRelative(firstArg, "teleport")) {
					
					if(args.length >= 2) {
						
						final String hologramName = args[1];
						
						if(getHolograms().containsKey(hologramName)) {
							
							getHolograms().get(hologramName).teleport(player.getLocation());
							
							player.sendMessage("§aL'hologramme §6" + hologramName + "§a a bien été téléporté sur vous.");
							
							return true;
							
						}else {
							
							player.sendMessage("§cErreur. Cet hologramme n'existe pas.");
							player.sendMessage("§bPour le créer, faites: /hologram create §a" + hologramName);
							
							return true;
							
						}
						
					}else {
						
						player.sendMessage("§c/hologram teleport <nom de l'hologramme>");
						
						return true;
						
					}
					
				}else {
					
					
					
				}
				
			}else {
				
				player.sendMessage("§cErreur. Il manque des arguments.");
				
				return true;
				
			}
			
		}else {
			
			sender.sendMessage("§cErreur. Cette commande ne peut être utilisée qu'en jeu.");
			return true;
			
		}
		
		return false;
	}
	
}
