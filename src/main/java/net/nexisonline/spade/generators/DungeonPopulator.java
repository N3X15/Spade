package net.nexisonline.spade.generators;

import java.util.Random;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.ConfigurationNode;

import toxi.math.noise.SimplexNoise;

public class DungeonPopulator extends SpadeEffectGenerator
{
	public DungeonPopulator(SpadePlugin plugin, World w,
			ConfigurationNode node, long seed) {
		super(plugin, w, node, seed);
		init(seed,16);
	}

	public void init(long seed, int maxRooms)
	{
		m_density = new SimplexNoise(seed + 343543);
		m_density.setFrequency(0.01);
		m_density.setAmplitude(maxRooms * 0.1);

		m_random = new Random(seed + 3531244);
	}

	public void populate(World w, Chunk chunk)
	{
		world=w;
		double density = m_density.noise(chunk.getX() * 16, chunk.getZ() * 16);
		//Logger.getLogger("Minecraft").info(String.format("Density: %.2f", density));

		int chunkY = m_random.nextInt(64);

		if (density > 0.8)
		{
			int roomCount = (int)(density * 10);

			for (int i = 0; i < roomCount; i++)
			{
				int x = m_random.nextInt(16);
				int y = chunkY + m_random.nextInt(2);
				int z = m_random.nextInt(16);

				int width = m_random.nextInt(16) + 1;
				int height = m_random.nextInt(8) + 2;
				int depth = m_random.nextInt(16) + 1;

				x += chunk.getX() * 16;
				z += chunk.getZ() * 16;

				//Logger.getLogger("Minecraft").info(String.format("Width: %d Height: %d Depth: %d", width, height, depth));

				generateRoom(x, y, z, width, height, depth, chunk);
			}
		}		
	}

	private void generateRoom(int px, int py, int pz, int width, int height, int depth, Chunk chunk)
	{
		try
		{

			int nx = px - 1;
			int ny = py - 1;
			int nz = pz - 1;

			int nw = width + 1;
			int nh = height + 1;
			int nd = depth + 1;

			for (int x = px; x < px + width; x++)
			{
				for (int y = py; y < py + height; y++)
				{			
					for (int z = pz; z < pz + depth; z++)
					{
						if (y == py)
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

	Random m_random;
	SimplexNoise m_density;
	@Override
	public void addToChunk(Chunk chunk, int x, int z) {
		populate(chunk.getWorld(),chunk);
	}
}