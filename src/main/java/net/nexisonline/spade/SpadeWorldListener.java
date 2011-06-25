package net.nexisonline.spade;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;


public class SpadeWorldListener extends WorldListener {
	private SpadePlugin spade;
	private List<String> worlds;

	public SpadeWorldListener(SpadePlugin plugin) {
		spade=plugin;
	}
	
	@Override
	public void onWorldLoad(WorldLoadEvent e) {}
	
	@Override
	public void onWorldSave(WorldSaveEvent e) {
		saveWorlds();
	}

	public void loadWorlds() {		
		File f = new File(spade.getDataFolder(),"Spade.yml");
		Configuration cfg = new Configuration(f);
		if(f.exists())
			cfg.load();
		worlds = cfg.getKeys("worlds");
		if(worlds!=null)
		{
			for(String worldName : worlds) {
				spade.genLimits.put(worldName.toLowerCase(), new GenerationLimits(cfg.getNode("worlds."+worldName+".limits")));
				spade.loadWorld(worldName,
						cfg.getInt("worlds."+worldName+".seed", (int) (new Random()).nextLong()),
						cfg.getString("worlds."+worldName+".chunk-manager.name","stock"), 
						cfg.getString("worlds."+worldName+".chunk-provider.name","stock"), 
						cfg.getNode("worlds."+worldName+".chunk-provider.config"));
			}
		} else {
			for(World w : spade.getServer().getWorlds())
			{
				String worldName = w.getName();
				cfg.setProperty("worlds."+worldName+".chunk-provider.name", "stock");
				cfg.setProperty("worlds."+worldName+".chunk-provider.config", null);
				cfg.setProperty("worlds."+worldName+".limits",(new GenerationLimits()).getConfig());
			}
			cfg.save();
		}
	}

	public void saveWorlds() {
		
		File f = new File(spade.getDataFolder(),"Spade.yml");
		Configuration cfg = new Configuration(f);
		if(f.exists())
			cfg.load();
		cfg.setHeader("# Spade Configuration File","# Automatically generated","#","# DO NOT USE TABS");
		ConfigurationNode node = Configuration.getEmptyNode();
		cfg.setProperty("worlds", node);
		for(World w : spade.getServer().getWorlds()) {
			cfg.setProperty("worlds."+w.getName()+".chunk-provider", Configuration.getEmptyNode());
			if(w.getGenerator() instanceof SpadeChunkProvider) {
				SpadeChunkProvider cp = (SpadeChunkProvider)w.getGenerator();
				cfg.setProperty("worlds."+w.getName()+".chunk-provider.name", spade.getNameForClass(cp));
				cfg.setProperty("worlds."+w.getName()+".chunk-provider.config", cp.getConfig());
			} else {
				cfg.setProperty("worlds."+w.getName()+".chunk-provider.name", "stock");
			}
		}
	}
}
