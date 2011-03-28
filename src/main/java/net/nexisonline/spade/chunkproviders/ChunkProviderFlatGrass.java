package net.nexisonline.spade.chunkproviders;

import java.util.Random;

import net.minecraft.server.NoiseGeneratorOctaves;

import org.bukkit.ChunkProvider;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class ChunkProviderFlatGrass extends ChunkProvider {
	
	private Random random;
	//private World world;
	private NoiseGeneratorOctaves hillnoise;

	public void onLoad(World world, long seed) {
		//this.world=world;
		this.random = new Random(seed);
		this.hillnoise = new net.minecraft.server.NoiseGeneratorOctaves(random, 4);
	}

	@Override
	public void generateChunk(World world, int X, int Z, byte[] abyte, Biome[] biomes,
			double[] temperature) {
		for(int x = 0;x<16;x++) {
			for(int z=0;z<16;z++) {
				double h = hillnoise.a((X*16)+x, (Z*16)+z); // Yes we're doing a heightmap
				for(int y=0;y<h+63;y++) {

					abyte[getBlockIndex(x,y,z)]=(byte) ((y<2) ? Material.BEDROCK.getId() : 1);
				}
			}
		}
		
	}
	@Override
	public void populateChunk(World world, int x, int z, byte[] abyte, Biome[] biomes) {
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
	public void generateCaves(World world, int x, int z, byte[] abyte) {
		// TODO Auto-generated method stub
		
	}

}
