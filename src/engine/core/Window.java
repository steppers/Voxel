package engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
    
    public static int width = 1366;
    public static int height = 768;

    public static void createWindow(String title) {
        Display.setTitle(title);
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
    
    public static void setFullscreen(boolean fs){
        DisplayMode mode = Display.getDesktopDisplayMode();
        if(fs){
            try {
                Display.setFullscreen(true);
                Display.setDisplayMode(mode);
                Display.setResizable(false);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Display.setFullscreen(false);
                Display.setDisplayMode(new DisplayMode(width, height));
                Display.setResizable(true);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void update(){
        width = Display.getWidth();
        height = Display.getHeight();
    }

    public static void render() {
        Display.update();
    }

    public static void dispose() {
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    public static boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public static int getWidth() {
        return Display.getDisplayMode().getWidth();
    }

    public static int getHeight() {
        return Display.getDisplayMode().getHeight();
    }

    public static String getTitle() {
        return Display.getTitle();
    }
}
