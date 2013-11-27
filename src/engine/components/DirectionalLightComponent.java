package engine.components;

import engine.ext.GameObject;
import engine.graphics.LightModel;
import engine.lights.BaseLight;
import engine.lights.DirectionalLight;
import engine.math.Vector3f;

public class DirectionalLightComponent extends Component
{
    
    private DirectionalLight light;
    
    public DirectionalLightComponent(GameObject gameObject)
    {
        super(gameObject, "DirectionalLight");
        light = new DirectionalLight(new BaseLight(Vector3f.ONE_VECTOR,1), Vector3f.ONE_VECTOR);
        LightModel.AddDirectionalLight(light);
    }
    
    @Override
    public void update(){
        Vector3f dir = Vector3f.FORWARD.rotate(gameObject.getTransform().getRotation().getX(), Vector3f.X_AXIS)
                                .rotate(gameObject.getTransform().getRotation().getY(), Vector3f.Y_AXIS)
                                .rotate(gameObject.getTransform().getRotation().getZ(), Vector3f.Z_AXIS);
        light.setDirection(dir);
    }
    
    public void setBaseLight(BaseLight light){
        this.light.setBase(light);
    }
    
    @Override
    public void destroy(){
        LightModel.RemoveDirectionalLight(light);
    }      
}
