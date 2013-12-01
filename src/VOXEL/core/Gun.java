package VOXEL.core;

import engine.components.Animator;
import engine.math.Vector3f;

public class Gun extends VoxelSprite
{
    
    public Gun(String fileName)
    {
        super(new Vector3f(-0.2f, -0.5f, 0.2f), 16, fileName);
        Animator gunAnimator = new Animator(this);
        gunAnimator.addAnimation("guny", "guny.anim");
        gunAnimator.addAnimation("gunx", "gunx.anim");
        gunAnimator.setLooping("guny", true);
        gunAnimator.setLooping("gunx", true);
        gunAnimator.setSpeed("guny", 0.75f);
        gunAnimator.setSpeed("gunx", 0.25f);
        gunAnimator.play("guny");
        gunAnimator.play("gunx");
        this.AddComponent(gunAnimator);
    }
    
}
