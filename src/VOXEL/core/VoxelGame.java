package VOXEL.core;

import Scripts.*;
import engine.components.*;
import engine.core.Texture;
import engine.ext.GameObject;
import engine.ext.Scene;
import engine.math.Vector3f;
import java.util.Random;

public class VoxelGame {

    public static final int CHUNK_SIZE_XZ = 16;
    public static final int CHUNK_SIZE_Y = 64;
    public static final int Y_DIMENSION = 1;
    public static final float VOXEL_SIZE = 1f;
    
    public static final float VIEW_DISTANCE = 90f;
    
    private Random rand = new Random();
    public static long seed = 4123674254L;
    public static String texturePackName = "terrainv2.png";
    public static Texture terrain;
    public static Perlin perlin;
    GameObject player;
    GameObject sun;
    Gun gun;

    public VoxelGame() {
        seed = (long) (Math.random() * 6787623f);
        perlin = new Perlin(seed);

        terrain = new Texture(texturePackName);

        player = new GameObject();
        player.setTag("Camera");
        player.AddComponent(new Camera(player));
        //player.AddComponent(new FreeFlyCamera(player));
        player.AddComponent(new FPSCamera(player));
        player.AddComponent(new Player(player));
        player.getComponent(Camera.class).setPerspective(70, 0.01f, 1000f);
        player.getTransform().setPos(32, (CHUNK_SIZE_Y*VOXEL_SIZE)+5f, 32f);
        Scene.addGameObject(player);

        sun = new GameObject();
        sun.setTag("Sun");
        sun.AddComponent(new DirectionalLightComponent(sun));
        sun.getTransform().setPos(0, 10, 0);
        sun.getTransform().setRotation(-78, 200, 0);
        Scene.addGameObject(sun);
        
        gun = new Gun("test.spr");
        player.addChild(gun);
        Scene.addGameObject(gun);

        ChunkManager.init();
    }

    public void input() {
        Scene.input();
    }

    public void update() {
        Scene.update();
        //ChunkManager.update();
    }

    public void render() {
        Scene.render();
    }
}
