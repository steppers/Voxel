package VOXEL.core;

import engine.math.Vector3f;

public class WorldGenerator 
{
    
    public static voxel GetVoxel(int x, int y, int z, Vector3f chunkPos){
        voxel res = new voxel();
        res.setType(1);
        res.setLight(7);
        
        float hillst = calcHills(x, y, z, chunkPos);
        float hillsb = calcHillsB(x, y, z, chunkPos);
        float hillsf = calcHillsF(x, y, z, chunkPos);
        float hillsba = calcHillsBa(x, y, z, chunkPos);
        float hillsl = calcHillsL(x, y, z, chunkPos);
        float hillsr = calcHillsR(x, y, z, chunkPos);
        float caves = calcCaves(x, y, z, chunkPos);

        if(hillst - (y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) < 4.3f){
            res.setType(2);
            res.setLight(7);
        }
        if(hillst - (y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) < 1.1f){
            res.setType(3);
            res.setLight(7);
        }
        if((y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY()))>hillst){
            res.setType(0);
            res.setLight(16);
        }
        
        if((y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) - hillsb < 4.3f){
            res.setType(2);
            res.setLight(7);
        }
        if((y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY())) - hillsb < 1.1f){
            res.setType(3);
            res.setLight(7);
        }
        if((y+(VoxelGame.CHUNK_SIZE_Y*chunkPos.getY()))<hillsb){
            res.setType(0);
            res.setLight(16);
        }
        
        if(hillsr - (x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX())) < 4.3f && res.getType() != 0 && res.getType() != 3){
            res.setType(2);
            res.setLight(7);
        }
        if(hillsr - (x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX())) < 1.1f && res.getType()!= 0){
            res.setType(3);
            res.setLight(7);
        }
        if((x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX()))>hillsr){
            res.setType(0);
            res.setLight(16);
        }
        
        if(x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX()) - hillsl < 4.3f && res.getType() != 0 && res.getType() != 3){
            res.setType(2);
            res.setLight(7);
        }
        if(x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX()) - hillsl < 1.1f && res.getType()!= 0){
            res.setType(3);
            res.setLight(7);
        }
        if(x+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getX())<hillsl){
            res.setType(0);
            res.setLight(16);
        }
        
        if(hillsba - (z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ())) < 4.3f && res.getType() != 0 && res.getType() != 3){
            res.setType(2);
            res.setLight(7);
        }
        if(hillsba - (z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ())) < 1.1f && res.getType()!= 0){
            res.setType(3);
            res.setLight(7);
        }
        if((z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ()))>hillsba){
            res.setType(0);
            res.setLight(16);
        }
        
        if(z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ()) - hillsf < 4.3f && res.getType() != 0 && res.getType() != 3){
            res.setType(2);
            res.setLight(7);
        }
        if(z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ()) - hillsf < 1.1f && res.getType()!= 0){
            res.setType(3);
            res.setLight(7);
        }
        if(z+(VoxelGame.CHUNK_SIZE_XZ*chunkPos.getZ())<hillsf){
            res.setType(0);
            res.setLight(16);
        }
        
        if(caves < 0){
            res.setType(0);
        }
        
        return res;
    }
    
    private static float calcCaves(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x)/5, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y)/5f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)/5)*-1)+0.2f;
    }
    
    private static float calcHills(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f) * 7) + (VoxelGame.MAP_DIMENSION*VoxelGame.CHUNK_SIZE_XZ)-14;
    }
    
    private static float calcHillsB(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D((((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f)+VoxelGame.CHUNK_SIZE_XZ, (((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f)+VoxelGame.CHUNK_SIZE_XZ) * 7) + 14;
    }
    
    private static float calcHillsL(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y) / 5.73f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f) * 7) + 14;
    }
    
    private static float calcHillsR(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D((((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y) / 5.73f)+VoxelGame.CHUNK_SIZE_Y, (((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f)+VoxelGame.CHUNK_SIZE_XZ) * 7) + (VoxelGame.MAP_DIMENSION*VoxelGame.CHUNK_SIZE_XZ)-14;
    }
    
    private static float calcHillsF(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f, ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y) / 5.73f) * 7) + 14;
    }
    
    private static float calcHillsBa(int x, int y, int z, Vector3f chunkPos){
        return (VoxelGame.perlin.PerlinNoise_2D((((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f)+VoxelGame.CHUNK_SIZE_XZ, (((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y) / 5.73f)+VoxelGame.CHUNK_SIZE_Y) * 7) + (VoxelGame.MAP_DIMENSION*VoxelGame.CHUNK_SIZE_XZ)-14;
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
