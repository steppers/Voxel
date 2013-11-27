package VOXEL.core;

import engine.components.MeshFilter;
import engine.components.MeshRenderer;
import engine.components.Transform;
import engine.core.Mesh;
import engine.core.Vertex;
import engine.default_Materials.DiffuseMaterial;
import engine.ext.GameObject;
import engine.math.Vector3f;
import java.util.ArrayList;

public class VoxelSprite extends GameObject
{
    public final int resolution;
    public voxel voxels[][][];
    
    Mesh mesh;
    MeshFilter filter;
    MeshRenderer renderer;
    Transform transform;
    DiffuseMaterial material;
    
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();
    
    public Vector3f pos;
    
    public VoxelSprite(Vector3f pos, int resolution, float voxelSize, String fileName)
    {
        super();
        getTransform().setPos(pos);
        
        this.resolution = resolution;
        voxels = new voxel[resolution][resolution][resolution];
        
        filter = new MeshFilter(this);
        renderer = new MeshRenderer(this);
        material = new DiffuseMaterial();
        material.setProperty("BaseColor", new Vector3f(1f, 1f, 1f));
        material.setProperty("BaseTexture", VoxelGame.terrain);
        renderer.setMaterial(material);
        this.AddComponent(filter);
        this.AddComponent(renderer);
        
        getComponent(MeshRenderer.class).setMaterial(material);
        getComponent(MeshFilter.class).setSharedMesh(mesh);
        
        
    }
    
}
