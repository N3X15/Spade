package net.nexisonline.spade;

import org.bukkit.Material;

import org.bukkit.ChunkProvider;
import org.bukkit.World;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeChunkProvider extends ChunkProvider {

	protected String worldName="";

	public void setWorldName(String cpName) {
		this.worldName=cpName;
	}

	public abstract ConfigurationNode configure(ConfigurationNode node);
	
	public boolean canSpawnAt(World w,int x, int z) {
		Material mat = w.getBlockAt(x, w.getHighestBlockYAt(x, z), z).getType();
		return !mat.equals(Material.WATER) && !mat.equals(Material.LAVA) && !mat.equals(Material.BEDROCK);
	}
	
}
