package net.nexisonline.spade.commands;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Location;
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

	// /tpw world
	// /tpw world x y z
	// /tpw world x top z
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length==0)
			return false;
		Player p = (Player)sender;
		World w = spade.getServer().getWorld(args[0]);
		if(w==null) {
			p.sendMessage("That world doesn't exist.  WorldList:");
			String wlist="";
			for(World w2 :spade.getServer().getWorlds()) {
				wlist+=w2.getName()+", ";
			}
			p.sendMessage(wlist.substring(0, wlist.length()-3));
			return false;
		}
		Location loc = w.getSpawnLocation();
		loc.setY(w.getHighestBlockYAt(loc));
		if(args.length==4) {
			double x = Double.valueOf(args[1]);
			double z = Double.valueOf(args[3]);
			double y;
			if(args[2].equalsIgnoreCase("top"))
				y=w.getHighestBlockYAt((int)x, (int)z);
			else
				y=Double.valueOf(args[2]);
			loc.setX(x);
			loc.setY(y);
			loc.setZ(z);
		}
		loc.setY(loc.getX()+2);
		return p.teleport(w.getSpawnLocation());
	}

}
