package net.nexisonline.spade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeChunkProvider extends ChunkGenerator {

	protected String worldName="";
	private GenerationManager popmanager=null;
	protected SpadePlugin plugin;

	public SpadeChunkProvider(SpadePlugin plugin) {
		this.plugin=plugin;
	}

	/**
	 * Load world settings and configure chunk generator
	 * @param worldSeed All we know about the new world presently
	 * @param map Configuration node for this plugin.
	 */
	public void onLoad(String worldName, long worldSeed, Map<String, Object> map) {
		this.popmanager = new GenerationManager(this.plugin, worldName,map, worldSeed);
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World w) {
		if(popmanager==null) 
			return new ArrayList<BlockPopulator>();
		return popmanager.getPopulators();
	}

	@Override
	public boolean canSpawn(World w, int x, int z) {
		//return true;
		int y = w.getHighestBlockYAt(x, z);
		return !w.getBlockAt(x, y, z).isLiquid();
		
	}

	protected void setBlockByte(byte[] blocks, int x, int y, int z, byte block) {
		blocks[y + (z * 128 + (x * 128 * 16))] = block;
	}

	public Map<String,Object> getConfig() {
		Map<String,Object> cfg = new HashMap<String,Object>();
		cfg.put("populators",popmanager.getConfig());
		return cfg;
	}
}
