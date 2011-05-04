package net.nexisonline.spade;

import java.util.ArrayList;

import net.nexisonline.spade.generators.SpadeEffectGenerator;

import org.bukkit.Chunk;
import org.bukkit.util.config.ConfigurationNode;

public class PopulationManager {
	boolean populate=true;
	ArrayList<SpadeEffectGenerator> populators = new ArrayList<SpadeEffectGenerator>();
	
	public PopulationManager(ConfigurationNode cfg) {
		populate=cfg.getBoolean("populate", true);
		
		for(Object o : cfg.getList("populators")) {
			if(o instanceof ConfigurationNode) {
				
			}
		}
	}
    public void PopulatorManager(byte[] blocks, long X, long Z)
    {
        
    }

}
