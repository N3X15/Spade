package net.nexisonline.spade;

import java.util.ArrayList;
import java.util.List;

import net.nexisonline.spade.populators.SpadeEffectGenerator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationManager {
	boolean populate=true;
	List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
	@SuppressWarnings("unchecked")
	public GenerationManager(String world, ConfigurationNode cfg) {
		// World population
		populate=cfg.getBoolean("populate", true);
		for(Object o : cfg.getList("populators")) {
			if(o instanceof ConfigurationNode) {
				String populatorName = ((ConfigurationNode) o).getString("name");
				Class<? extends SpadeEffectGenerator> c;
				try {
					c = (Class<? extends SpadeEffectGenerator>) Class.forName(populatorName);
					SpadeEffectGenerator seg = c.getConstructor(ConfigurationNode.class).newInstance(o);
					populators.add(seg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public List<BlockPopulator> getPopulators() {
		return populators;
	}
}
