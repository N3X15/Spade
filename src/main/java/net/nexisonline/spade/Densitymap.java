package net.nexisonline.spade;

import static net.nexisonline.spade.MathUtils.lerp;

public class Densitymap {
    public double[][][] map;
    public int height, width, depth;
    
    public Densitymap(final int sx, final int sy, final int sz) {
        map = new double[sx][sy][sz];
        height = sz;
        width = sz;
        depth = sy;
    }
    
    public void set(int x, int y, int z, final double h) {
        if ((x < 0) || (x >= width)) {
            x = (width + x) & (width - 1);
        }
        if ((y < 0) || (y >= depth)) {
            y = (depth + y) & (depth - 1);
        }
        if ((z < 0) || (z >= height)) {
            z = (height + z) & (height - 1);
        }
        map[x][y][z] = h;
    }
    
    public double get(int x, int y, int z) {
        if ((x < 0) || (x >= width)) {
            x = (width + x) & (width - 1);
        }
        if ((y < 0) || (y >= depth)) {
            y = (depth + y) & (depth - 1);
        }
        if ((z < 0) || (z >= height)) {
            z = (height + z) & (height - 1);
        }
        return map[x][y][z];
    }
    
    public void interpolate(int x, int y, int z, final double value) {
        if ((x < 0) || (x >= width)) {
            x = (width + x) & (width - 1);
        }
        if ((y < 0) || (y >= depth)) {
            y = (depth + y) & (depth - 1);
        }
        if ((z < 0) || (z >= height)) {
            z = (height + z) & (height - 1);
        }
        try {
            map[x][y][z] = value;
            
            if (y > 0) {
                map[x][y - 1][z] = lerp(map[x][y][z], map[x][y - 16][z], it(1));
                map[x][y - 2][z] = lerp(map[x][y][z], map[x][y - 16][z], it(2));
                map[x][y - 3][z] = lerp(map[x][y][z], map[x][y - 16][z], it(3));
                map[x][y - 4][z] = lerp(map[x][y][z], map[x][y - 16][z], it(4));
                map[x][y - 5][z] = lerp(map[x][y][z], map[x][y - 16][z], it(5));
                map[x][y - 6][z] = lerp(map[x][y][z], map[x][y - 16][z], it(6));
                map[x][y - 7][z] = lerp(map[x][y][z], map[x][y - 16][z], it(7));
                map[x][y - 8][z] = lerp(map[x][y][z], map[x][y - 16][z], it(8));
                map[x][y - 9][z] = lerp(map[x][y][z], map[x][y - 16][z], it(9));
                map[x][y - 10][z] = lerp(map[x][y][z], map[x][y - 16][z], it(10));
                map[x][y - 11][z] = lerp(map[x][y][z], map[x][y - 16][z], it(11));
                map[x][y - 12][z] = lerp(map[x][y][z], map[x][y - 16][z], it(12));
                map[x][y - 13][z] = lerp(map[x][y][z], map[x][y - 16][z], it(13));
                map[x][y - 14][z] = lerp(map[x][y][z], map[x][y - 16][z], it(14));
                map[x][y - 15][z] = lerp(map[x][y][z], map[x][y - 16][z], it(15));
            }
        } catch (final Exception e) {
            System.out.println("Error setting density. You probably didn't initialize the interpolator.");
        }
    }
    
    private double it(final int iter) {
        return iter * (1.0 / 16.0);
    }
}
