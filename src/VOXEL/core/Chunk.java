package VOXEL.core;

import engine.components.*;
import engine.core.Color;
import engine.core.Mesh;
import engine.core.Util;
import engine.core.Vertex;
import engine.default_Materials.DiffuseMaterial;
import engine.ext.GameObject;
import engine.ext.Scene;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.math.VoxelMath;
import java.util.ArrayList;

public class Chunk extends GameObject {

    public Vector3f chunkPos;
    MeshFilter filter;
    MeshRenderer renderer;
    Transform transform;
    DiffuseMaterial material;
    Mesh mesh;
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();
    private voxel[][][] voxels = new voxel[VoxelGame.CHUNK_SIZE_XZ][VoxelGame.CHUNK_SIZE_Y][VoxelGame.CHUNK_SIZE_XZ];

    public Chunk(Vector3f chunkPos) {
        super();
        this.chunkPos = chunkPos;
        getTransform().setPos(chunkPos.mul(VoxelGame.CHUNK_SIZE_XZ * VoxelGame.VOXEL_SIZE));

        filter = new MeshFilter(this);
        renderer = new MeshRenderer(this);
        material = new DiffuseMaterial();
        material.setProperty("BaseColor", new Vector3f(1f, 1f, 1f));
        material.setProperty("BaseTexture", VoxelGame.terrain);
        renderer.setMaterial(material);
        this.AddComponent(filter);
        this.AddComponent(renderer);

        genChunk();

        getComponent(MeshRenderer.class).setMaterial(material);
        getComponent(MeshFilter.class).setSharedMesh(mesh);

        Scene.addGameObject(this);
    }

    public void Generate() {
        float time = System.nanoTime();
        genChunk();
        calcLight();
        genMesh();

        getComponent(MeshRenderer.class).setMaterial(material);
        getComponent(MeshFilter.class).setSharedMesh(mesh);

        Scene.addGameObject(this);
        System.out.println("Chunk Load time: " + (System.nanoTime() - time));
    }
    
    public void calcLight(){
        for (int x = 0; x < VoxelGame.CHUNK_SIZE_XZ; x++) {
            for (int y = 0; y < VoxelGame.CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < VoxelGame.CHUNK_SIZE_XZ; z++) {
                    if (voxels[x][y][z].getType() == 0) {
                        for(int i = 1; i < VoxelGame.CHUNK_SIZE_Y - (y+1); i++){
                            if(voxels[x][y+i][z].getType() != 0){
                                voxels[x][y][z].setLight(9);
                                break;
                            }
                            if(i == VoxelGame.CHUNK_SIZE_Y - (y+2))
                                voxels[x][y][z].setLight(16);
                        }
                    }
                }
            }
        }
    }

    public void updateMesh() {
        calcLight();
        genMesh();
        getComponent(MeshFilter.class).setSharedMesh(mesh);
    }

    public void genChunk() {
        for (int x = 0; x < VoxelGame.CHUNK_SIZE_XZ; x++) {
            for (int y = 0; y < VoxelGame.CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < VoxelGame.CHUNK_SIZE_XZ; z++) {
                    
                    if (VoxelGame.perlin.PerlinNoise_3D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x), ((chunkPos.getY() * VoxelGame.CHUNK_SIZE_Y) + y), ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z)) > 0) {
                        voxels[x][y][z] = new voxel();
                        voxels[x][y][z].setType(1);
                        voxels[x][y][z].setLight((short)0);
                    } else {
                        voxels[x][y][z] = new voxel();
                        voxels[x][y][z].setType(0);
                        voxels[x][y][z].setLight((short)16);
                    }
                    
                    
//                    if (y < (VoxelGame.perlin.PerlinNoise_2D(((chunkPos.getX() * VoxelGame.CHUNK_SIZE_XZ) + x) / 5.73f, ((chunkPos.getZ() * VoxelGame.CHUNK_SIZE_XZ) + z) / 5.73f) * 10) + 40) {
//                        voxels[x][y][z] = new voxel();
//                        voxels[x][y][z].setType(1);
//                        voxels[x][y][z].setLight((short)0);
//                    } else {
//                        voxels[x][y][z] = new voxel();
//                        voxels[x][y][z].setType(0);
//                        voxels[x][y][z].setLight((short)16);
//                    }
                }
            }
        }
    }

    public void genMesh() {
        for (int x = 0; x < VoxelGame.CHUNK_SIZE_XZ; x++) {
            for (int y = 0; y < VoxelGame.CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < VoxelGame.CHUNK_SIZE_XZ; z++) {
                    if (voxels[x][y][z].getType() != 0) {
                        VoxelSurrounding surround = genSurrounding(x, y, z);
                        genVoxel(surround, new Vector3f(x, y, z), voxels[x][y][z].getType());
                    }
                }
            }
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);
        vertices.clear();

        Integer[] indexData = new Integer[indices.size()];
        indices.toArray(indexData);
        indices.clear();

        mesh = new Mesh(vertexData, Util.toIntArray(indexData), true);
    }

    public voxel[][][] getVoxels() {
        return voxels;
    }

    public void setVoxels(voxel[][][] voxels) {
        this.voxels = voxels;
    }

    public voxel getVoxel(int x, int y, int z) {
        return voxels[x][y][z];
    }

    public void setVoxel(int x, int y, int z, voxel v) {
        voxels[x][y][z] = v;
    }

    private VoxelSurrounding genSurrounding(int x, int y, int z) {
        VoxelSurrounding s = new VoxelSurrounding();
        
        s.vs[1][1][1] = voxels[x][y][z];

        //Top, bottom, left, right, front, back
        if (x != 0) {
            s.vs[0][1][1] = voxels[x - 1][y][z];
        }
        if (y != 0) {
            s.vs[1][0][1] = voxels[x][y - 1][z];
        }
        if (z != 0) {
            s.vs[1][1][0] = voxels[x][y][z - 1];
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][1][1] = voxels[x + 1][y][z];
        }
        if (y != VoxelGame.CHUNK_SIZE_Y - 1) {
            s.vs[1][2][1] = voxels[x][y + 1][z];
        }
        if (z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[1][1][2] = voxels[x][y][z + 1];
        }

        //XZ plane
        if (x != 0 && z != 0) {
            s.vs[0][1][0] = voxels[x - 1][y][z - 1];
        } else {
            s.vs[0][1][0] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && z != 0) {
            s.vs[2][1][0] = voxels[x + 1][y][z - 1];
        } else {
            s.vs[2][1][0] = new voxel();
        }
        if (x != 0 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[0][1][2] = voxels[x - 1][y][z + 1];
        } else {
            s.vs[0][1][2] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][1][2] = voxels[x + 1][y][z + 1];
        } else {
            s.vs[2][1][2] = new voxel();
        }

        //ZY Plane
        if (y != 0 && z != 0) {
            s.vs[1][0][0] = voxels[x][y - 1][z - 1];
        } else {
            s.vs[1][0][0] = new voxel();
        }
        if (y != VoxelGame.CHUNK_SIZE_Y - 1 && z != 0) {
            s.vs[1][2][0] = voxels[x][y + 1][z - 1];
        } else {
            s.vs[1][2][0] = new voxel();
        }
        if (y != 0 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[1][0][2] = voxels[x][y - 1][z + 1];
        } else {
            s.vs[1][0][2] = new voxel();
        }
        if (y != VoxelGame.CHUNK_SIZE_Y - 1 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[1][2][2] = voxels[x][y + 1][z + 1];
        } else {
            s.vs[1][2][2] = new voxel();
        }

        //XY Plane
        if (y != 0 && x != 0) {
            s.vs[0][0][1] = voxels[x - 1][y - 1][z];
        } else {
            s.vs[0][0][1] = new voxel();
        }
        if (y != VoxelGame.CHUNK_SIZE_Y - 1 && x != 0) {
            s.vs[0][2][1] = voxels[x - 1][y + 1][z];
        } else {
            s.vs[0][2][1] = new voxel();
        }
        if (y != 0 && x != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][0][1] = voxels[x + 1][y - 1][z];
        } else {
            s.vs[2][0][1] = new voxel();
        }
        if (y != VoxelGame.CHUNK_SIZE_Y - 1 && x != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][2][1] = voxels[x + 1][y + 1][z];
        } else {
            s.vs[2][2][1] = new voxel();
        }

        //bottom corners
        if (x != 0 && y != 0 && z != 0) {
            s.vs[0][0][0] = voxels[x - 1][y - 1][z - 1];
        } else {
            s.vs[0][0][0] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && y != 0 && z != 0) {
            s.vs[2][0][0] = voxels[x + 1][y - 1][z - 1];
        } else {
            s.vs[2][0][0] = new voxel();
        }
        if (x != 0 && y != 0 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[0][0][2] = voxels[x - 1][y - 1][z + 1];
        } else {
            s.vs[0][0][2] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && y != 0 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][0][2] = voxels[x + 1][y - 1][z + 1];
        } else {
            s.vs[2][0][2] = new voxel();
        }

        //top corners
        if (x != 0 && y != VoxelGame.CHUNK_SIZE_Y - 1 && z != 0) {
            s.vs[0][2][0] = voxels[x - 1][y + 1][z - 1];
        } else {
            s.vs[0][2][0] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && y != VoxelGame.CHUNK_SIZE_Y - 1 && z != 0) {
            s.vs[2][2][0] = voxels[x + 1][y + 1][z - 1];
        } else {
            s.vs[2][2][0] = new voxel();
        }
        if (x != 0 && y != VoxelGame.CHUNK_SIZE_Y - 1 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[0][2][2] = voxels[x - 1][y + 1][z + 1];
        } else {
            s.vs[0][2][2] = new voxel();
        }
        if (x != VoxelGame.CHUNK_SIZE_XZ - 1 && y != VoxelGame.CHUNK_SIZE_Y - 1 && z != VoxelGame.CHUNK_SIZE_XZ - 1) {
            s.vs[2][2][2] = voxels[x + 1][y + 1][z + 1];
        } else {
            s.vs[2][2][2] = new voxel();
        }

        if (x == 0) {
            if (ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()) != null) {
                s.vs[0][1][1] = ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()).getVoxel(15, y, z);
            } else {
                s.vs[0][1][1] = new voxel();
            }
        }
        if (y == 0) {
            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY() - 1, (int) chunkPos.getZ()) != null) {
                s.vs[1][0][1] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY() - 1, (int) chunkPos.getZ()).getVoxel(x, 15, z);
            } else {
                s.vs[1][0][1] = new voxel();
            }
        }
        if (z == 0) {
            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY(), (int) chunkPos.getZ() - 1) != null) {
                s.vs[1][1][0] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY(), (int) chunkPos.getZ() - 1).getVoxel(x, y, 15);
            } else {
                s.vs[1][1][0] = new voxel();
            }
        }
        if (x == VoxelGame.CHUNK_SIZE_XZ - 1) {
            if (ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()) != null) {
                s.vs[2][1][1] = ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()).getVoxel(0, y, z);
            } else {
                s.vs[2][1][1] = new voxel();
            }
        }
        if (y == VoxelGame.CHUNK_SIZE_Y - 1) {
            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY() + 1, (int) chunkPos.getZ()) != null) {
                s.vs[1][2][1] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY() + 1, (int) chunkPos.getZ()).getVoxel(x, 0, z);
            } else {
                s.vs[1][2][1] = new voxel();
            }
        }
        if (z == VoxelGame.CHUNK_SIZE_XZ - 1) {
            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY(), (int) chunkPos.getZ() + 1) != null) {
                s.vs[1][1][2] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY(), (int) chunkPos.getZ() + 1).getVoxel(x, y, 0);
            } else {
                s.vs[1][1][2] = new voxel();
            }
        }

//        //XZ Plane
//        if(x == 0 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()-1) != null) {
//                s.vs[0][1][0] = ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()-1).getVoxel(15, y, 15);
//            } else {
//                s.vs[0][1][0] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()-1) != null) {
//                s.vs[2][1][0] = ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()-1).getVoxel(0, y, 15);
//            } else {
//                s.vs[2][1][0] = new voxel();
//            }
//        }
//        if(x == 0 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()+1) != null) {
//                s.vs[0][1][2] = ChunkManager.getChunk((int) chunkPos.getX() - 1, (int) chunkPos.getY(), (int) chunkPos.getZ()+1).getVoxel(15, y, 0);
//            } else {
//                s.vs[0][1][2] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()+1) != null) {
//                s.vs[2][1][2] = ChunkManager.getChunk((int) chunkPos.getX() + 1, (int) chunkPos.getY(), (int) chunkPos.getZ()+1).getVoxel(0, y, 0);
//            } else {
//                s.vs[2][1][2] = new voxel();
//            }
//        }
//        
//        //YZ Plane
//        if(y == 0 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[1][0][0] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1).getVoxel(x, 15, 15);
//            } else {
//                s.vs[1][0][0] = new voxel();
//            }
//        }
//        if(y == VoxelGame.CHUNK_SIZE_Y - 1 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[1][2][0] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1).getVoxel(x, 0, 15);
//            } else {
//                s.vs[1][2][0] = new voxel();
//            }
//        }
//        if(y == 0 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[1][0][2] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1).getVoxel(x, 15, 0);
//            } else {
//                s.vs[1][0][2] = new voxel();
//            }
//        }
//        if(y == VoxelGame.CHUNK_SIZE_Y - 1 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[1][2][2] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1).getVoxel(x, 0, 0);
//            } else {
//                s.vs[1][2][2] = new voxel();
//            }
//        }
//        
//        //XY Plane
//        if(x == 0 && y == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()) != null) {
//                s.vs[0][0][1] = ChunkManager.getChunk((int) chunkPos.getX(), (int) chunkPos.getY()-1, (int) chunkPos.getZ()).getVoxel(15, 15, z);
//            } else {
//                s.vs[0][0][1] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()) != null) {
//                s.vs[0][2][1] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()).getVoxel(0, 15, z);
//            } else {
//                s.vs[0][2][1] = new voxel();
//            }
//        }
//        if(x == 0 && y == VoxelGame.CHUNK_SIZE_Y - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()) != null) {
//                s.vs[2][0][1] = ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()).getVoxel(15, 0, z);
//            } else {
//                s.vs[2][0][1] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == VoxelGame.CHUNK_SIZE_Y - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()) != null) {
//                s.vs[2][2][1] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()).getVoxel(0, 0, z);
//            } else {
//                s.vs[2][2][1] = new voxel();
//            }
//        }
//        
//        //BottomCorners
//        if(x == 0 && y == 0 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[0][0][0] = ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1).getVoxel(15, 15, 15);
//            } else {
//                s.vs[0][0][0] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == 0 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[2][0][0] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()-1).getVoxel(0, 15, 15);
//            } else {
//                s.vs[2][0][0] = new voxel();
//            }
//        }
//        if(x == 0 && y == 0 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[0][0][2] = ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1).getVoxel(15, 15, 0);
//            } else {
//                s.vs[0][0][2] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == 0 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[2][0][2] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()-1, (int) chunkPos.getZ()+1).getVoxel(0, 15, 0);
//            } else {
//                s.vs[2][0][2] = new voxel();
//            }
//        }
//        
//        //TopCorners
//        if(x == 0 && y == VoxelGame.CHUNK_SIZE_Y - 1 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[0][2][0] = ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1).getVoxel(15, 0, 15);
//            } else {
//                s.vs[0][2][0] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == VoxelGame.CHUNK_SIZE_Y - 1 && z == 0){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1) != null) {
//                s.vs[2][2][0] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()-1).getVoxel(0, 0, 15);
//            } else {
//                s.vs[2][2][0] = new voxel();
//            }
//        }
//        if(x == 0 && y == VoxelGame.CHUNK_SIZE_Y - 1 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[0][2][2] = ChunkManager.getChunk((int) chunkPos.getX()-1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1).getVoxel(15, 0, 0);
//            } else {
//                s.vs[0][2][2] = new voxel();
//            }
//        }
//        if(x == VoxelGame.CHUNK_SIZE_XZ - 1 && y == VoxelGame.CHUNK_SIZE_Y - 1 && z == VoxelGame.CHUNK_SIZE_XZ - 1){
//            if (ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1) != null) {
//                s.vs[2][2][2] = ChunkManager.getChunk((int) chunkPos.getX()+1, (int) chunkPos.getY()+1, (int) chunkPos.getZ()+1).getVoxel(0, 0, 0);
//            } else {
//                s.vs[2][2][2] = new voxel();
//            }
//        }
        return s;
    }

    private void genVoxel(VoxelSurrounding s, Vector3f pos, int type) {
        Vector2f[] uv = getUVS(type);

        float AOFactor = 0.5f;

        boolean invert = false;
        boolean invert2 = false;

        Vertex v1 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE));
        Vertex v2 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE));
        Vertex v3 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE));
        Vertex v4 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE));
        Vertex v5 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE));
        Vertex v6 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE));
        Vertex v7 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE));
        Vertex v8 = new Vertex(new Vector3f(pos.getX() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getY() * VoxelGame.VOXEL_SIZE + VoxelGame.VOXEL_SIZE, pos.getZ() * VoxelGame.VOXEL_SIZE));

        //Left
        if (s.vs[0][1][1].getType() == 0) {
            Vertex t1 = new Vertex(v1.getPos()).setColor(Color.white.mul((float)s.vs[0][1][1].getLight()/16)).setTexCoord(uv[0]);
            Vertex t2 = new Vertex(v2.getPos()).setColor(Color.white.mul((float)s.vs[0][1][1].getLight()/16)).setTexCoord(uv[3]);
            Vertex t3 = new Vertex(v3.getPos()).setColor(Color.white.mul((float)s.vs[0][1][1].getLight()/16)).setTexCoord(uv[1]);
            Vertex t4 = new Vertex(v4.getPos()).setColor(Color.white.mul((float)s.vs[0][1][1].getLight()/16)).setTexCoord(uv[2]);
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[0][2][1].getType() != 0) {
                t1.setColor(Color.white.mul(AOFactor).mul((float)s.vs[0][1][1].getLight()/16));
                invert = true;
            }
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[0][1][0].getType() != 0) {
                t2.setColor(Color.white.mul(AOFactor).mul((float)s.vs[0][1][1].getLight()/16));
            }
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[0][0][1].getType() != 0) {
                t3.setColor(Color.white.mul(AOFactor).mul((float)s.vs[0][1][1].getLight()/16));
                invert2 = true;
            }
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[0][1][0].getType() != 0) {
                t4.setColor(Color.white.mul(AOFactor).mul((float)s.vs[0][1][1].getLight()/16));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t1);
                vertices.add(t2);
                vertices.add(t3);
                vertices.add(t4);
            } else {
                vertices.add(t3);
                vertices.add(t4);
                vertices.add(t2);
                vertices.add(t1);
            }
            indices.add(vertices.size() - 4);//1
            indices.add(vertices.size() - 3);//2
            indices.add(vertices.size() - 2);//3
            indices.add(vertices.size() - 4);//1
            indices.add(vertices.size() - 1);//4
            indices.add(vertices.size() - 3);//2
            invert = false;
            invert2 = false;
        }
        //Bottom
        if (s.vs[1][0][1].getType() == 0) {
            Vertex t3 = new Vertex(v3.getPos()).setColor(Color.white.mul((float)s.vs[1][0][1].getLight()/16)).setTexCoord(uv[0]);
            Vertex t6 = new Vertex(v6.getPos()).setColor(Color.white.mul((float)s.vs[1][0][1].getLight()/16)).setTexCoord(uv[3]);
            Vertex t7 = new Vertex(v7.getPos()).setColor(Color.white.mul((float)s.vs[1][0][1].getLight()/16)).setTexCoord(uv[1]);
            Vertex t2 = new Vertex(v2.getPos()).setColor(Color.white.mul((float)s.vs[1][0][1].getLight()/16)).setTexCoord(uv[2]);
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[1][0][2].getType() != 0) {
                t3.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][0][1].getLight()/16));
                invert = true;
            }
            if (s.vs[2][0][1].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t6.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][0][1].getLight()/16));
            }
            if (s.vs[1][0][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][0][1].getType() != 0) {
                t7.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][0][1].getLight()/16));
                invert2 = true;
            }
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t2.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][0][1].getLight()/16));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t3);
                vertices.add(t6);
                vertices.add(t7);
                vertices.add(t2);
            } else {
                vertices.add(t7);
                vertices.add(t2);
                vertices.add(t6);
                vertices.add(t3);
            }
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 2);
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 1);
            indices.add(vertices.size() - 3);
            invert = false;
            invert2 = false;
        }
        //Front
        if (s.vs[1][1][0].getType() == 0) {
            Vertex t4 = new Vertex(v4.getPos()).setColor(Color.white.mul((float)s.vs[1][1][0].getLight()/16)).setTexCoord(uv[0]);
            Vertex t6 = new Vertex(v6.getPos()).setColor(Color.white.mul((float)s.vs[1][1][0].getLight()/16)).setTexCoord(uv[3]);
            Vertex t2 = new Vertex(v2.getPos()).setColor(Color.white.mul((float)s.vs[1][1][0].getLight()/16)).setTexCoord(uv[1]);
            Vertex t8 = new Vertex(v8.getPos()).setColor(Color.white.mul((float)s.vs[1][1][0].getLight()/16)).setTexCoord(uv[2]);
            if (s.vs[0][1][0].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t4.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][0].getLight()/16));
                invert = true;
            }
            if (s.vs[2][1][0].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t6.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][0].getLight()/16));
            }
            if (s.vs[0][1][0].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t2.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][0].getLight()/16));
                invert2 = true;
            }
            if (s.vs[1][2][0].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t8.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][0].getLight()/16));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t4);
                vertices.add(t6);
                vertices.add(t2);
                vertices.add(t8);
            } else {
                vertices.add(t2);
                vertices.add(t8);
                vertices.add(t6);
                vertices.add(t4);
            }
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 2);
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 1);
            indices.add(vertices.size() - 3);
            invert = false;
            invert2 = false;
        }
        //Right
        if (s.vs[2][1][1].getType() == 0) {
            Vertex t5 = new Vertex(v5.getPos()).setColor(Color.white.mul((float)s.vs[2][1][1].getLight()/16)).setTexCoord(uv[0]);
            Vertex t6 = new Vertex(v6.getPos()).setColor(Color.white.mul((float)s.vs[2][1][1].getLight()/16)).setTexCoord(uv[3]);
            Vertex t7 = new Vertex(v7.getPos()).setColor(Color.white.mul((float)s.vs[2][1][1].getLight()/16)).setTexCoord(uv[1]);
            Vertex t8 = new Vertex(v8.getPos()).setColor(Color.white.mul((float)s.vs[2][1][1].getLight()/16)).setTexCoord(uv[2]);
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t5.setColor(Color.white.mul(AOFactor).mul((float)s.vs[2][1][1].getLight()/16));
                invert = true;
            }
            if (s.vs[2][0][1].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t6.setColor(Color.white.mul(AOFactor).mul((float)s.vs[2][1][1].getLight()/16));
            }
            if (s.vs[2][1][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][0][1].getType() != 0) {
                t7.setColor(Color.white.mul(AOFactor).mul((float)s.vs[2][1][1].getLight()/16));
                invert2 = true;
            }
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t8.setColor(Color.white.mul(AOFactor).mul((float)s.vs[2][1][1].getLight()/16));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t5);
                vertices.add(t6);
                vertices.add(t7);
                vertices.add(t8);
            } else {
                vertices.add(t7);
                vertices.add(t8);
                vertices.add(t6);
                vertices.add(t5);
            }
            indices.add(vertices.size() - 2);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 1);
            indices.add(vertices.size() - 4);
            invert = false;
            invert2 = false;
        }
        //Top
        if (s.vs[1][2][1].getType() == 0) {
            Vertex t1 = new Vertex(v1.getPos()).setColor(Color.white.mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f)).setTexCoord(uv[0]);
            Vertex t8 = new Vertex(v8.getPos()).setColor(Color.white.mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f)).setTexCoord(uv[3]);
            Vertex t5 = new Vertex(v5.getPos()).setColor(Color.white.mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f)).setTexCoord(uv[1]);
            Vertex t4 = new Vertex(v4.getPos()).setColor(Color.white.mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f)).setTexCoord(uv[2]);
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[1][2][2].getType() != 0) {
                t1.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f));
                invert = true;
            }
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t8.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f));
            }
            if (s.vs[1][2][2].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][2][1].getType() != 0) {
                t5.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f));
                invert2 = true;
            }
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t4.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][2][1].getLight()/16).mul(0.75f));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t1);
                vertices.add(t8);
                vertices.add(t5);
                vertices.add(t4);
            } else {
                vertices.add(t5);
                vertices.add(t4);
                vertices.add(t8);
                vertices.add(t1);
            }
            indices.add(vertices.size() - 2);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 1);
            indices.add(vertices.size() - 4);
            invert = false;
            invert2 = false;
        }
        //Back
        if (s.vs[1][1][2].getType() == 0) {
            Vertex t1 = new Vertex(v1.getPos()).setColor(Color.white.mul((float)s.vs[1][1][2].getLight()/16)).setTexCoord(uv[0]);
            Vertex t7 = new Vertex(v7.getPos()).setColor(Color.white.mul((float)s.vs[1][1][2].getLight()/16)).setTexCoord(uv[3]);
            Vertex t3 = new Vertex(v3.getPos()).setColor(Color.white.mul((float)s.vs[1][1][2].getLight()/16)).setTexCoord(uv[1]);
            Vertex t5 = new Vertex(v5.getPos()).setColor(Color.white.mul((float)s.vs[1][1][2].getLight()/16)).setTexCoord(uv[2]);
            if (s.vs[1][2][2].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[0][1][2].getType() != 0) {
                t1.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][2].getLight()/16));
                invert = true;
            }
            if (s.vs[1][0][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t7.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][2].getLight()/16));
            }
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[1][0][2].getType() != 0) {
                t3.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][2].getLight()/16));
                invert2 = true;
            }
            if (s.vs[1][2][2].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t5.setColor(Color.white.mul(AOFactor).mul((float)s.vs[1][1][2].getLight()/16));
            }
            if ((!invert && invert2) || (invert && !invert2)) {
                vertices.add(t1);
                vertices.add(t7);
                vertices.add(t3);
                vertices.add(t5);
            } else {
                vertices.add(t3);
                vertices.add(t5);
                vertices.add(t7);
                vertices.add(t1);
            }
            indices.add(vertices.size() - 2);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 4);
            indices.add(vertices.size() - 3);
            indices.add(vertices.size() - 1);
            indices.add(vertices.size() - 4);
            invert = false;
            invert2 = false;
        }
    }

    private Vector2f[] getUVS(int type) {
        Vector2f[] uv = new Vector2f[4];
        float x, y;
        x = type % 16;
        y = (int)(type/16);

        uv[0] = new Vector2f(x*0.125f, y*0.125f);
        uv[1] = new Vector2f(x*0.125f + 0.125f, y*0.125f);
        uv[2] = new Vector2f(x*0.125f, y*0.125f + 0.125f);
        uv[3] = new Vector2f(x*0.125f + 0.125f, y*0.125f + 0.125f);
        return uv;
    }
    
    private Vector3f[] getTypeColor(int type){
        Vector3f[] c = new Vector3f[6];
        for(int i = 0; i < 6; i++){
            c[i] = Color.white;
        }
        switch(type){
            case 1:
        }
        return c;
    }
    
    public Chunk getAdjactentChunk(int x, int y, int z){
        Chunk c = ChunkManager.getChunk((int)(chunkPos.getX() + x), (int)(chunkPos.getY() + y), (int)(chunkPos.getZ() + z));
        return c;
    }
}

class VoxelSurrounding {

    public voxel[][][] vs = new voxel[3][3][3];
}