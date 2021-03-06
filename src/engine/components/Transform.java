package engine.components;

import engine.ext.GameObject;
import engine.math.Matrix4f;
import engine.math.Vector3f;

public class Transform extends Component
{
    private Vector3f pos;
    private Vector3f rot;
    private Vector3f scale;
    
    private Vector3f animPos = Vector3f.ZERO_VECTOR;
    private Vector3f animRot = Vector3f.ZERO_VECTOR;
    private Vector3f animScale = Vector3f.ONE_VECTOR;
    
    public Vector3f forward = new Vector3f(0,0,1);
    public Vector3f up = new Vector3f(0,1,0);
    public Vector3f left = new Vector3f(-1,0,0);
    
    private Matrix4f transformationMatrix;
    
    public Transform(GameObject gameObject, Vector3f pos, Vector3f rot, Vector3f scale)
    {
        super(gameObject, "Transform");
        this.pos = pos;
        this.rot = rot;
        this.scale = scale;
    }
    
    public void calcTransformation(){
        Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX() + animPos.getX(), pos.getY() + animPos.getY(), pos.getZ() + animPos.getZ());
        Matrix4f rotationMatrix = new Matrix4f().initRotation(-rot.getX() + animRot.getX(), rot.getY() + animRot.getY(), rot.getZ() + animRot.getZ());
        Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX() * animScale.getX(), scale.getY() * animScale.getY(), scale.getZ() * animScale.getZ());
        transformationMatrix = translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
        
        if(super.getGameObject().getParent() != null){
            super.getGameObject().getParent().getTransform().calcTransformation();
            Matrix4f parentTranslationMatrix = super.getGameObject().getParent().getTransform().getTransformation();
            transformationMatrix = parentTranslationMatrix.mul(transformationMatrix);
        }
    }
    
    public Matrix4f getTransformation() {
        return transformationMatrix;
    }
    
    public Vector3f getPos() {
        return pos;
    }
    
    public Vector3f getPosLight(){        
        calcTransformation();
        Matrix4f mat = getTransformation();
        return new Vector3f(mat.get(0, 3), mat.get(1, 3), mat.get(2, 3));
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setPos(float x, float y, float z) {
        this.pos = new Vector3f(x, y, z);
    }
    
    public void translate(float x, float y, float z){
        pos = pos.add(new Vector3f(x,y,z));
    }
    
    public void translate(Vector3f t){
        pos = pos.add(t);
    }

    public Vector3f getRotation() {
        return rot;
    }

    public void setRotation(Vector3f rot) {
        forward = Vector3f.FORWARD.rotate(-rot.getX(), Vector3f.X_AXIS).rotate(-rot.getY(), Vector3f.Y_AXIS).rotate(-rot.getZ(), Vector3f.Z_AXIS);
        left = Vector3f.LEFT.rotate(-rot.getX(), Vector3f.X_AXIS).rotate(-rot.getY(), Vector3f.Y_AXIS).rotate(-rot.getZ(), Vector3f.Z_AXIS);
        up = Vector3f.UP.rotate(-rot.getX(), Vector3f.X_AXIS).rotate(-rot.getY(), Vector3f.Y_AXIS).rotate(-rot.getZ(), Vector3f.Z_AXIS);
        this.rot = rot;
    }

    public void setRotation(float x, float y, float z) {
        forward = Vector3f.FORWARD.rotate(-x, Vector3f.X_AXIS).rotate(-y, Vector3f.Y_AXIS).rotate(-z, Vector3f.Z_AXIS);
        left = Vector3f.LEFT.rotate(-x, Vector3f.X_AXIS).rotate(-y, Vector3f.Y_AXIS).rotate(-z, Vector3f.Z_AXIS);
        up = Vector3f.UP.rotate(-x, Vector3f.X_AXIS).rotate(-y, Vector3f.Y_AXIS).rotate(-z, Vector3f.Z_AXIS);
        this.rot = new Vector3f(x, y, z);
    }
    
    public void rotate(float x, float y, float z){
        rot.add(x, y, z);
    }
    
    public void resetAnimations(){
        animPos = Vector3f.ZERO_VECTOR;
        animRot = Vector3f.ZERO_VECTOR;
        animScale = Vector3f.ONE_VECTOR;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
    }

    public void setAnimPos(Vector3f animPos) {
        this.animPos = this.animPos.add(animPos);
    }
    
    public void setAnimPos(float x, float y, float z) {
        this.animPos = this.animPos.add(new Vector3f(x,y,z));
    }

    public void setAnimRot(Vector3f animRot) {
        this.animRot = this.animRot.add(animRot);
    }

    public void setAnimScale(Vector3f animScale) {
        this.animScale = this.animScale.mul(animScale);
    }
}
