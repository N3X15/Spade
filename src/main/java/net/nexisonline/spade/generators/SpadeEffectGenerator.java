package net.nexisonline.spade.generators;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeEffectGenerator {
	protected World world;
	protected SpadePlugin plugin;
	protected ConfigurationNode config;
	protected long seed;

	public SpadeEffectGenerator(SpadePlugin plugin,World w,ConfigurationNode node, long seed) {
		this.plugin=plugin;
		this.world=w;
		this.config=node;
		this.seed=seed;
	}

	public abstract void addToChunk(Chunk chunk, int x, int z);
}
