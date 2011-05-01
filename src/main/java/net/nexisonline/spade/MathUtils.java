package net.nexisonline.spade;

public class MathUtils {
	public static double getDist2D(double a_x, double a_z, double b_x,
			double b_z) {
		return Math.sqrt(Math.pow(a_x - b_x, 2) + Math.pow(a_z - b_z, 2));
	}

	public static double getDist2DSquared(double a_x, double a_z, double b_x,
			double b_z) {
		return Math.pow(a_x - b_x, 2) + Math.pow(a_z - b_z, 2);
	}

	public static double getDist3D(double a_x, double a_y, double a_z,
			double b_x, double b_y, double b_z) {
		return Math.sqrt(Math.pow(a_x - b_x, 2) + Math.pow(a_y - b_y, 2) + Math.pow(a_z - b_z, 2));
	}

	public static double getDist3DSquared(double a_x, double a_y, double a_z,
			double b_x, double b_y, double b_z) {
		return Math.pow(a_x - b_x, 2) + Math.pow(a_y - b_y, 2) + Math.pow(a_z - b_z, 2);
	}

	public static double lerp(double a, double b, double f)
	{
		return (a + (b - a) * f);
	}
}
