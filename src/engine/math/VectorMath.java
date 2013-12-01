package engine.math;

public class VectorMath 
{
    
    public static Vector3f lerp(float x, Vector3f x1, Vector3f x2){
        Vector3f res = new Vector3f(0,0,0);
            res.setX(x1.getX() + (x2.getX() - x1.getX()) * x);
            res.setY(x1.getY() + (x2.getY() - x1.getY()) * x);
            res.setZ(x1.getZ() + (x2.getZ() - x1.getZ()) * x);
        return res;
    }
    
}
