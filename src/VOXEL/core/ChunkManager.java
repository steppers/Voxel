package VOXEL.core;

import engine.math.Vector3f;
import java.util.ArrayList;

public class ChunkManager 
{
    public static ArrayList<Chunk> chunks = new ArrayList<>();
    
    private static final int VIEW_RANGE_CHUNKS = 8;
    
    public static void init(){
        chunks.add(new Chunk(new Vector3f(0, 0, 0)));
        chunks.add(new Chunk(new Vector3f(1, 0, 0)));
        chunks.add(new Chunk(new Vector3f(2, 0, 0)));
        chunks.add(new Chunk(new Vector3f(3, 0, 0)));
        chunks.add(new Chunk(new Vector3f(0, 0, 1)));
        chunks.add(new Chunk(new Vector3f(1, 0, 1)));
        chunks.add(new Chunk(new Vector3f(2, 0, 1)));
        chunks.add(new Chunk(new Vector3f(3, 0, 1)));
        chunks.add(new Chunk(new Vector3f(0, 0, 2)));
        chunks.add(new Chunk(new Vector3f(1, 0, 2)));
        chunks.add(new Chunk(new Vector3f(2, 0, 2)));
        chunks.add(new Chunk(new Vector3f(3, 0, 2)));
        chunks.add(new Chunk(new Vector3f(0, 0, 3)));
        chunks.add(new Chunk(new Vector3f(1, 0, 3)));
        chunks.add(new Chunk(new Vector3f(2, 0, 3)));
        chunks.add(new Chunk(new Vector3f(3, 0, 3)));
        for(Chunk c : chunks){
            c.Generate();
        }
    }
    
    public static Chunk getChunk(int x, int y, int z){
        for(Chunk c : chunks){
            if(c.chunkPos.getX() == x && c.chunkPos.getY() == y && c.chunkPos.getZ() == z){
                return c;
            }
        }
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
