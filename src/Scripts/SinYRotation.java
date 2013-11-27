package Scripts;

import engine.components.Component;
import engine.core.Time;
import engine.ext.GameObject;

public class SinYRotation extends Component
{
    
    public SinYRotation(GameObject gameObject)
    {
        super(gameObject, "SinYRotation");
    }
    
    @Override
    public void update(){
        gameObject.getTransform().setRotation(gameObject.getTransform().getRotation().getX(), 
                                            (float)(gameObject.getTransform().getRotation().getY()+90*Math.sin(Time.getDelta())), 
                                            gameObject.getTransform().getRotation().getZ());
    }
    
}
