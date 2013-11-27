package engine.components;

import engine.ext.GameObject;
import engine.graphics.LightModel;
import engine.lights.Attenuation;
import engine.lights.BaseLight;
import engine.lights.PointLight;
import engine.math.Vector3f;

public class PointLightComponent extends Component
{   
    private PointLight light;
    
    public PointLightComponent(GameObject gameObject)
    {
        super(gameObject, "PointLight");
        light = new PointLight(new BaseLight(Vector3f.ONE_VECTOR,1), new Attenuation(2,6f,0.1f), gameObject.getTransform().getPos(), 30);
        LightModel.AddPointLight(light);
        this.setActive(true);
    }
    
    @Override
    public void update(){
        light.setPosition(gameObject.getTransform().getPosLight());
    }
    
    public void setRange(float range){
        light.setRange(range);
    }
    
    public void setBaseLight(BaseLight light){
        this.light.setBaseLight(light);
    }
    
    public void setAtten(Attenuation atten){
        light.setAtten(atten);
    }
    
    @Override
    public void destroy(){
        LightModel.RemovePointLight(light);
    }    
}
