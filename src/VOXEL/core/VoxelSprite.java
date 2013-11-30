package VOXEL.core;

import engine.components.MeshFilter;
import engine.components.MeshRenderer;
import engine.components.Transform;
import engine.core.Color;
import engine.core.Mesh;
import engine.core.Util;
import engine.core.Vertex;
import engine.default_Materials.DiffuseMaterial;
import engine.ext.GameObject;
import engine.math.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class VoxelSprite extends GameObject {

    public final int resolution;
    public final float voxelsize;
    public voxel voxels[][][];
    
    Mesh mesh;
    MeshFilter filter;
    MeshRenderer renderer;
    Transform transform;
    DiffuseMaterial material;
    
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();

    public VoxelSprite(Vector3f pos, int resolution, String fileName) {
        super();
        getTransform().setPos(pos);

        this.resolution = resolution;
        this.voxelsize = 1/(float)resolution;
        voxels = new voxel[resolution][resolution][resolution];

        filter = new MeshFilter(this);
        renderer = new MeshRenderer(this);
        material = new DiffuseMaterial();
        material.setProperty("BaseColor", new Vector3f(1f, 1f, 1f));
        renderer.setMaterial(material);
        this.AddComponent(filter);
        this.AddComponent(renderer);

        getComponent(MeshRenderer.class).setMaterial(material);
        
        load(fileName);
        calcLight();
        genMesh();
        getComponent(MeshFilter.class).setSharedMesh(mesh);
    }
    
    private void calcLight(){
        for (int x = 0; x < resolution; x++) {
            for (int y = 0; y < resolution; y++) {
                for (int z = 0; z < resolution; z++) {
                    if (voxels[x][y][z].getType() == 0) {
                        for (int i = 1; i < resolution - (y + 1); i++) {
                            if (voxels[x][y + i][z].getType() != 0) {
                                voxels[x][y][z].setLight(9);
                                break;
                            }
                            if (i == resolution - (y + 2)) {
                                voxels[x][y][z].setLight(16);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void genMesh(){
        for (int x = 0; x < resolution; x++) {
            for (int y = 0; y < resolution; y++) {
                for (int z = 0; z < resolution; z++) {
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

    public void load(String fileName) {
        for (int x = 0; x < resolution; x++) {
            for (int y = 0; y < resolution; y++) {
                for (int z = 0; z < resolution; z++) {
                    voxels[x][y][z] = new voxel();
                }
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("./res/voxelsprites/" + fileName));
            String line;
            int yLevel = 0;
            int z = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    yLevel = Integer.parseInt(line.split(" ")[1]);
                    z = resolution - 1;
                } else {
                    String[] tokens = line.split("/");
                    for (int x = 0; x < resolution; x++) {
                        voxels[x][yLevel][z].setType(Integer.parseInt(tokens[x]));
                    }
                    z--;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: Could not load voxel sprite, " + fileName);
            System.exit(1);
        }
    }
    
    private void genVoxel(VoxelSurrounding s, Vector3f pos, int type) {
        Vector3f col = getTypeColor(type);
        float AOFactor = 0.5f;

        boolean invert = false;
        boolean invert2 = false;

        Vertex v1 = new Vertex(new Vector3f(pos.getX() * voxelsize, pos.getY() * voxelsize + voxelsize, pos.getZ() * voxelsize + voxelsize));
        Vertex v2 = new Vertex(new Vector3f(pos.getX() * voxelsize, pos.getY() * voxelsize, pos.getZ() * voxelsize));
        Vertex v3 = new Vertex(new Vector3f(pos.getX() * voxelsize, pos.getY() * voxelsize, pos.getZ() * voxelsize + voxelsize));
        Vertex v4 = new Vertex(new Vector3f(pos.getX() * voxelsize, pos.getY() * voxelsize + voxelsize, pos.getZ() * voxelsize));
        Vertex v5 = new Vertex(new Vector3f(pos.getX() * voxelsize + voxelsize, pos.getY() * voxelsize + voxelsize, pos.getZ() * voxelsize + voxelsize));
        Vertex v6 = new Vertex(new Vector3f(pos.getX() * voxelsize + voxelsize, pos.getY() * voxelsize, pos.getZ() * voxelsize));
        Vertex v7 = new Vertex(new Vector3f(pos.getX() * voxelsize + voxelsize, pos.getY() * voxelsize, pos.getZ() * voxelsize + voxelsize));
        Vertex v8 = new Vertex(new Vector3f(pos.getX() * voxelsize + voxelsize, pos.getY() * voxelsize + voxelsize, pos.getZ() * voxelsize));
        
        //Left
        if (s.vs[0][1][1].getType() == 0) {
            Vertex t1 = new Vertex(v1.getPos()).setColor(col.mul((float) s.vs[0][1][1].getLight() / 16));
            Vertex t2 = new Vertex(v2.getPos()).setColor(col.mul((float) s.vs[0][1][1].getLight() / 16));
            Vertex t3 = new Vertex(v3.getPos()).setColor(col.mul((float) s.vs[0][1][1].getLight() / 16));
            Vertex t4 = new Vertex(v4.getPos()).setColor(col.mul((float) s.vs[0][1][1].getLight() / 16));
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[0][2][1].getType() != 0) {
                t1.setColor(col.mul(AOFactor).mul((float) s.vs[0][1][1].getLight() / 16));
                invert = true;
            }
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[0][1][0].getType() != 0) {
                t2.setColor(col.mul(AOFactor).mul((float) s.vs[0][1][1].getLight() / 16));
            }
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[0][0][1].getType() != 0) {
                t3.setColor(col.mul(AOFactor).mul((float) s.vs[0][1][1].getLight() / 16));
                invert2 = true;
            }
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[0][1][0].getType() != 0) {
                t4.setColor(col.mul(AOFactor).mul((float) s.vs[0][1][1].getLight() / 16));
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
            Vertex t3 = new Vertex(v3.getPos()).setColor(col.mul((float) s.vs[1][0][1].getLight() / 16));
            Vertex t6 = new Vertex(v6.getPos()).setColor(col.mul((float) s.vs[1][0][1].getLight() / 16));
            Vertex t7 = new Vertex(v7.getPos()).setColor(col.mul((float) s.vs[1][0][1].getLight() / 16));
            Vertex t2 = new Vertex(v2.getPos()).setColor(col.mul((float) s.vs[1][0][1].getLight() / 16));
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[1][0][2].getType() != 0) {
                t3.setColor(col.mul(AOFactor).mul((float) s.vs[1][0][1].getLight() / 16));
                invert = true;
            }
            if (s.vs[2][0][1].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t6.setColor(col.mul(AOFactor).mul((float) s.vs[1][0][1].getLight() / 16));
            }
            if (s.vs[1][0][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][0][1].getType() != 0) {
                t7.setColor(col.mul(AOFactor).mul((float) s.vs[1][0][1].getLight() / 16));
                invert2 = true;
            }
            if (s.vs[0][0][1].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t2.setColor(col.mul(AOFactor).mul((float) s.vs[1][0][1].getLight() / 16));
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
            Vertex t4 = new Vertex(v4.getPos()).setColor(col.mul((float) s.vs[1][1][0].getLight() / 16));
            Vertex t6 = new Vertex(v6.getPos()).setColor(col.mul((float) s.vs[1][1][0].getLight() / 16));
            Vertex t2 = new Vertex(v2.getPos()).setColor(col.mul((float) s.vs[1][1][0].getLight() / 16));
            Vertex t8 = new Vertex(v8.getPos()).setColor(col.mul((float) s.vs[1][1][0].getLight() / 16));
            if (s.vs[0][1][0].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t4.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][0].getLight() / 16));
                invert = true;
            }
            if (s.vs[2][1][0].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t6.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][0].getLight() / 16));
            }
            if (s.vs[0][1][0].getType() != 0 || s.vs[0][0][0].getType() != 0 || s.vs[1][0][0].getType() != 0) {
                t2.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][0].getLight() / 16));
                invert2 = true;
            }
            if (s.vs[1][2][0].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t8.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][0].getLight() / 16));
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
            Vertex t5 = new Vertex(v5.getPos()).setColor(col.mul((float) s.vs[2][1][1].getLight() / 16));
            Vertex t6 = new Vertex(v6.getPos()).setColor(col.mul((float) s.vs[2][1][1].getLight() / 16));
            Vertex t7 = new Vertex(v7.getPos()).setColor(col.mul((float) s.vs[2][1][1].getLight() / 16));
            Vertex t8 = new Vertex(v8.getPos()).setColor(col.mul((float) s.vs[2][1][1].getLight() / 16));
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t5.setColor(col.mul(AOFactor).mul((float) s.vs[2][1][1].getLight() / 16));
                invert = true;
            }
            if (s.vs[2][0][1].getType() != 0 || s.vs[2][0][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t6.setColor(col.mul(AOFactor).mul((float) s.vs[2][1][1].getLight() / 16));
            }
            if (s.vs[2][1][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][0][1].getType() != 0) {
                t7.setColor(col.mul(AOFactor).mul((float) s.vs[2][1][1].getLight() / 16));
                invert2 = true;
            }
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[2][1][0].getType() != 0) {
                t8.setColor(col.mul(AOFactor).mul((float) s.vs[2][1][1].getLight() / 16));
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
            Vertex t1 = new Vertex(v1.getPos()).setColor(col.mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
            Vertex t8 = new Vertex(v8.getPos()).setColor(col.mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
            Vertex t5 = new Vertex(v5.getPos()).setColor(col.mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
            Vertex t4 = new Vertex(v4.getPos()).setColor(col.mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[1][2][2].getType() != 0) {
                t1.setColor(col.mul(AOFactor).mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
                invert = true;
            }
            if (s.vs[2][2][1].getType() != 0 || s.vs[2][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t8.setColor(col.mul(AOFactor).mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
            }
            if (s.vs[1][2][2].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][2][1].getType() != 0) {
                t5.setColor(col.mul(AOFactor).mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
                invert2 = true;
            }
            if (s.vs[0][2][1].getType() != 0 || s.vs[0][2][0].getType() != 0 || s.vs[1][2][0].getType() != 0) {
                t4.setColor(col.mul(AOFactor).mul((float) s.vs[1][2][1].getLight() / 16).mul(0.75f));
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
            Vertex t1 = new Vertex(v1.getPos()).setColor(col.mul((float) s.vs[1][1][2].getLight() / 16));
            Vertex t7 = new Vertex(v7.getPos()).setColor(col.mul((float) s.vs[1][1][2].getLight() / 16));
            Vertex t3 = new Vertex(v3.getPos()).setColor(col.mul((float) s.vs[1][1][2].getLight() / 16));
            Vertex t5 = new Vertex(v5.getPos()).setColor(col.mul((float) s.vs[1][1][2].getLight() / 16));
            if (s.vs[1][2][2].getType() != 0 || s.vs[0][2][2].getType() != 0 || s.vs[0][1][2].getType() != 0) {
                t1.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][2].getLight() / 16));
                invert = true;
            }
            if (s.vs[1][0][2].getType() != 0 || s.vs[2][0][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t7.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][2].getLight() / 16));
            }
            if (s.vs[0][1][2].getType() != 0 || s.vs[0][0][2].getType() != 0 || s.vs[1][0][2].getType() != 0) {
                t3.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][2].getLight() / 16));
                invert2 = true;
            }
            if (s.vs[1][2][2].getType() != 0 || s.vs[2][2][2].getType() != 0 || s.vs[2][1][2].getType() != 0) {
                t5.setColor(col.mul(AOFactor).mul((float) s.vs[1][1][2].getLight() / 16));
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
    
    private VoxelSurrounding genSurrounding(int x, int y, int z) {
        VoxelSurrounding s = new VoxelSurrounding();

        s.vs[1][1][1] = voxels[x][y][z];

        //Top, bottom, left, right, front, back
        if (x != 0) {
            s.vs[0][1][1] = voxels[x - 1][y][z];
        }else{
            s.vs[0][1][1] = new voxel();
        }
        if (y != 0) {
            s.vs[1][0][1] = voxels[x][y - 1][z];
        }else{
            s.vs[1][0][1] = new voxel();
        }
        if (z != 0) {
            s.vs[1][1][0] = voxels[x][y][z - 1];
        }else{
            s.vs[1][1][0] = new voxel();
        }
        if (x != resolution - 1) {
            s.vs[2][1][1] = voxels[x + 1][y][z];
        }else{
            s.vs[2][1][1] = new voxel();
        }
        if (y != resolution - 1) {
            s.vs[1][2][1] = voxels[x][y + 1][z];
        }else{
            s.vs[1][2][1] = new voxel();
        }
        if (z != resolution - 1) {
            s.vs[1][1][2] = voxels[x][y][z + 1];
        }else{
            s.vs[1][1][2] = new voxel();
        }

        //XZ plane
        if (x != 0 && z != 0) {
            s.vs[0][1][0] = voxels[x - 1][y][z - 1];
        } else {
            s.vs[0][1][0] = new voxel();
        }
        if (x != resolution - 1 && z != 0) {
            s.vs[2][1][0] = voxels[x + 1][y][z - 1];
        } else {
            s.vs[2][1][0] = new voxel();
        }
        if (x != 0 && z != resolution - 1) {
            s.vs[0][1][2] = voxels[x - 1][y][z + 1];
        } else {
            s.vs[0][1][2] = new voxel();
        }
        if (x != resolution - 1 && z != resolution - 1) {
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
        if (y != resolution - 1 && z != 0) {
            s.vs[1][2][0] = voxels[x][y + 1][z - 1];
        } else {
            s.vs[1][2][0] = new voxel();
        }
        if (y != 0 && z != resolution - 1) {
            s.vs[1][0][2] = voxels[x][y - 1][z + 1];
        } else {
            s.vs[1][0][2] = new voxel();
        }
        if (y != resolution - 1 && z != resolution - 1) {
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
        if (y != resolution - 1 && x != 0) {
            s.vs[0][2][1] = voxels[x - 1][y + 1][z];
        } else {
            s.vs[0][2][1] = new voxel();
        }
        if (y != 0 && x != resolution - 1) {
            s.vs[2][0][1] = voxels[x + 1][y - 1][z];
        } else {
            s.vs[2][0][1] = new voxel();
        }
        if (y != resolution - 1 && x != resolution - 1) {
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
        if (x != resolution - 1 && y != 0 && z != 0) {
            s.vs[2][0][0] = voxels[x + 1][y - 1][z - 1];
        } else {
            s.vs[2][0][0] = new voxel();
        }
        if (x != 0 && y != 0 && z != resolution - 1) {
            s.vs[0][0][2] = voxels[x - 1][y - 1][z + 1];
        } else {
            s.vs[0][0][2] = new voxel();
        }
        if (x != resolution - 1 && y != 0 && z != resolution - 1) {
            s.vs[2][0][2] = voxels[x + 1][y - 1][z + 1];
        } else {
            s.vs[2][0][2] = new voxel();
        }

        //top corners
        if (x != 0 && y != resolution - 1 && z != 0) {
            s.vs[0][2][0] = voxels[x - 1][y + 1][z - 1];
        } else {
            s.vs[0][2][0] = new voxel();
        }
        if (x != resolution - 1 && y != resolution - 1 && z != 0) {
            s.vs[2][2][0] = voxels[x + 1][y + 1][z - 1];
        } else {
            s.vs[2][2][0] = new voxel();
        }
        if (x != 0 && y != resolution - 1 && z != resolution - 1) {
            s.vs[0][2][2] = voxels[x - 1][y + 1][z + 1];
        } else {
            s.vs[0][2][2] = new voxel();
        }
        if (x != resolution - 1 && y != resolution - 1 && z != resolution - 1) {
            s.vs[2][2][2] = voxels[x + 1][y + 1][z + 1];
        } else {
            s.vs[2][2][2] = new voxel();
        }
        return s;
    }
    
    private Vector3f getTypeColor(int type) {
        switch (type) {
            case 1:
                return Color.grey;
            case 2:
                return Color.green;
            case 3:
                return Color.blue;
            case 4:
                return Color.cyan;
            case 5:
                return Color.red;
            case 6:
                return Color.white;
        }
        return Vector3f.ONE_VECTOR;
    }
}
