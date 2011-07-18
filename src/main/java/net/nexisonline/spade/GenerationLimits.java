package net.nexisonline.spade;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationLimits {
	public boolean round=false;
	public int distanceSquared=10000;
	public int distance=100;
	public boolean enabled=false;
	
	public GenerationLimits() { }
		
	public GenerationLimits(Map<String,Object> map) {
		if(map!=null) {
    		enabled=(Boolean) map.get("enabled");
    		round=(Boolean) map.get("round");
    		distance=(Integer) map.get("chunks-from-spawn");
    		distanceSquared=(int) Math.pow(distance, 2);
		}
	}

	public Map<String,Object> getConfig() {
		Map<String,Object> node = new HashMap<String,Object>();
		node.put("enabled", enabled);
		node.put("round", round);
		node.put("chunks-from-spawn",distance);
		return node;
	}
}
