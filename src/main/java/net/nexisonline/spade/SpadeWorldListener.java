package net.nexisonline.spade;

import java.io.File;
import java.util.List;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.util.config.Configuration;


public class SpadeWorldListener extends WorldListener {
	private SpadePlugin spade;
	private List<String> worlds;

	public SpadeWorldListener(SpadePlugin plugin) {
		spade=plugin;
	}

	public void loadWorlds() {		
		File f = new File("plugins/Spade/Spade.yml");
		Configuration cfg = new Configuration(f);
		
		worlds = cfg.getKeys("worlds");
		if(worlds!=null)
		{
			for(String worldName : worlds) {
				spade.loadWorld(worldName,cfg.getString("worlds."+worldName+".chunk-manager"), cfg.getString("worlds."+worldName+".chunk-provider"));
			}
		} else {
			for(World w : spade.getServer().getWorlds())
			{
				String worldName = w.getName();
				cfg.setProperty("worlds."+worldName+".chunk-manager", "stock");
				cfg.setProperty("worlds."+worldName+".chunk-provider", "stock");
			}
			cfg.save();
		}
			
	}
	
	public void onWorldLoad() {
		
	}

}
