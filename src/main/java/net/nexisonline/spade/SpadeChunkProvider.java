package net.nexisonline.spade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeChunkProvider extends ChunkGenerator {

	protected String worldName="";
	private GenerationManager popmanager=null;

	/**
	 * Load world settings and configure chunk generator
	 * @param worldSeed All we know about the new world presently
	 * @param node Configuration node for this plugin.
	 */
	public void onLoad(String worldName, long worldSeed, ConfigurationNode node) {
		this.popmanager = new GenerationManager(worldName,node);
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World w) {
		if(popmanager==null) 
			return new ArrayList<BlockPopulator>();
		return popmanager.getPopulators();
	}

	@Override
	public boolean canSpawn(World w, int x, int z) {
		int y = w.getHighestBlockYAt(x, z);
		return w.getBlockAt(x, y, z).getType().isBlock() && w.getBlockAt(x,y+1,z).equals(Material.AIR) && w.getBlockAt(x,y+2,z).equals(Material.AIR);
	}

	protected void setBlockByte(byte[] blocks, int x, int y, int z, byte block) {
		blocks[y + (z * 128 + (x * 128 * 16))] = block;
	}
	
}
