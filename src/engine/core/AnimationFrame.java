package engine.core;

import engine.math.Vector3f;

public class AnimationFrame 
{
    
    private float time;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    
    public AnimationFrame(){
        this(0, Vector3f.ZERO_VECTOR, Vector3f.ZERO_VECTOR, Vector3f.ONE_VECTOR);
    }
    
    public AnimationFrame(float time, Vector3f position){
        this(time, position, Vector3f.ZERO_VECTOR, Vector3f.ONE_VECTOR);
    }
    
    public AnimationFrame(float time, Vector3f position, Vector3f rotation){
        this(time, position, rotation, Vector3f.ONE_VECTOR);
    }
    
    public AnimationFrame(float time, Vector3f position, Vector3f rotation, Vector3f scale){
        this.time = time;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
