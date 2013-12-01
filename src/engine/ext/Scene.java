package engine.ext;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class Scene {

    private static ArrayList<GameObject> gameObjects = new ArrayList<>();

    public static void addGameObject(GameObject g) {
        gameObjects.add(g);
    }

    public static void destroyGameObject(GameObject g) {
        g.destroy();
        gameObjects.remove(g);
    }

    public static void input() {
        for (GameObject g : gameObjects) {
            g.input();
        }
    }

    public static void update() {
        for (GameObject g : gameObjects) {
            g.update();
        }
    }

    public static void render() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        for (GameObject g : gameObjects) {
            g.render();
        }
        for (GameObject g : gameObjects) {
            g.getTransform().resetAnimations();
        }
    }

    public static GameObject FindGameObjectWithTag(String tag) {
        for (GameObject g : gameObjects) {
            if (g.getTag().equals(tag)) {
                return g;
            } else if (g.getChildWithTag(tag) != null) {
                return g.getChildWithTag(tag);
            }
        }
        return null;
    }
    
    public static ArrayList<GameObject> FindGameObjectsWithTag(String tag) {
        ArrayList<GameObject> gos = new ArrayList<>();
        for (GameObject g : gameObjects) {
            if (g.getTag().equals(tag)) {
                gos.add(g);
            } else if (g.getChildWithTag(tag) != null) {
                gos.add(g.getChildWithTag(tag));
            }
        }
        return null;
    }
}
