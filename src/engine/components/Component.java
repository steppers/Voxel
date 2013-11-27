package engine.components;

import engine.ext.GameObject;

public class Component 
{
    public GameObject gameObject;
    private String type;
    private boolean active = true;
    
    public Component(GameObject gameObject, String type)
    {
        this.gameObject = gameObject;
        this.type = type;
    }
    
    public void input(){
        
    }
    
    public void update(){
        
    }
    
    public void render(){
        
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    } 

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void destroy(){
        
    }
}
