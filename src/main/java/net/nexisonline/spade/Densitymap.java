package net.nexisonline.spade;

public class Densitymap {
		public double[][][] map;
		public int height,width,depth;
		
		public Densitymap(int sx,int sy,int sz) {
			map = new double[sx][sy][sz];
			height=sz;
			width=sz;
			depth=sy;
		}
		
		public void set(int x,int y,int z,double h) {
			if (x < 0 || x >= width) x = (width + x) & (width - 1);
			if (y < 0 || y >= depth) y = (depth + y) & (depth - 1);
			if (z < 0 || z >= height) z = (height + z) & (height - 1);
			map[x][y][z]=h;
		}
		
		public double get(int x,int y,int z) {
			if (x < 0 || x >= width) x = (width + x) & (width - 1);
			if (y < 0 || y >= depth) y = (depth + y) & (depth - 1);
			if (z < 0 || z >= height) z = (height + z) & (height - 1);
			return map[x][y][z];
		}
}
