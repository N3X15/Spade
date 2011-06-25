package net.nexisonline.spade.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.WorldGenClay;
import net.minecraft.server.WorldGenDungeons;
import net.minecraft.server.WorldGenFlowers;
import net.minecraft.server.WorldGenLakes;
import net.minecraft.server.WorldGenLightStone1;
import net.minecraft.server.WorldGenLightStone2;
import net.minecraft.server.WorldGenLiquids;
import net.minecraft.server.WorldGenMinable;
import net.minecraft.server.WorldGenPumpkin;
import net.minecraft.server.WorldGenReed;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class OrePopulator extends SpadeEffectGenerator {

	public enum DepositType {
		BLOB, 
		LAKE, 
		NOTCH_DUNGEON, 
		PONY_DUNGEON,
		CLAY, 
		FLOWER, 
		REED, 
		PUMPKIN, 
		CACTUS, 
		LIQUID, 
		LIGHTSTONE_A, 
		LIGHTSTONE_B
	}

	/**
	 * @author Rob
	 *
	 */
	public class DepositDef {

		public DepositType depositType=DepositType.BLOB;
		public int blockType=16;
		public int minHeight=0;
		public int maxHeight=128;
		public int minBlocks=0;
		public int maxBlocks=50;
		public int minDeposits=0;
		public int maxDeposits=15;
		public boolean rare=false;
		public int rarity=0;
		private Chunk chunk;
		private int X;
		private int Z;
		private Random random;
		public List<Biome> validBiomes = new ArrayList<Biome>();

		public DepositDef() {}
		
		/**
		 * Ore definition.
		 * @param depositType Kind of deposit (blob, vein
		 * @param blockType
		 * @param minHeight
		 * @param maxHeight
		 * @param minBlocks
		 * @param maxBlocks
		 * @param minDeposits
		 * @param maxDeposits
		 */
		public DepositDef(DepositType depositType, int blockType, int minHeight, int maxHeight, int minBlocks, int maxBlocks, int minDeposits, int maxDeposits){
			this.depositType=depositType;
			this.blockType=blockType;
			this.minHeight=minHeight;
			this.maxHeight=maxHeight;
			this.minBlocks=minBlocks;
			this.maxBlocks=maxBlocks;
			this.minDeposits=minDeposits;
			this.maxDeposits=maxDeposits;
		}
		
		/**
		 * For defining lakes, other stuff.
		 */
		public DepositDef(DepositType depositType, int blockType, int minHeight, int maxHeight, int rarity){
			this.depositType=depositType;
			this.blockType=blockType;
			this.minHeight=minHeight;
			this.maxHeight=maxHeight;
			this.rarity = rarity;
		}
		
		public void populate(Chunk chunk, int X, int Z, Random random) {
			this.chunk=chunk;
			this.X=X;
			this.Z=Z;
			this.random=random;

			if(rarity>0)
				addByRarity();
			else
				addManyTimes();
		}

		private void addManyTimes() {
			int max = maxDeposits;
			if(minDeposits<maxDeposits)
				max = randInRange(minDeposits,maxDeposits);
			int rarityshift = (rare) ? 8:0;
			for(int i = 0;i<max;i++) {
				int x = X + random.nextInt(16) + rarityshift;
				int y = randInRange(minHeight,Math.min(127, maxHeight));
				int z = Z + random.nextInt(16) + rarityshift;
				addDeposit(x, y, z);
			}
		}

		private int randInRange(int min,int max) {
			return min + (int)(random.nextDouble() * ((max - min) + 1));
		}
		private void addByRarity() {
			int rarityshift = (rare) ? 8:0;
			if (random.nextInt(rarity) == 0) {
				int x = X + random.nextInt(16) + rarityshift;
				int y = randInRange(minHeight,Math.min(127, maxHeight));
				int z = Z + random.nextInt(16) + rarityshift;
				addDeposit(x, y, z);
			}
		}

		private void addDeposit(int x, int y, int z) {
			switch(depositType) {
			case LAKE:
				(new WorldGenLakes(blockType)).a(getMCWorld(), random, x, y, z);
				break;
			case NOTCH_DUNGEON:
				(new WorldGenDungeons()).a(getMCWorld(), random, x, y, z);
				break;
			case PONY_DUNGEON:
				(new DungeonPopulator(plugin, config.getNode("dungeons"), seed)).populate(world, random, chunk);
				break;
			case CLAY:
				(new WorldGenClay(maxBlocks)).a(getMCWorld(), random, x, y, z);
				break;
			case BLOB:
				(new WorldGenMinable(blockType,maxBlocks)).a(getMCWorld(), random, x, y, z);
				break;
			case FLOWER:
				(new WorldGenFlowers(maxBlocks)).a(getMCWorld(), random, x, y, z);
				break;
			case REED:
				(new WorldGenReed()).a(getMCWorld(), random, x, y, z);
				break;
			case PUMPKIN:
				(new WorldGenPumpkin()).a(getMCWorld(), random, x, y, z);
				break;
			case LIQUID:
				(new WorldGenLiquids(blockType)).a(getMCWorld(), random, x, y, z);
				break;
			case LIGHTSTONE_A:
				(new WorldGenLightStone1()).a(getMCWorld(), random, x, y, z);
				break;
			case LIGHTSTONE_B:
				(new WorldGenLightStone2()).a(getMCWorld(), random, x, y, z);
				break;
			}
		}

		private net.minecraft.server.World getMCWorld() {
			return ((CraftWorld)chunk.getWorld()).getHandle();
		}

		public ConfigurationNode toConfigNode() {
			ConfigurationNode cfg = Configuration.getEmptyNode();
			cfg.setProperty("depositType",depositType);
			cfg.setProperty("blockType",blockType);
			cfg.setProperty("minHeight",minHeight);
			cfg.setProperty("maxHeight",maxHeight);
			cfg.setProperty("minBlocks",minBlocks);
			cfg.setProperty("maxBlocks",maxBlocks);
			cfg.setProperty("minDeposits",minDeposits);
			cfg.setProperty("maxDeposits",maxDeposits);
			cfg.setProperty("rare",rare);
			cfg.setProperty("rarity",rarity);
			return cfg;
		}
	}
	private List<DepositDef> oreDefs = new ArrayList<DepositDef>();
	private Random random;
	
	public OrePopulator(SpadePlugin plugin, ConfigurationNode node,long seed) {
		super(plugin, node, seed);
		if(node.getProperty("deposits")!=null) {
			for(ConfigurationNode odn : node.getNodeList("deposits",getDefaults())) {
				DepositDef od = new DepositDef();
				od.depositType = DepositType.valueOf(odn.getString("depositType","blob"));
				od.blockType = odn.getInt("blockType",16);
				od.minHeight = odn.getInt("minHeight", 0);
				od.maxHeight = odn.getInt("maxHeight", 127);
				od.minBlocks = odn.getInt("minBlocks", 1);
				od.maxBlocks = odn.getInt("maxBlocks", 50);
				od.minDeposits = odn.getInt("minDeposits", 0);
				od.maxDeposits = odn.getInt("maxDeposits", 0);
				od.rarity = odn.getInt("rarity", 4);
				oreDefs.add(od);
			}
		}
		random = new Random();
	}
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		// Shamelessly stolen from Notch's code to avoid fucking up the existing sediment.
        random.setSeed(world.getSeed());
        long xseed = random.nextLong() / 2L * 2L + 1L;
        long zseed = random.nextLong() / 2L * 2L + 1L;
        random.setSeed((long) chunk.getX() * xseed + (long) chunk.getZ() * zseed ^ world.getSeed());
        // END Notchcode
        
        for(DepositDef ore : oreDefs) {
        	ore.populate(chunk,chunk.getX(),chunk.getZ(),random);
        }
	}
	
	// Translated from Notchcode.  You fuckers better donate, my fingers hurt.
	private List<ConfigurationNode> getDefaults() {
		List<ConfigurationNode> defs = new ArrayList<ConfigurationNode>();
		DepositDef o;
        // Water lake
        defs.add(new DepositDef(DepositType.LAKE,Material.STATIONARY_WATER.getId(),0,128,4).toConfigNode());
        // Lava lake
        defs.add(new DepositDef(DepositType.LAKE,Material.STATIONARY_LAVA.getId(), 0,128,8).toConfigNode());
        // Dungeons
        defs.add(new DepositDef(DepositType.NOTCH_DUNGEON,Material.AIR.getId(),0,128,0,0,1,1).toConfigNode());
        // Clay
        defs.add(new DepositDef(DepositType.CLAY,Material.CLAY_BRICK.getId(),0,128,6,32,10,10).toConfigNode());
        // Dirt
        defs.add(new DepositDef(DepositType.BLOB,Material.DIRT.getId(),0,128,0,32,20,20).toConfigNode());
        // Gravel
        defs.add(new DepositDef(DepositType.BLOB,Material.GRAVEL.getId(),0,128,0,32,10,10).toConfigNode());
        // Coal Ore
        defs.add(new DepositDef(DepositType.BLOB,Material.COAL_ORE.getId(),0,128,0,16,20,20).toConfigNode());
        // Iron Ore
        defs.add(new DepositDef(DepositType.BLOB,Material.IRON_ORE.getId(),0,64,0,8,20,20).toConfigNode());
        // Gold Ore
        defs.add(new DepositDef(DepositType.BLOB,Material.GOLD_ORE.getId(),0,32,0,8,2,2).toConfigNode());
        // Redstone Ore
        defs.add(new DepositDef(DepositType.BLOB,Material.REDSTONE_ORE.getId(),0,16,0,7,8,8).toConfigNode());
        // Diamond ore
        defs.add(new DepositDef(DepositType.BLOB,Material.DIAMOND_ORE.getId(),0,16,0,7,1,1).toConfigNode());
        // Lapis Ore
        defs.add(new DepositDef(DepositType.BLOB,Material.LAPIS_ORE.getId(),0,32,0,6,1,1).toConfigNode());
        // Flowers
        defs.add(new DepositDef(DepositType.FLOWER,Material.YELLOW_FLOWER.getId(),0,128,0,0,2,2).toConfigNode());
        defs.add(new DepositDef(DepositType.FLOWER,Material.RED_ROSE.getId(),0,128,2).toConfigNode());
        // Shrooms
        defs.add(new DepositDef(DepositType.FLOWER,Material.BROWN_MUSHROOM.getId(),0,128,4).toConfigNode());
        defs.add(new DepositDef(DepositType.FLOWER,Material.RED_MUSHROOM.getId(),0,128,8).toConfigNode());
        // Reeds
        defs.add(new DepositDef(DepositType.REED,Material.SUGAR_CANE_BLOCK.getId(),0,128,0,0,10,10).toConfigNode());
        // Pumpkins
        defs.add(new DepositDef(DepositType.PUMPKIN,Material.PUMPKIN.getId(),0,128,32).toConfigNode());
        // Cactus
        o = new DepositDef(DepositType.CACTUS,Material.CACTUS.getId(),0,128,0,0,10,10);
        o.validBiomes.add(Biome.DESERT);
        defs.add(o.toConfigNode());
        // Liquids (Lava, Water)
        defs.add(new DepositDef(DepositType.LIQUID,Material.WATER.getId(),8,128,0,0,50,50).toConfigNode());
        defs.add(new DepositDef(DepositType.LIQUID,Material.LAVA.getId(),8,120,0,0,20,20).toConfigNode());

		return defs;
	}

	@Override
	public ConfigurationNode getConfiguration() {
		ConfigurationNode node = Configuration.getEmptyNode();
		List<ConfigurationNode> deposits = new ArrayList<ConfigurationNode>();
		for(DepositDef def : oreDefs) {
			deposits.add(def.toConfigNode());
		}
		node.setProperty("deposits", deposits);
		return node;
	}
}