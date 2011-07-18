package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.util.config.ConfigurationNode;

public class ChunkProviderFlatGrass extends SpadeChunkProvider {
	public ChunkProviderFlatGrass(SpadePlugin plugin) {
		super(plugin);
	}
	
	private byte[] template;
	
	@Override
    public void onLoad(String worldName, long worldSeed, Map<String, Object> map) {
        super.onLoad(worldName,worldSeed,map);
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
