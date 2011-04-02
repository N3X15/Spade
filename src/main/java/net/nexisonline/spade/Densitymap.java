package net.nexisonline.spade;

public class Densitymap {
		public double[] map;
		
		public Densitymap(int sx,int sy,int sz) {
			map = new double[sx*sy*sz];
		}
		
		public void set(int x,int y,int z,double h) {
			map[(x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F)]=h;
		}
		
		public double get(int x,int y,int z) {
			return map[(x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F)];
		}
}
