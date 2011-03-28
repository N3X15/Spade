package net.nexisonline.spade.chunkproviders;

import org.bukkit.ChunkProvider;
import org.bukkit.World;

public class ChunkProviderStock extends ChunkProvider {
	@Override
	public void onLoad(World world, long seed) {
		// Everything is stock.
	}
}
