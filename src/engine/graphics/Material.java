package engine.graphics;

import engine.components.MeshRenderer;
import engine.core.Texture;
import engine.math.Vector3f;
import engine.shaders.Shader;
import java.util.ArrayList;


public class Material 
{ 
    private MeshRenderer renderer;
    private Shader shader;
    private ArrayList<MaterialProperty> properties = new ArrayList<>();
    
    public Material(){
        addProperty("BaseColor", new Vector3f(0.5f, 0.5f, 0.5f));
        addProperty("BaseTexture", new Texture("blank.png"));
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
    
    public <T> void addProperty(String name, T data){
        MaterialProperty<T> p = new MaterialProperty<>();
        p.data = data; p.name = name;
        properties.add(p);
    }
    
    public <T> void setProperty(String name, T v){
        for(MaterialProperty<T> p : properties){
            if(p.name.equals(name)){
                p.data = v;
                return;
            }
        }
    }
    
    public <T> T getProperty(String name, Class<T> type){
        for(MaterialProperty<T> p : properties){
            if(p.name.equals(name))
                return p.data;
        }
        return null;
    }
}
class MaterialProperty<T>{
    public T data;
    public String name;
}