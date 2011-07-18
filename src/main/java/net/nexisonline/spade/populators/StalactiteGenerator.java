package net.nexisonline.spade.populators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.config.ConfigurationNode;

public class StalactiteGenerator extends SpadeEffectGenerator {
	private int X;
	private int Z;
	private Random rnd;
	private Chunk chunk;
	private int maxStalactitesPerChunk;
	private int maxStalactiteLength;
	
	public StalactiteGenerator(SpadePlugin plugin, ConfigurationNode node, long seed) {
		super(plugin, node, seed);
		
		// Configure
		maxStalactitesPerChunk = node.getInt("max-stalactites-per-chunk", 10);
		maxStalactiteLength = node.getInt("max-stalactite-length", 15);
		
		rnd=new Random((seed*1024)+15);
	}
	

	public static SpadeEffectGenerator getInstance(SpadePlugin plugin, ConfigurationNode node, long seed) {
		return new StalactiteGenerator(plugin,node,seed);
	}

	@Override
    public void populate(World world, Random rand, Chunk chunk) {
		SpadeLogging.info(String.format("Generating stalactites in chunk (%d,%d)",chunk.getX(),chunk.getZ()));
		this.chunk=chunk;
		this.X=chunk.getX();
		this.Z=chunk.getZ();

		for(int i = 0;i<rnd.nextInt(maxStalactitesPerChunk-1)+1;i++) {
			addStalactite(world,rnd.nextInt(15),rnd.nextInt(15)); 
		}
	}

	private void addStalactite(World w,int x, int z) {
		for(int y = 1;y<127;y++) {
			if(get(x,y,z)==1 && (get(x,y-1,z)==0 || get(x,y-1,z)==8)) {
				int h=rnd.nextInt(maxStalactiteLength);
				//for(;!(get(x,y-h,z)==1)&&y-h>1;h++) {}
				y-=h;
				addStalactite(w,x+(X*16), y, x+(Z*16));
			}
		}
	}

	private final double MINISTALACTITE_CHANCE=0.10;
	private void addStalactite(World w,int x, int y, int z) {
		if(!w.isChunkLoaded(x>>4, z>>4)) {
			w.loadChunk(x>>4, z>>4);
		}
		if(y>=w.getHighestBlockYAt(x, z)) return;
		boolean N=false;
		boolean E=false;
		boolean W=false;
		boolean S=false;
		for(;y<128&&w.getBlockAt(x,y,z).getTypeId()==0;y++) {
			w.getBlockAt(x,y,z).setTypeId(1);
			if(rnd.nextDouble()<MINISTALACTITE_CHANCE && !N)
				addStalactite(w,x+1,y,z);
			if(rnd.nextDouble()<MINISTALACTITE_CHANCE && !E)
				addStalactite(w,x,y,z+1);
			if(rnd.nextDouble()<MINISTALACTITE_CHANCE && !W)
				addStalactite(w,x-1,y,z);
			if(rnd.nextDouble()<MINISTALACTITE_CHANCE && !S)
				addStalactite(w,x,y,z-1);
		}
	}

	private int get(int x, int y, int z) {
		return this.chunk.getBlock(x, y, z).getTypeId();
	}

	@Override
    public Map<String, Object> getConfiguration() {
        Map<String,Object> node = new HashMap<String,Object>();
		node.put("max-stalactites-per-chunk", maxStalactitesPerChunk);
		node.put("max-stalactite-length", maxStalactiteLength);
		return node;
	}
}
