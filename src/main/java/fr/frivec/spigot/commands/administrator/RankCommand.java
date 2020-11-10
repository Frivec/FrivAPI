package fr.frivec.spigot.commands.administrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.frivec.core.ranks.RankList;
import fr.frivec.spigot.commands.AbstractCommand;

public class RankCommand extends AbstractCommand {

	public RankCommand() {
		super("rank", "/rank <player> <rank>", "Set a new rank to a player by using its power or its name", "§cErreur. Vous n'avez pas la permission d'utiliser cette commande.", Arrays.asList("ranks"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length > 0) {
			
			if(args.length == 1) {
				
				sender.sendMessage("§cErreur. Il manque le grade que vous voulez attribuer à ce joueur.");
				sender.sendMessage(this.usage);
				
				return true;
				
			}else if(args.length >= 2) {
				
				final String playerName = args[0],
							rankID = args[1];
				
				RankList rank = RankList.PLAYER;
				
				if(rankID instanceof String)
					
					rank = RankList.getRank(rankID);
				
				else
					
					rank = RankList.getRank(Integer.valueOf(rankID));
				
				final Player target = Bukkit.getPlayer(playerName);
				
				if(target != null)
					
					target.sendMessage("§aVotre grade a été mis à jour. Vous êtes à présent: " + rank.getName() + ".");
				
				sender.sendMessage("§aAction effectuée. Le grade du joueur " + playerName + " a bien été mis à jour.");
				sender.sendMessage("§7§m--------------------------------------------");
				sender.sendMessage("§bRésumé des actions:");
				sender.sendMessage("§6Passage du grade de " + playerName + " de <?> à " + rank.getName());
				
				return true;
				
			}
			
		}else {
			
			sender.sendMessage("§cErreur. Il manque des arguments.");
			sender.sendMessage(this.usage);
			
			return true;
			
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 1) {
			
			final List<String> names = new ArrayList<>();
			
			for(Player players : Bukkit.getOnlinePlayers())
				
				names.add(players.getName());
			
			return names;
			
		}else if(args.length == 2) {
			
			final List<String> ranksNames = new ArrayList<>();
		
			for(RankList ranks : RankList.values())
				
				ranksNames.add(ranks.getName());
			
			return ranksNames;
			
		}
		
		return super.onTabComplete(sender, cmd, label, args);
	}

}
