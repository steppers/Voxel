package engine.default_Materials;

import engine.default_shaders.DiffuseShader;
import engine.graphics.Material;

public class DiffuseMaterial extends Material
{   
    public DiffuseMaterial()
    {
        super();
        this.setShader(DiffuseShader.getInstance());
    }   
}
