package engine.core;

import engine.math.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderUtil {

    //Clears the color buffer and depth buffer
    public static void clearScreen() {
        //TODO: Stencil Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    //Enable texture rendeing within the engine
    public static void setTextures(boolean enabled) {
        if (enabled) {
            glEnable(GL_TEXTURE_2D);
        } else {
            glDisable(GL_TEXTURE_2D);
        }
    }

    //unbinds the current texture
    public static void unbindTextures() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    //Sets the rendering clear color
    public static void setClearColor(Vector3f color) {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
    }

    //Initialises OpenGL
    public static void initGraphics() {
        glClearColor(0.5f, 0.5f, 0.7f, 0.0f);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_DEPTH_CLAMP);

        glEnable(GL_TEXTURE_2D);
    }

    //Retrieves the current OpenGL Version
    public static String getOpenGLVersion() {
        return glGetString(GL_VERSION);
    }
}
