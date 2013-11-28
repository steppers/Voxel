package VOXEL.core;

import engine.math.Vector3f;

public class ChunkManager 
{
    public static Chunk[][][] chunks = new Chunk[VoxelGame.MAP_DIMENSION][VoxelGame.MAP_DIMENSION][VoxelGame.MAP_DIMENSION];
    
    private static final int VIEW_RANGE_CHUNKS = 8;
    
    public static void init(){
        for(int x = 0; x < VoxelGame.MAP_DIMENSION; x++){
            for(int y = 0; y < VoxelGame.MAP_DIMENSION; y++){
                for(int z = 0; z < VoxelGame.MAP_DIMENSION; z++){
                    chunks[x][y][z] = new Chunk(new Vector3f(x,y,z));
                }
            }
        }
        float time;
        float loadTime = System.currentTimeMillis();
        for(int x = 0; x < VoxelGame.MAP_DIMENSION; x++){
            for(int y = 0; y < VoxelGame.MAP_DIMENSION; y++){
                for(int z = 0; z < VoxelGame.MAP_DIMENSION; z++){
                    time = System.nanoTime();
                    chunks[x][y][z].genChunk();
                    System.out.println("Chunk Load time: " + (System.nanoTime() - time) + "ns");
                }
            }
        }
        for(int x = 0; x < VoxelGame.MAP_DIMENSION; x++){
            for(int y = 0; y < VoxelGame.MAP_DIMENSION; y++){
                for(int z = 0; z < VoxelGame.MAP_DIMENSION; z++){
                    chunks[x][y][z].Generate();
                }
            }
        }
        System.out.println("World Load time: " + (System.currentTimeMillis() - loadTime)/1000 + " secs");
    }
    
    public static Chunk getChunk(int x, int y, int z){
        if(x >= VoxelGame.MAP_DIMENSION || y >= VoxelGame.MAP_DIMENSION || z >= VoxelGame.MAP_DIMENSION || x < 0 || y < 0 || z < 0)
            return null;
        if(chunks[x][y][z] != null)
            return chunks[x][y][z];
        else
            return null;
    }
    
    public static voxel getBlockFromWorldPos(float x, float y, float z){
        int xV = (int)((x%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int yV = (int)((y%(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int zV = (int)((z%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        
        if(xV < 0)
            xV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(xV);
        if(yV < 0)
            yV = VoxelGame.CHUNK_SIZE_Y - Math.abs(yV);
        if(zV < 0)
            zV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(zV);
        
        int xC = (int)(x/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        int yC = (int)(y/(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE));
        int zC = (int)(z/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        if(getChunk(xC, yC, zC) != null)
            return getChunk(xC, yC, zC).getVoxel(xV, yV, zV);   
        else
            return new voxel();
    }   
    
    public static voxel getBlockFromWorldPos(Vector3f v){
        int xV = (int)((v.getX()%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int yV = (int)((v.getY()%(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int zV = (int)((v.getZ()%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        
        if(xV < 0)
            xV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(xV);
        if(yV < 0)
            yV = VoxelGame.CHUNK_SIZE_Y - Math.abs(yV);
        if(zV < 0)
            zV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(zV);
        
        int xC = (int)(v.getX()/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        int yC = (int)(v.getY()/(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE));
        int zC = (int)(v.getZ()/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        if(getChunk(xC, yC, zC) != null)
            return getChunk(xC, yC, zC).getVoxel(xV, yV, zV);   
        else
            return new voxel();
    } 
    
    public static Chunk getChunkFromWorldPos(Vector3f v){
        int xC = (int)(v.getX()/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        int yC = (int)(v.getY()/(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE));
        int zC = (int)(v.getZ()/(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE));
        return getChunk(xC, yC, zC);
    }
    
    public static Vector3f getBlockPositionFromWorldPos(Vector3f v){
        int xV = (int)((v.getX()%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int yV = (int)((v.getY()%(VoxelGame.CHUNK_SIZE_Y*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        int zV = (int)((v.getZ()%(VoxelGame.CHUNK_SIZE_XZ*VoxelGame.VOXEL_SIZE))*VoxelGame.VOXEL_SIZE);
        
        if(xV < 0)
            xV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(xV);
        if(yV < 0)
            yV = VoxelGame.CHUNK_SIZE_Y - Math.abs(yV);
        if(zV < 0)
            zV = VoxelGame.CHUNK_SIZE_XZ - Math.abs(zV);
        return new Vector3f(xV,yV,zV);
    }
}
