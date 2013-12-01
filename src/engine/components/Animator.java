package engine.components;

import engine.core.Animation;
import engine.core.AnimationFrame;
import engine.ext.GameObject;
import java.util.ArrayList;

public class Animator extends Component
{
    private ArrayList<Animation> animations = new ArrayList<>();
    private ArrayList<Animation> playing = new ArrayList<>();
    
    public Animator(GameObject gameObject)
    {
        super(gameObject, "Animator");
    }
    
    @Override
    public void update(){
        for(Animation a : playing){
            if(!a.paused){
                process(a.getFrame());
            }
        }
    }
    
    public void addAnimation(String name, String fileName){
        animations.add(new Animation(name, fileName));
    }
    
    public void setLooping(String name, boolean looping){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                a.setLooping(looping);
                break;
            }
        }
    }
    
    public void setSpeed(String name, float speed){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                a.setSpeed(speed);
                break;
            }
        }
    }
    
    public void play(String name){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                playing.add(a);
                break;
            }
        }
    }
    
    public void stop(String name){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                playing.remove(a);
                a.paused = false;
                a.setTotalTime(0);
                break;
            }
        }
    }
    
    public void pause(String name){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                a.paused = true;
                break;
            }
        }
    }
    
    public void unpause(String name){
        for(Animation a : animations){
            if(a.getName().equals(name)){
                a.paused = false;
                break;
            }
        }
    }
    
    private void process(AnimationFrame a){
        gameObject.getTransform().setAnimPos(a.getPosition());
        gameObject.getTransform().setAnimRot(a.getRotation());
        gameObject.getTransform().setAnimScale(a.getScale());
    }
    
}
