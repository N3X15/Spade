package net.nexisonline.spade;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationLimits {
	public boolean round=false;
	public int distanceSquared=10000;
	public int distance=100;
	public boolean enabled=false;
	
	public GenerationLimits() { }
		
	public GenerationLimits(ConfigurationNode node) {
		if(node==null)
			node=Configuration.getEmptyNode();
		enabled=node.getBoolean("enabled",false);
		round=node.getBoolean("round", true);
		distance=node.getInt("chunks-from-spawn", 0);
		distanceSquared=(int) Math.pow(distance, 2);
	}

	public ConfigurationNode getConfig() {
		ConfigurationNode node = Configuration.getEmptyNode();
		node.setProperty("enabled", enabled);
		node.setProperty("round", round);
		node.setProperty("chunks-from-spawn",distance);
		return node;
	}
}
