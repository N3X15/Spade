/**
 * 
 */
package net.nexisonline.spade.commands;

import net.minecraft.server.WorldServer;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
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
			WorldServer W = ((CraftWorld)w).getHandle();
			int cx = p.getLocation().getBlockX()>>4;
			int cz = p.getLocation().getBlockZ()>>4;
			net.minecraft.server.Chunk oc = W.worldProvider.c().getOrCreateChunk(cx,cz);
			for(int x=0;x<16;++x) {
				for(int z=0;z<16;++z) {
					for(int y=0;y<128;++y) {
						int type = oc.getTypeId(x, y, z);
						byte data = (byte) oc.getData(x, y, z);
						W.setTypeIdAndData(x+(cx<<4),y,z+(cz<<4),type, data);
					}
				}
			}
			return true;
		}
		return false;
	}
}