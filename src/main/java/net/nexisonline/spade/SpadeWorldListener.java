package net.nexisonline.spade;

import java.io.File;
import java.util.List;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.util.config.Configuration;


public class SpadeWorldListener extends WorldListener {
	private SpadePlugin spade;
	private List<String> worlds;

	public SpadeWorldListener(SpadePlugin plugin) {
		spade=plugin;
	}
	
	@Override
	public void onWorldLoad(WorldLoadEvent e) {
		
	}

	public void loadWorlds() {		
		File f = new File("plugins/Spade/Spade.yml");
		Configuration cfg = new Configuration(f);
		if(f.exists())
			cfg.load();
		worlds = cfg.getKeys("worlds");
		if(worlds!=null)
		{
			for(String worldName : worlds) {
				spade.genLimits.put(worldName, new GenerationLimits(cfg.getNode("worlds."+worldName+".limits")));
				spade.loadWorld(worldName,
						cfg.getString("worlds."+worldName+".chunk-manager.name"), 
						cfg.getString("worlds."+worldName+".chunk-provider.name"), 
						cfg.getNode("worlds."+worldName+".chunk-provider.config"));
			}
		} else {
			for(World w : spade.getServer().getWorlds())
			{
				String worldName = w.getName();
				cfg.setProperty("worlds."+worldName+".chunk-manager.name", "stock");
				cfg.setProperty("worlds."+worldName+".chunk-manager.config", null);
				cfg.setProperty("worlds."+worldName+".chunk-provider.name", "stock");
				cfg.setProperty("worlds."+worldName+".chunk-provider.config", null);
				cfg.setProperty("worlds."+worldName+".limits",(new GenerationLimits()).getConfig());
			}
			cfg.save();
		}
			
	}

}
