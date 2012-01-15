package net.nexisonline.spade;

public class InterpolatedDensityMap {
    public InterpolatedDensityMap() {
        m_density = new double[16][129][16];
    }
    
    public void setDensity(final int x, final int y, final int z, final double value) {
        m_density[x][y][z] = value;
        
        if (y > 0) {
            m_density[x][y - 1][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(1));
            m_density[x][y - 2][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(2));
            m_density[x][y - 3][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(3));
            m_density[x][y - 4][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(4));
            m_density[x][y - 5][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(5));
            m_density[x][y - 6][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(6));
            m_density[x][y - 7][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(7));
            m_density[x][y - 8][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(8));
            m_density[x][y - 9][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(9));
            m_density[x][y - 10][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(10));
            m_density[x][y - 11][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(11));
            m_density[x][y - 12][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(12));
            m_density[x][y - 13][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(13));
            m_density[x][y - 14][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(14));
            m_density[x][y - 15][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], getInterpolationAmount(15));
        }
    }
    
    public double getValue(final int x, final int y, final int z) {
        return m_density[x][y][z];
    }
    
    public void interpolate() {
        buildCells();
        interpolateCells();
    }
    
    private void buildCells() {
        for (int x = 0; x < 16; x += 5) {
            for (int y = 0; y <= 128; y++) {
                for (int z = 0; z < 16; z += 5) {
                    if (x < 15) {
                        for (int i = 1; i <= 4; i++) {
                            m_density[x + i][y][z] = lerp(m_density[x][y][z], m_density[x + 5][y][z], getFactor(i));
                        }
                    }
                    
                    if (z < 15) {
                        for (int i = 1; i <= 4; i++) {
                            m_density[x][y][z + i] = lerp(m_density[x][y][z], m_density[x][y][z + 5], getFactor(i));
                        }
                    }
                }
            }
        }
    }
    
    private void interpolateCells() {
        for (int x = 0; x < 15; x += 5) {
            for (int y = 0; y <= 128; y++) {
                for (int z = 0; z < 15; z += 5) {
                    for (int cx = 1; cx <= 4; cx++) {
                        for (int cz = 1; cz <= 4; cz++) {
                            m_density[x + cx][y][z + cz] = lerp(m_density[x + cx][y][z], m_density[x + cx][y][z + 5], getFactor(cz));
                        }
                    }
                }
            }
        }
    }
    
    private double getInterpolationAmount(final int iteration) {
        return iteration * (1.0 / 16.0);
    }
    
    private double getFactor(final int iter) {
        return iter * (1.0 / 5.0);
    }
    
    private double lerp(final double a, final double b, final double f) {
        return (a + ((b - a) * f));
    }
    
    private final double[][][] m_density;
    
    public double getDensity(final int x, final int y, final int z) {
        return m_density[x][y][z];
    }
}
