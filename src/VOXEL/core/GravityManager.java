package VOXEL.core;

import engine.math.Vector3f;

public class GravityManager 
{
    public static final float TERMINAL_VEL = 22f;
    public static final float GRAVITY = 25f;
    
    public static void getGravity(int x, int y, int z){
        Vector3f grav = Vector3f.Y_AXIS;
        if(y > (VoxelGame.MAP_DIMENSION*VoxelGame.VOXEL_SIZE)-x && y > (VoxelGame.MAP_DIMENSION*VoxelGame.VOXEL_SIZE)-z && y > x && y > z){
            grav = new Vector3f(0,-GRAVITY,0);
        }
            
    }
    
}
