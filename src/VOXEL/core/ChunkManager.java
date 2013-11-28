package VOXEL.core;

import engine.ext.Scene;
import engine.math.Vector3f;
import java.util.ArrayList;

public class ChunkManager 
{
    public static ArrayList<Chunk> chunks = new ArrayList<>();
    
    private static final int VIEW_RANGE_CHUNKS = 2;
    
    public static void init(){
        for(int x = 0; x < 2*VIEW_RANGE_CHUNKS; x++){
            for(int y = 0; y < VoxelGame.Y_DIMENSION; y++){
                for(int z = 0; z < 2*VIEW_RANGE_CHUNKS; z++){
                    chunks.add(new Chunk(new Vector3f(x,y,z)));
                }
            }
        }
        float time;
        float loadTime = System.currentTimeMillis();
        for(Chunk c : chunks){
            time = System.nanoTime();
            c.genChunk();
            System.out.println("Chunk Load time: " + (System.nanoTime() - time) + "ns");
        }

        for(Chunk c : chunks){
            c.Generate();
        }
        System.out.println("World Load time: " + (System.currentTimeMillis() - loadTime)/1000 + " secs");
    }
    
    public static void update(){
        ArrayList<Integer> del = new ArrayList<>();
        for(Chunk c : chunks){
            if(Scene.FindGameObjectWithTag("Camera").getTransform().getPos().sub(c.getTransform().getPos()).length() > VoxelGame.VIEW_DISTANCE){
                del.add(chunks.indexOf(c));
            }
        }
        for(Integer i : del){
            Vector3f pos = chunks.get(i).chunkPos;
            Scene.destroyGameObject(chunks.get(i));
            if(getChunk((int)pos.getX() + 1, (int)pos.getY(), (int)pos.getZ()) != null)
                getChunk((int)pos.getX() + 1, (int)pos.getY(), (int)pos.getZ()).updateMesh();
            if(getChunk((int)pos.getX() - 1, (int)pos.getY(), (int)pos.getZ()) != null)
                getChunk((int)pos.getX() - 1, (int)pos.getY(), (int)pos.getZ()).updateMesh();
            if(getChunk((int)pos.getX(), (int)pos.getY(), (int)pos.getZ() + 1) != null)
                getChunk((int)pos.getX(), (int)pos.getY(), (int)pos.getZ() + 1).updateMesh();
            if(getChunk((int)pos.getX(), (int)pos.getY(), (int)pos.getZ() - 1) != null)
                getChunk((int)pos.getX(), (int)pos.getY(), (int)pos.getZ() - 1).updateMesh();
            chunks.remove(chunks.get(i));
        }
        del.clear();
    }
    
    public static Chunk getChunk(int x, int y, int z){
        for(Chunk c : chunks){
            if(x == c.chunkPos.getX() && y == c.chunkPos.getY() && z == c.chunkPos.getZ())
                return c;
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
