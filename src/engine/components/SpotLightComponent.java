package engine.components;

import engine.ext.GameObject;
import engine.graphics.LightModel;
import engine.lights.Attenuation;
import engine.lights.BaseLight;
import engine.lights.PointLight;
import engine.lights.SpotLight;
import engine.math.Vector3f;

public class SpotLightComponent extends Component
{   
    private SpotLight light;
    
    public SpotLightComponent(GameObject gameObject)
    {
        super(gameObject, "SpotLight");
        light = new SpotLight(new PointLight(new BaseLight(Vector3f.ONE_VECTOR,1), new Attenuation(0,5f,0.2f), gameObject.getTransform().getPos(), 30), Vector3f.FORWARD, 30);
        LightModel.AddSpotLight(light);
    }
    
    @Override
    public void update(){
        light.setPosition(gameObject.getTransform().getPosLight());
        Vector3f dir = Vector3f.FORWARD.rotate(gameObject.getTransform().getRotation().getX(), Vector3f.X_AXIS)
                                .rotate(gameObject.getTransform().getRotation().getY(), Vector3f.Y_AXIS)
                                .rotate(gameObject.getTransform().getRotation().getZ(), Vector3f.Z_AXIS);
        light.setDirection(dir);
    }
    
    public void setRange(float range){
        light.setRange(range);
    }
    
    public void setPointLight(PointLight light){
        this.light.setPointLight(light);
    }
    
    public void setAtten(Attenuation atten){
        light.setAtten(atten);
    }

    public void setCutoff(float cutoff) {
        light.setCutoff(cutoff);
    }
    
    @Override
    public void destroy(){
        LightModel.RemoveSpotLight(light);
    }    
    
}
