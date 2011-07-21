package net.nexisonline.spade;

@Deprecated
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
        for (int x = 0; x < 16; x += 3) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z += 3) {
                    if (x == 0 && z > 0) {
                        abyte[dmi(x, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.25f);
                        abyte[dmi(x, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.85f);
                    } else if (x > 0 && z > 0) {
                        abyte[dmi(x - 1, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.25f);
                        abyte[dmi(x - 2, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.85f);
                        
                        abyte[dmi(x, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.25f);
                        abyte[dmi(x - 1, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.25f);
                        abyte[dmi(x - 2, y, z - 1)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.85f);
                        
                        abyte[dmi(x, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x, y, z - 3)], 0.85f);
                        abyte[dmi(x - 1, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.25f);
                        abyte[dmi(x - 2, y, z - 2)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z - 3)], 0.85f);
                    } else if (x > 0 && z == 0) {
                        abyte[dmi(x - 1, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.25f);
                        abyte[dmi(x - 2, y, z)] = lerp(abyte[dmi(x, y, z)], abyte[dmi(x - 3, y, z)], 0.85f);
                    }
                }
            }
        }
        return abyte;
    }
    
    /**
     * From Oddlabs' Procedurality
     * @param hm
     * @return
     */
    public static Heightmap LinearExpandHeightmap(Heightmap hm, int newheight,
            int newwidth) {
        double x_coord = 0;
        double z_coord = 0;
        double val1 = 0;
        double val2 = 0;
        Heightmap newhm = new Heightmap(newheight,newwidth);
        double height_ratio = (double) hm.height / newheight;
        double width_ratio = (double) hm.width / newwidth;
        for (int y = 0; y < newheight; y++) {
            z_coord = y * height_ratio - 0.5f;
            int y_coord_lo = (int) z_coord;
            int y_coord_hi = y_coord_lo + 1;
            for (int x = 0; x < newwidth; x++) {
                x_coord = x * width_ratio - 0.5f;
                int x_coord_lo = (int) x_coord;
                int x_coord_hi = x_coord_lo + 1;
                float x_diff = (float) (x_coord - x_coord_lo);
                val1 = lerp(hm.get(x_coord_lo, y_coord_lo), hm.get(x_coord_hi, y_coord_lo), x_diff);
                val2 = lerp(hm.get(x_coord_lo, y_coord_hi), hm.get(x_coord_hi, y_coord_hi), x_diff);
                newhm.set(x, y, Math.max(Math.min(lerp(val1, val2, (float) (z_coord - y_coord_lo)), 1f), 0f));
            }
        }
        return newhm;
    }
    
    /**
     * Adapted from Oddlabs' Procedurality
     * @param hm
     * @return
     */
    public static Densitymap _DERPLinearExpandDensitymap(Densitymap hm,
            int newheight, int newdepth, int newwidth) {
        Densitymap ndm = new Densitymap(newheight, newdepth, newwidth);
        double x_coord = 0;
        double y_coord = 0;
        double z_coord = 0;
        double nl = 0;
        double sl = 0;
        double nu = 0;
        double su = 0;
        double l = 0;
        double u = 0;
        double height_ratio = (double) hm.height / newheight;
        double width_ratio = (double) hm.width / newwidth;
        double depth_ratio = (double) hm.depth / newdepth;
        for (int z = 0; z < newheight; z++) {
            z_coord = z * height_ratio - 0.5f;
            int z_coord_lo = (int) z_coord;
            int z_coord_hi = z_coord_lo + 1;
            for (int x = 0; x < newwidth; x++) {
                x_coord = x * width_ratio - 0.5f;
                for (int y = 0; y < newdepth; y++) {
                    y_coord = y * depth_ratio - 0.5f;
                    int x_coord_lo = (int) x_coord;
                    int x_coord_hi = x_coord_lo + 1;
                    float x_diff = (float) (x_coord - x_coord_lo);
                    float z_diff = (float) (z_coord - z_coord_lo);
                    int y_coord_lo = (int) y_coord;
                    int y_coord_hi = y_coord_lo + 1;
                    float y_diff = (float) (y_coord - y_coord_lo);
                    // north, lower
                    nl = lerp(hm.get(x_coord_lo, y_coord_lo, z_coord_lo), hm.get(x_coord_hi, y_coord_lo, z_coord_lo), x_diff);
                    // south, lower
                    sl = lerp(hm.get(x_coord_lo, y_coord_lo, z_coord_hi), hm.get(x_coord_hi, y_coord_lo, z_coord_hi), x_diff);
                    l = lerp(nl, sl, z_diff);
                    // north, upper
                    nu = lerp(hm.get(x_coord_lo, y_coord_hi, z_coord_lo), hm.get(x_coord_hi, y_coord_hi, z_coord_lo), x_diff);
                    // south, upper
                    su = lerp(hm.get(x_coord_lo, y_coord_hi, z_coord_hi), hm.get(x_coord_hi, y_coord_hi, z_coord_hi), x_diff);
                    u = lerp(nu, su, z_diff);
                    
                    ndm.set(x, y, z, Math.max(Math.min(lerp(l, u, y_diff), 1f), 0f));
                }
            }
        }
        return ndm;
    }
    
    public static Densitymap LinearExpandDensitymap(Densitymap hm,
            int newheight, int newdepth, int newwidth) {
        int ixl, ixh, iyl, iyh, izl, izh;
        double na, nb, nc, nd, ne, nf, ng, nh;
        double la, lb, lc, ld, le, lf;
        Densitymap ndm = new Densitymap(newheight, newdepth, newwidth);
        for (int z = 0; z < newheight; z++) {
            for (int x = 0; x < newdepth; x++) {
                for (int y = 0; y < newwidth; y++) {
                    float i = (float) (((float) x) * 1 / 0.25);
                    float j = (float) ((y) / 0.25 * 1);
                    float k = (float) (((float) z) * 1 / 0.25);
                    
                    ixl = (x + 0) % 4;
                    ixh = (x + 1) % 4;
                    iyl = (y + 0) % 32;
                    iyh = (y + 1) % 32;
                    izl = (z + 0) % 4;
                    izh = (z + 1) % 4;
                    
                    na = hm.get(ixl, iyl, izl);
                    nb = hm.get(ixh, iyl, izl);
                    nc = hm.get(ixl, iyh, izl);
                    nd = hm.get(ixh, iyh, izl);
                    ne = hm.get(ixl, iyl, izh);
                    nf = hm.get(ixh, iyl, izh);
                    ng = hm.get(ixl, iyh, izh);
                    nh = hm.get(ixh, iyh, izh);
                    
                    la = lerp(na, nb, i - (int) i);
                    lb = lerp(nc, nd, i - (int) i);
                    lc = lerp(ne, nf, j - (int) j);
                    ld = lerp(ng, nh, j - (int) j);
                    le = lerp(la, lb, k - (int) k);
                    lf = lerp(lc, ld, k - (int) k);
                    
                    ndm.set(x, y, z, lerp(le, lf, i - (int) k));
                }
            }
        }
        return hm;
    }
    
    /**
     * Linear Interpolator
     * @author PrettyPony <prettypony@7chan.org>
     * @param hm 16x16 grid of height values.
     */
    public static Heightmap OLD_LinearExpandHeightmap(Heightmap hm) {
        // Generate the xz plane of blocks by interpolation
        for (int x = 0; x < 16; x += 3) {
            for (int z = 0; z < 16; z += 3) {
                if (x == 0 && z > 0) {
                    hm.set(x, z - 1, lerp(hm.get(x, z), hm.get(x, z - 3), 0.25f));
                    hm.set(x, z - 2, lerp(hm.get(x, z), hm.get(x, z - 3), 0.85f));
                } else if (x > 0 && z > 0) {
                    hm.set(x - 1, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.25f));
                    hm.set(x - 2, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.85f));
                    
                    hm.set(x, z - 1, lerp(hm.get(x, z), hm.get(x, z - 3), 0.25f));
                    hm.set(x - 1, z - 1, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.25f));
                    hm.set(x - 2, z - 1, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.85f));
                    
                    hm.set(x, z - 2, lerp(hm.get(x, z), hm.get(x, z - 3), 0.85f));
                    hm.set(x - 1, z - 2, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.25f));
                    hm.set(x - 2, z - 2, lerp(hm.get(x, z), hm.get(x - 3, z - 3), 0.85f));
                } else if (x > 0 && z == 0) {
                    hm.set(x - 1, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.25f));
                    hm.set(x - 2, z, lerp(hm.get(x, z), hm.get(x - 3, z), 0.85f));
                }
            }
        }
        return hm;
    }
    
    private static double lerp(double a, double b, float f) {
        return (a * (1 - f) + b * f);
    }
}
