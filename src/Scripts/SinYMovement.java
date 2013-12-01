package Scripts;

import engine.components.Component;
import engine.core.Time;
import engine.ext.GameObject;

public class SinYMovement extends Component
{
    
    public SinYMovement(GameObject gameObject)
    {
        super(gameObject, "SinYMovement");
    }
    
    @Override
    public void update(){
        gameObject.getTransform().setAnimPos(gameObject.getTransform().getPos().getX(), 
                                            (float)(gameObject.getTransform().getPos().getY()+Math.sin(Time.getDelta())), 
                                            gameObject.getTransform().getPos().getZ());
    }
    
}
