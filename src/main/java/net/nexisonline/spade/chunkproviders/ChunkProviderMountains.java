package net.nexisonline.spade.chunkproviders;

import org.bukkit.ChunkProvider;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class ChunkProviderMountains extends ChunkProvider {
	@Override
	public boolean hasCustomTerrainGenerator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCustomPopulator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCustomCaves() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLoad(World world, long seed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateChunk(int x, int z, byte[] abyte, Biome[] biomes,
			double[] temperature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateChunk(int x, int z, byte[] abyte, Biome[] biomes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateCaves(Object parent, int x, int z, byte[] abyte) {
		// TODO Auto-generated method stub
		
	}

}
