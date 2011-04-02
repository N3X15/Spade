package net.nexisonline.spade;

public class Heightmap {
	public double[] map;
	
	public Heightmap() {
		map = new double[16*16];
	}
	
	public void set(int x,int z,double h) {
		map[z << 4 | x]=h;
	}
	
	public double get(int x,int z) {
		return map[z << 4 | x];
	}
}
