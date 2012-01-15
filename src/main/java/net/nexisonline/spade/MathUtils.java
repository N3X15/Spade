package net.nexisonline.spade;

public class MathUtils {
    public static double getDist2D(final double a_x, final double a_z, final double b_x, final double b_z) {
        return Math.sqrt(Math.pow(a_x - b_x, 2) + Math.pow(a_z - b_z, 2));
    }
    
    public static double getDist2DSquared(final double a_x, final double a_z, final double b_x, final double b_z) {
        return Math.pow(a_x - b_x, 2) + Math.pow(a_z - b_z, 2);
    }
    
    public static double getDist3D(final double a_x, final double a_y, final double a_z, final double b_x, final double b_y, final double b_z) {
        return Math.sqrt(Math.pow(a_x - b_x, 2) + Math.pow(a_y - b_y, 2) + Math.pow(a_z - b_z, 2));
    }
    
    public static double getDist3DSquared(final double a_x, final double a_y, final double a_z, final double b_x, final double b_y, final double b_z) {
        return Math.pow(a_x - b_x, 2) + Math.pow(a_y - b_y, 2) + Math.pow(a_z - b_z, 2);
    }
    
    public static double lerp(final double a, final double b, final double f) {
        return (a + ((b - a) * f));
    }
}
