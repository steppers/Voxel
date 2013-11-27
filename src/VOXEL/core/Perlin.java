package VOXEL.core;

import static engine.math.VoxelMath.*;
import java.util.Random;

public class Perlin {

    private long seed;
    private Random rand;
    private int octave0 = 1;
    private float persistence = 0.4f;

    public Perlin(long seed) {
        this.seed = seed;
        rand = new Random();
    }

    public float Noise2D(float x, float y) {
        long temp = (long) (x * 71 * (seed) + y * 57 * (seed));
        rand.setSeed(temp);
        return (rand.nextFloat() * 2) - 1;
    }

    public float Noise3D(float x, float y, float z) {
        long temp = (long) (x * 71 * (seed) + y * 57 * (seed) + z * 839 * (seed));
        rand.setSeed(temp);
        return (rand.nextFloat() * 2) - 1;
    }

    public float SmoothNoise2D(float x, float y) {
        float corners = (Noise2D(x - 1, y - 1) + Noise2D(x + 1, y - 1) + Noise2D(x - 1, y + 1) + Noise2D(x + 1, y + 1)) / 16;
        float sides = (Noise2D(x - 1, y) + Noise2D(x + 1, y) + Noise2D(x, y - 1) + Noise2D(x, y + 1)) / 8;
        float center = Noise2D(x, y) / 4;
        return corners + sides + center;
    }

    public float SmoothNoise3D(float x, float y, float z) {
        float corners = (Noise3D(x - 1, y - 1, z - 1) + Noise3D(x + 1, y - 1, z - 1) + Noise3D(x - 1, y + 1, z - 1) + Noise3D(x + 1, y + 1, z - 1)
                + Noise3D(x - 1, y - 1, z + 1) + Noise3D(x + 1, y - 1, z + 1) + Noise3D(x - 1, y + 1, z + 1) + Noise3D(x + 1, y + 1, z + 1))/8;
        float sides = (Noise3D(x - 1, y, z) + Noise3D(x + 1, y, z) + Noise3D(x, y - 1, z) + Noise3D(x, y + 1, z)
                + Noise3D(x, y, z - 1) + Noise3D(x, y, z + 1))/6;
        float planes = (Noise3D(x - 1, y - 1, z) + Noise3D(x + 1, y - 1, z) + Noise3D(x - 1, y + 1, z) + Noise3D(x + 1, y + 1, z)
                + Noise3D(x - 1, y, z - 1) + Noise3D(x + 1, y, z - 1) + Noise3D(x - 1, y, z + 1) + Noise3D(x + 1, y, z + 1)
                + Noise3D(x, y - 1, z - 1) + Noise3D(x, y + 1, z - 1) + Noise3D(x, y - 1, z + 1) + Noise3D(x, y + 1, z + 1)) / 12;
        float center = Noise3D(x, y, z);
        return ((corners/2) + sides + (planes/1.5f) + center)/4;
    }

    public float InterpolatedNoise2D(float x, float y) {
        int iX = (int) x;
        int iY = (int) y;

        float fX = x - iX;
        float fY = y - iY;

        float v1 = SmoothNoise2D(iX, iY);
        float v2 = SmoothNoise2D(iX + 1, iY);
        float v3 = SmoothNoise2D(iX, iY + 1);
        float v4 = SmoothNoise2D(iX + 1, iY + 1);

        return biLerp(fX, fY, v1, v3, v2, v4, 0, 1, 0, 1);
    }

    public float InterpolatedNoise3D(float x, float y, float z) {
        int iX = (int) x;
        int iY = (int) y;
        int iZ = (int) z;

        float fX = x - iX;
        float fY = y - iY;
        float fZ = z - iZ;

        fX = (3 * fX * fX) - (2 * fX * fX * fX);
        fY = (3 * fY * fY) - (2 * fY * fY * fY);
        fZ = (3 * fZ * fZ) - (2 * fZ * fZ * fZ);

        float v1 = SmoothNoise3D(iX, iY, iZ);
        float v2 = SmoothNoise3D(iX + 1, iY, iZ);
        float v3 = SmoothNoise3D(iX, iY + 1, iZ);
        float v4 = SmoothNoise3D(iX + 1, iY + 1, iZ);
        float v5 = SmoothNoise3D(iX, iY, iZ + 1);
        float v6 = SmoothNoise3D(iX + 1, iY, iZ + 1);
        float v7 = SmoothNoise3D(iX, iY + 1, iZ + 1);
        float v8 = SmoothNoise3D(iX + 1, iY + 1, iZ + 1);

        return triLerp(fX, fY, fZ, v1, v5, v3, v7, v2, v6, v4, v8, 0, 1, 0, 1, 0, 1);
    }

    public float PerlinNoise_2D(float x, float y) {
        float total = 0;

        double freq;
        double amp;

        for (int i = 0; i < octave0; i++) {

            freq = Math.pow(2, i);
            amp = Math.pow(persistence, i);

            total += InterpolatedNoise2D((float) (x * freq), (float) (y * freq)) * amp;
        }
        return total;
    }

    public float PerlinNoise_3D(float x, float y, float z) {
        float total = 0;

        double freq;
        double amp;

        for (int i = 0; i < octave0; i++) {

            freq = Math.pow(2, i);

            total += InterpolatedNoise3D((float) (x * freq), (float) (y * freq), (float) (z * freq));
        }
        return total;
    }
}
