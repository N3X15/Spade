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
			map[(x & 0xF)][(z & 0xF)][(y & 0x7F)]=h;
		}
		
		public double get(int x,int y,int z) {
			return map[(x & 0xF)][(z & 0xF)][(y & 0x7F)];
		}
}
