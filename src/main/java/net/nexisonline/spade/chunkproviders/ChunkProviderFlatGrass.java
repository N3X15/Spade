package net.nexisonline.spade.chunkproviders;

import java.util.Random;

import net.minecraft.server.Block;
import net.minecraft.server.Chunk;
import net.minecraft.server.NoiseGeneratorOctaves;

import org.bukkit.ChunkProvider;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class ChunkProviderFlatGrass extends ChunkProvider {
	
	private Random random;
	private World world;
	private NoiseGeneratorOctaves hillnoise;

	public void onLoad(World world, long seed) {
		this.world=world;
		this.random = new Random(seed);
		this.hillnoise = new net.minecraft.server.NoiseGeneratorOctaves(random, 4);
	}

	@Override
	public void generateChunk(int X, int Z, byte[] abyte, Biome[] biomes,
			double[] temperature) {
		org.bukkit.Chunk c = (org.bukkit.Chunk)(new Chunk((net.minecraft.server.World)this.world, abyte, X, Z));
		for(int x = 0;x<16;x++) {
			for(int z=0;z<16;z++) {
				double h = hillnoise.a((X*16)+x, (Z*16)+z); // Yes we're doing a heightmap
				for(int y=0;y<h+63;y++) {
					c.getBlock(x, y, z).setTypeId(y<2?Block.BEDROCK.id:1);
				}
			}
		}
		
	}
	@Override
	public void populateChunk(int x, int z, byte[] abyte, Biome[] biomes) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasCustomTerrainGenerator() {
		// TODO Auto-generated method stub
		return true;
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
	public void generateCaves(Object parent, int x, int z, byte[] abyte) {
		// TODO Auto-generated method stub

	}

}
