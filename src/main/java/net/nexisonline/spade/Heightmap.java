package net.nexisonline.spade;

public class Heightmap {
	public double[][] map;
	public int height;
	public int width;
	
	public Heightmap(int sx,int sz) {
		map = new double[sx][sz];
		height=sz;
		width=sx;
	}
	
	public void set(int x,int z,double h) {
		map[x][z]=h;
	}
	
	public double get(int x,int z) {
		return map[x][z];
	}
}
