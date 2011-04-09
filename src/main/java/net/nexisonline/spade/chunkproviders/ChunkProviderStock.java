package net.nexisonline.spade.chunkproviders;

import net.nexisonline.spade.SpadeChunkProvider;

import org.bukkit.util.config.ConfigurationNode;

public class ChunkProviderStock extends SpadeChunkProvider {
	@Override
	public void onLoad(Object world, long seed) {
		// Everything is stock.
	}

	@Override
	public ConfigurationNode configure(ConfigurationNode node) {
		return node;
		// TODO Auto-generated method stub
		
	}
}
