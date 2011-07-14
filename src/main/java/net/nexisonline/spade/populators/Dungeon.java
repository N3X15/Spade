package net.nexisonline.spade.populators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.ChunkCoordinates;
import net.nexisonline.spade.SpadeLogging;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;

public class Dungeon {
	public Map<ChunkCoordinates,Boolean> neededChunks = new HashMap<ChunkCoordinates,Boolean>();
	private int dx;
	private int dy;
	private int dz;
	private int width;
	private int height;
	private int depth;
	private World world;
	private Random m_random;

	public Dungeon(World world, int x_, int y_, int z_, int width, int height, int depth) {
		this.dx=x_;
		this.dy=y_;
		this.dz=z_;
		this.width=width;
		this.height=height;
		this.depth=depth;
		this.world=world;
		m_random=new Random(world.getSeed());
		for (int _x = x_; _x < x_ + width; _x++)
		{
			for (int _y = y_; _y < y_ + height; _y++)
			{			
				for (int _z = z_; _z < z_ + depth; _z++)
				{
					ChunkCoordinates cc = new ChunkCoordinates();
					cc.x = (x_>>4);
					cc.z = (z_>>4);
					if(!neededChunks.containsKey(cc)) {
						neededChunks.put(cc,world.loadChunk(cc.x, cc.z, false));
					}
				}
			}
		}
		checkChunkStatus();
	}

	public void onChunkLoaded(int cx, int cz) {
		ChunkCoordinates cc = new ChunkCoordinates();
		cc.x=cx;
		cc.z=cz;
		neededChunks.put(cc, true);
		
		checkChunkStatus();
	}
	
	public void checkChunkStatus() {
		// Ensure all are true prior to proceeding
		for(ChunkCoordinates cc : neededChunks.keySet()) {
			if(!neededChunks.get(cc)) {
				if(world.loadChunk(cc.x, cc.z, false))
					continue;
				return;
			}
		}
		SpadeLogging.info("All "+neededChunks.size()+" chunks have been loaded, generating dungeon!");
		try
		{

			int nx = dx - 1;
			int ny = dy - 1;
			int nz = dz - 1;

			int nw = width + 1;
			int nh = height + 1;
			int nd = depth + 1;

			for (int x = dx; x < dx + width; x++)
			{
				for (int y = dy; y < dy + height; y++)
				{			
					for (int z = dz; z < dz + depth; z++)
					{
						if (y == dy)
						{
							int placeSpawner = m_random.nextInt((width * height) * 2);
							int placeChest = m_random.nextInt((width * height) * 4);

							int currentBlock = world.getBlockAt(x, y, z).getTypeId();
							
							if (placeSpawner == 0 && currentBlock != 0 && canPlaceBlock(x, y, z))
							{
								Block spawner = world.getBlockAt(x, y, z);
								spawner.setType(Material.MOB_SPAWNER);
								((CreatureSpawner)spawner.getState()).setCreatureTypeId(getRandomMob());
							}
							else if (placeChest == 0 && currentBlock != 0 && canPlaceBlock(x,y,z))
							{
								Block block = world.getBlockAt(x, y, z);
								block.setType(Material.CHEST);
								Chest chest = (Chest)block.getState();


								for (int i = 0; i < 4; i++)
								{
									ItemStack st = getArmor(i);

									if (st != null)
									{
										chest.getInventory().setItem(m_random.nextInt(chest.getInventory().getSize()), st);
									}
								}

								for (int i = 0; i < 5; i++)
								{
									ItemStack st = getTools(i);

									if (st != null)
									{
										chest.getInventory().setItem(m_random.nextInt(chest.getInventory().getSize()), st);
									}
								}

								ItemStack st = getOre();

								if (st != null)
								{
									chest.getInventory().setItem(m_random.nextInt(chest.getInventory().getSize()), st);
								}

								ItemStack st2 = getItem();

								if (st2 != null)
								{
									//tileEntity.setInventorySlotContents(m_random.nextInt(tileEntity.getSizeInventory()), st2);
								}
							}								
							else
							{
								placeBlock(x,y,z,Material.AIR);
							}
						}
						else
						{
							placeBlock(x,y,z,Material.AIR);
						}
					}
				}
			}

			for (int x = nx; x <= nx + nw; x++)
			{
				for (int z = nz; z <= nz + nd; z++)
				{
					placeBlock(x, ny, z,Material.MOSSY_COBBLESTONE);
					placeBlock(x, ny + nh, z,Material.MOSSY_COBBLESTONE);
				}
			}

			for (int y = ny; y <= ny + nh; y++)
			{
				for (int z = nz; z <= nz + nd; z++)
				{
					placeBlock(nx, y, z,Material.MOSSY_COBBLESTONE);
					placeBlock(nx + nw, y, z,Material.MOSSY_COBBLESTONE);
				}
			}

			for (int x = nx; x <= nx + nw; x++)
			{
				for (int y = ny; y <= ny + nh; y++)
				{
					placeBlock(x, y, nz,Material.MOSSY_COBBLESTONE);
					placeBlock(x, y, nz + nd,Material.MOSSY_COBBLESTONE);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	



	private String getRandomMob()
	{
		int i = m_random.nextInt(4);

		if (i == 0)
		{
			return "Skeleton";
		}
		else if (i == 1)
		{
			return "Zombie";
		}
		else if (i == 2)
		{
			return "Creeper";
		}
		else if (i == 3)
		{
			return "Spider";
		}
		else
		{
			return "";
		}
	}

	private ItemStack getOre()
	{
		int i = m_random.nextInt(255);
		int count = m_random.nextInt(63) + 1;

		if (i > 253)
		{
			return new ItemStack(22, count);
		}
		else if (i > 230)
		{
			return new ItemStack(56, count);
		}
		else if (i > 190)
		{
			return new ItemStack(14, count);
		}
		else if (i > 150)
		{
			return new ItemStack(15, count);
		}
		else
		{
			return new ItemStack(263, count);
		}
	}

	private ItemStack getTools(int index)
	{
		int i = m_random.nextInt(255);

		if (i > 245)
		{
			return new ItemStack(276 + index, 1);
		}
		else if (i > 230)
		{
			return new ItemStack(283 + index, 1);
		}
		else if (i > 190)
		{
			if (index == 0)
			{
				return new ItemStack(272, 1);
			}
			else
			{
				return new ItemStack(255 + index, 1);
			}
		}
		else if (i > 150)
		{
			return new ItemStack(272 + index, 1);
		}
		else
		{
			return new ItemStack(268 + index, 1);
		}
	}

	private ItemStack getArmor(int index)
	{
		int i = m_random.nextInt(255);

		if (i > 245)
		{			
			return new ItemStack(310 + index, 1);
		}
		else if (i > 230)
		{
			return new ItemStack(302 + index, 1);
		}
		else if (i > 190)
		{
			return new ItemStack(314 + index, 1);
		}
		else if (i > 150)
		{
			return new ItemStack(306 + index, 1);
		}
		else
		{
			return new ItemStack(298 + index, 1);
		}
	}

	private ItemStack getItem()
	{
		int id = m_random.nextInt(255) + 255;		
		int count = m_random.nextInt(63) + 1;		
		return new ItemStack(id, count);
	}

	private boolean canPlaceBlock(int x, int y, int z)
	{
		boolean place = true;
		Material m = world.getBlockAt(x, y, z).getType();
		switch(m) {
		case AIR:
		case MOB_SPAWNER:
		case CHEST:
		case WATER:
		case STATIONARY_WATER:
		case LAVA:
		case STATIONARY_LAVA:
			place=false;
			break;
		}
		return place;
	}
	private void placeBlock(int x, int y, int z, Material mat) {
		if (canPlaceBlock(x,y,z))
		{
			world.getBlockAt(x, y, z).setType(mat);
		}
	}
}
