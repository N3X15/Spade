package net.nexisonline.spade;

public class Interpolator {
	/**
	 * Density map index.
	 * @param x
	 * @param y
	 * @param z
	 * @return Index of item in density map
	 */
	private static int dmi(int x, int y, int z) {
		return x << 11 | z << 7 | y;
	}
	/**
	 * Linear Interpolator
	 * @author PrettyPony <prettypony@7chan.org>
	 */
	public static double[] LinearExpand(double[] abyte) {
		// Generate the xy and yz planes of blocks by interpolation
		/*
		for (int x = 0; x < 16; x += 3)
		{
			for (int y = 0; y < 128; y += 3)
			{
				for (int z = 0; z < 16; z += 3)
				{
					if (y != 15)
					{
						abyte[arrayindex(x , y + 1, z)] = lerp(abyte[arrayindex(x, y, z)], abyte[arrayindex(x, y + 3, z)], 0.02f);
						abyte[arrayindex(x, y + 2, z)] = lerp(abyte[arrayindex(x, y, z)], abyte[arrayindex(x, y + 3, z)], 0.98f);
					}
				}
			}
		}
		*/
		// Generate the xz plane of blocks by interpolation
		for (int x = 0; x < 16; x += 3)
		{
			for (int y = 0; y < 128; y++)
			{
				for (int z = 0; z < 16; z += 3)
				{
					if (x == 0 && z > 0)
					{
						abyte[dmi(x, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.25f);
						abyte[dmi(x, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.85f);
					}
					else if (x > 0 && z > 0)
					{
						abyte[dmi(x - 1, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.25f);
						abyte[dmi(x - 2, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.85f);

						abyte[dmi(x, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.25f);
						abyte[dmi(x - 1, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.25f);
						abyte[dmi(x - 2, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.85f);

						abyte[dmi(x, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.85f);
						abyte[dmi(x - 1, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.25f);
						abyte[dmi(x - 2, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.85f);
					}
					else if (x > 0 && z == 0)
					{
						abyte[dmi(x - 1, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.25f);
						abyte[dmi(x - 2, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.85f);
					}
				}
			}
		}
		return abyte;
	}
	/**
	 * Linear Interpolator
	 * @author PrettyPony <prettypony@7chan.org>
	 * @param hm 16x16 grid of height values.
	 */
	public static Heightmap LinearExpandHeightmap(Heightmap hm) {
		// Generate the xz plane of blocks by interpolation
		for (int x = 0; x < 16; x += 3)
		{
			for (int z = 0; z < 16; z += 3)
			{
				if (x == 0 && z > 0)
				{
					hm.set(x, z - 1, lerp(hm.get(x, z), hm.get(x, z - 3), 0.25f));
					hm.set(x, z - 2, lerp(hm.get(x, z), hm.get(x, z - 3), 0.85f));
				}
				else if (x > 0 && z > 0)
				{
					hm.set(x - 1, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.25f));
					hm.set(x - 2, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.85f));

					hm.set(x, z - 1, lerp(hm.get(x, z), hm.get(x, z - 3), 0.25f));
					hm.set(x - 1, z - 1, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.25f));
					hm.set(x - 2, z - 1, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.85f));

					hm.set(x, z - 2, lerp(hm.get(x, z), hm.get(x, z - 3), 0.85f));
					hm.set(x - 1, z - 2, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.25f));
					hm.set(x - 2, z - 2, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.85f));
				}
				else if (x > 0 && z == 0)
				{
					hm.set(x - 1, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.25f));
					hm.set(x - 2, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.85f));
				}
			}
		}
		return hm;
	}
	private static int hmi(int x, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	private static double lerp(double a, double b, float f) {
		return  (a+(b-a)*f);
	}
}
