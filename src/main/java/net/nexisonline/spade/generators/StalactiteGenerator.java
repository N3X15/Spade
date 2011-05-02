package net.nexisonline.spade.generators;

import java.util.Random;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.config.ConfigurationNode;

public class StalactiteGenerator extends SpadeEffectGenerator {
	public StalactiteGenerator(SpadePlugin plugin, World w,
			ConfigurationNode node, long seed) {
		super(plugin, w, node, seed);
		rnd=new Random((seed*1024)+15);
	}
	private int X;
	private int Z;
	private Random rnd;
	private Chunk chunk;

	public void addToChunk(Chunk chunk, int x, int z) {
		this.chunk=chunk;
		this.X=x;
		this.Z=z;

		for(int i = 0;i<10;i++) {
			addStalactite(chunk.getWorld(),rnd.nextInt(15),rnd.nextInt(15)); 
		}
	}

	private void addStalactite(World w,int x, int z) {
		for(int y = 1;y<127;y++) {
			if(get(x,y,z)==1 && (get(x,y-1,z)==0 || get(x,y-1,z)==8)) {
				int h=rnd.nextInt(15);
				//for(;!(get(x,y-h,z)==1)&&y-h>1;h++) {}
				y-=h;
				addStalactite(w,x+(X*16), y, x+(Z*16));
			}
		}
	}

	private final double MINISTALACTITE_CHANCE=0.10;
	private void addStalactite(World w,int x, int y, int z) {
		if(w.isChunkLoaded(x>>4, z>>4)) {
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
}
