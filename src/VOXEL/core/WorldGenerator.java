package VOXEL.core;

import engine.math.Vector3f;

public class WorldGenerator 
{
    
    public static voxel GetVoxel(int x, int y, int z, Vector3f chunkPos){
        voxel res = new voxel();
        res.setType(1);
        res.setLight(7);
        
        float hills = calcHills(x, y, z, chunkPos);
        float caves = calcCaves(x, y, z, chunkPos);

        if(hills - (y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) < 4.3f){
            res.setType(2);
            res.setLight(7);
        }
        if(hills - (y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) < 1.1f){
            res.setType(3);
            res.setLight(7);
        }
        if((y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY()))>hills){
            res.setType(0);
            res.setLight(16);
        }
        
        if(caves < 0){
            res.setType(0);
        }
        
        return res;
    }
    
    private static float calcCaves(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x)/5, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y)/4f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)/5)*-1)+0.2f;
    }
    
    private static float calcHills(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f) * 7) + (VoxelGame.Y_DIMENSION*VoxelGame.CHUNK_SIZE_Y)-25;
    }
    
    private static float calcMinerals(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x)/6, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y)/3.6f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)/6)*-1)+0.2f;
    }
    
    public static voxel GetVoxelCubeWorld(int x, int y, int z, Vector3f chunkPos){
        voxel res = new voxel();
        res.setType(0);
        res.setLight(16);
        
        float caves = calcCaves(x, y, z, chunkPos);
        
        return res;
    }
    
}
