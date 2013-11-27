package VOXEL.core;

import engine.math.Vector3f;

public class WorldGenerator 
{
    
    public static voxel GetVoxel(int x, int y, int z, Vector3f chunkPos){
        voxel res = new voxel();
        res.setType(0);
        res.setLight(16);
        
        float hills = calcHills(x, y, z, chunkPos);
        float caves = calcCaves(x, y, z, chunkPos);
        if(y<hills){
            if(hills - y < 2.3f){
                res.setType(2);
                res.setLight(7);
            }else{
                res.setType(1);
                res.setLight(7);
            }
            
            if(hills - y < 1){
                res.setType(3);
                res.setLight(7);
            }
        }
        if(caves < 0){
            res.setType(0);
        }
        return res;
    }
    
    private static float calcCaves(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x)/6, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y)/3.6f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)/6)*-1)+0.2f;
    }
    
    private static float calcHills(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f) * 10) + 50;
    }
    
}
