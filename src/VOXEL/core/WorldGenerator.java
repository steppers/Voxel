package VOXEL.core;

import engine.math.Vector3f;

public class WorldGenerator 
{
    
    public static float GetVoxel(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x)/31.7335f, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y)/31.7335f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)/31.7335f)*-1);
    }
    
}
