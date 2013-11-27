package engine.components;

import engine.core.Mesh;
import engine.ext.GameObject;

public class MeshFilter extends Component
{
    private Mesh sharedMesh;
    
    public MeshFilter(GameObject gameObject)
    {
        super(gameObject, "MeshFilter");
    }

    public Mesh getSharedMesh() {
        return sharedMesh;
    }

    public void setSharedMesh(Mesh mesh) {
        this.sharedMesh = mesh;
    }
    
    public void destroy(){
        sharedMesh = null;
    }
}
