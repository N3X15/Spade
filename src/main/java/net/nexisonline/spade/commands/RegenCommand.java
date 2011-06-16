/**
 * 
 */
package net.nexisonline.spade.commands;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Rob
 *
 */
public class RegenCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private SpadePlugin spade;

	public RegenCommand(SpadePlugin spadePlugin) {
		this.spade=spadePlugin;
	}

	// /regen chunk|world [titan]
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length==0)
			return false;
		
		Player p = (Player)sender;
		if(!p.isOp()) {
			p.kickPlayer("STOP TRYING TO FUCK WITH SHIT YOU DON'T HAVE ACCESS TO");
			return false;
		}
		
		if(args[0].equalsIgnoreCase("chunk")) {
			World w = p.getWorld();
			int cx = p.getLocation().getBlockX()>>4;
			int cz = p.getLocation().getBlockZ()>>4;
			w.regenerateChunk(cx,cz);
			return true;
		}
		return false;
	}
}
