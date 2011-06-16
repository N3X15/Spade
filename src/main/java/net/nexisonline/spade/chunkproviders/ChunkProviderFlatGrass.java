package net.nexisonline.spade.chunkproviders;

import java.util.Random;
import java.util.logging.Logger;

import net.nexisonline.spade.SpadeChunkProvider;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

public class ChunkProviderFlatGrass extends SpadeChunkProvider {
	private byte[] template;
	
	public void onLoad(Object world, long seed) {
		template=new byte[16*128*16];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double h = 64;
				for (int y = 0; y < h; y++) {
					setBlockByte(template,x,y,z,(byte) ((y < 2) ? 7 : 1));
				}
			}
		}
	}

	@Override
	public byte[] generate(World world, Random random, int X, int Z) {
		return template;
		
		//Logger.getLogger("Minecraft").info(String.format("[Flatgrass] Chunk (%d,%d)", X, Z));
	}

}
