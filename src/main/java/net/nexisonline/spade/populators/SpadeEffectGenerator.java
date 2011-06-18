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

	public abstract ConfigurationNode getConfiguration();
}
