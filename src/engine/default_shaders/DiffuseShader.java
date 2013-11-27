package engine.default_shaders;

import engine.core.RenderUtil;
import engine.core.Texture;
import engine.graphics.LightModel;
import engine.graphics.Material;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.shaders.Shader;
import org.lwjgl.opengl.GL11;

public class DiffuseShader extends Shader
{  
    private static final DiffuseShader instance = new DiffuseShader();
    public static DiffuseShader getInstance(){
        return instance;
    }
    
    private DiffuseShader()
    {
        super();
        
        addVertexShaderFromFile("DefaultDiffuse.vs");
        addFragmentShaderFromFile("DefaultDiffuse.fs");
        compileShader();
        
        addUniform("transform");
        addUniform("transformProjected");
        addUniform("BaseColor");   
        addUniform("ambientLight");
        
        for (int i = 0; i < LightModel.MAX_DIR_LIGHTS; i++) {
            addUniform("directionalLights[" + i + "].base.color");
            addUniform("directionalLights[" + i + "].base.intensity");
            addUniform("directionalLights[" + i + "].direction");
        }
        
        for (int i = 0; i < LightModel.MAX_POINT_LIGHTS; i++) {
            addUniform("pointLights[" + i + "].base.color");
            addUniform("pointLights[" + i + "].base.intensity");
            addUniform("pointLights[" + i + "].atten.constant");
            addUniform("pointLights[" + i + "].atten.linear");
            addUniform("pointLights[" + i + "].atten.exponent");
            addUniform("pointLights[" + i + "].position");
            addUniform("pointLights[" + i + "].range");
        }

        for (int i = 0; i < LightModel.MAX_SPOT_LIGHTS; i++) {
            addUniform("spotLights[" + i + "].pointLight.base.color");
            addUniform("spotLights[" + i + "].pointLight.base.intensity");
            addUniform("spotLights[" + i + "].pointLight.atten.constant");
            addUniform("spotLights[" + i + "].pointLight.atten.linear");
            addUniform("spotLights[" + i + "].pointLight.atten.exponent");
            addUniform("spotLights[" + i + "].pointLight.position");
            addUniform("spotLights[" + i + "].pointLight.range");
            addUniform("spotLights[" + i + "].direction");
            addUniform("spotLights[" + i + "].cutoff");
        }
    }
    
    @Override
    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
        if (material.getProperty("BaseTexture", Texture.class) != null) {
            material.getProperty("BaseTexture", Texture.class).bind();
        } else {
            RenderUtil.unbindTextures();
        }

        setUniform("transformProjected", projectedMatrix);
        setUniform("transform", worldMatrix);
        setUniform("BaseColor", material.getProperty("BaseColor", Vector3f.class));

        setUniform("ambientLight", LightModel.GLOBAL_AMBIENT_LIGHT);
        
        for (int i = 0; i < LightModel.getDirectionalLights().length; i++) {
            setUniform("directionalLights[" + i + "]", LightModel.getDirectionalLights()[i]);
        }

        for (int i = 0; i < LightModel.getPointLights().length; i++) {
            setUniform("pointLights[" + i + "]", LightModel.getPointLights()[i]);
        }

        for (int i = 0; i < LightModel.getSpotLights().length; i++) {
            setUniform("spotLights[" + i + "]", LightModel.getSpotLights()[i]);
        }
    }
}
