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

	public SpadeEffectGenerator(SpadePlugin plugin,World w,ConfigurationNode node, long seed) {
		this.plugin=plugin;
		this.world=w;
		this.config=node;
		this.seed=seed;
	}
}
