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
		if(x<0 || x>width-1 || z<0 || z>height-1) return;
		map[x][z]=h;
	}
	
	public double get(int x,int z) {
		if(x<0 || x>width-1 || z<0 || z>height-1) return 0d;
		return map[x][z];
	}
}
