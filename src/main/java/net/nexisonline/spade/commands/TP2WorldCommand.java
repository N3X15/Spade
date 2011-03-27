package net.nexisonline.spade.commands;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TP2WorldCommand implements CommandExecutor {
	SpadePlugin spade;
	public TP2WorldCommand(SpadePlugin spade) {
		this.spade=spade;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length==0)
			return false;
		Player p = (Player)sender;
		World w = spade.getServer().getWorld(args[0]);
		if(w==null) {
			p.sendMessage("You're a derp.");
			return false;
		}
		return p.teleport(w.getSpawnLocation());
	}

}
