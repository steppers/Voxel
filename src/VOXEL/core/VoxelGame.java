package VOXEL.core;

import Scripts.*;
import engine.components.Camera;
import engine.components.DirectionalLightComponent;
import engine.core.Texture;
import engine.ext.GameObject;
import engine.ext.Scene;
import java.util.Random;

public class VoxelGame {

    public static final int CHUNK_SIZE_XZ = 16;
    public static final int CHUNK_SIZE_Y = 64;
    public static final float VOXEL_SIZE = 1f;
    private Random rand = new Random();
    public static long seed = 4123674254L;
    public static String texturePackName = "terrainv2.png";
    public static Texture terrain;
    public static Perlin perlin;
    GameObject player;
    GameObject sun;

    public VoxelGame() {
        seed = (long) (Math.random() * 6787623f);
        perlin = new Perlin(seed);

        terrain = new Texture(texturePackName);

        player = new GameObject();
        player.setTag("Camera");
        player.AddComponent(new Camera(player));
        player.AddComponent(new FPSCamera(player));
        player.AddComponent(new Player(player));
        player.getComponent(Camera.class).setPerspective(70, 0.01f, 1000f);
        player.getTransform().setPos(32, 100f, 32f);
        player.getTransform().setRotation(-45, 0, 0);
        Scene.addGameObject(player);

        sun = new GameObject();
        sun.setTag("Sun");
        sun.AddComponent(new DirectionalLightComponent(sun));
        sun.getTransform().setPos(0, 10, 0);
        sun.getTransform().setRotation(-78, 200, 0);
        Scene.addGameObject(sun);

        ChunkManager.init();
    }

    public void input() {
        Scene.input();
    }

    public void update() {
        Scene.update();
    }

    public void render() {
        Scene.render();
    }
}
