package engine.default_Materials;

import engine.default_shaders.SpecularShader;
import engine.graphics.Material;
import engine.math.Vector3f;

public class SpecularMaterial extends Material
{
    
    public SpecularMaterial()
    {
        super();
        this.setShader(SpecularShader.getInstance());
        this.addProperty("specularIntensity", 2f);
        this.addProperty("specularPower", 32f);
        this.addProperty("eyePos", Vector3f.ZERO_VECTOR);
    }
    
}
