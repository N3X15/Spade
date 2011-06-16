package net.nexisonline.spade;

import java.util.ArrayList;
import java.util.List;

import net.nexisonline.spade.populators.SpadeEffectGenerator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationManager {
	boolean populate=true;
	List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
	@SuppressWarnings("unchecked")
	public GenerationManager(String world, ConfigurationNode cfg) {
		if(cfg.getProperty("populators")==null)
			cfg.setProperty("populators", getDefaultPopulators());
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

	private List<ConfigurationNode> getDefaultPopulators() {
		List<ConfigurationNode> nodes = new ArrayList<ConfigurationNode>();
		ConfigurationNode currentNode = Configuration.getEmptyNode();
		nodes.add(currentNode);
		return nodes;
	}

	public List<BlockPopulator> getPopulators() {
		return populators;
	}
}
