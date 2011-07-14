package net.nexisonline.spade.populators;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeEffectGenerator extends BlockPopulator {
	protected World world;
	protected SpadePlugin plugin;
	protected ConfigurationNode config;
	protected long seed;

	public SpadeEffectGenerator(SpadePlugin plugin,ConfigurationNode node, long seed) {
		this.plugin=plugin;
		this.config=node;
		this.seed=seed;
	}

	protected void ensureChunkIsLoaded(int x, int z) {
		if(!world.isChunkLoaded(x, z))
			world.loadChunk(x, z);
	}
	
	public static SpadeEffectGenerator getInstance(SpadePlugin plugin, ConfigurationNode node, long seed) { return null; }

	public abstract ConfigurationNode getConfiguration();
}
