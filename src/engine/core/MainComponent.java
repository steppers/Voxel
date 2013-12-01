package engine.core;

import engine.ext.Game;

public class MainComponent {

    //Window resolution
    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;
    
    //Window Title
    public static final String TITLE = "Voxel Engine";
    
    //Frame Limit
    public static final double FRAME_CAP = 60.0;
    
    //true when the loop is iterating
    private boolean isRunning;
    
    //game instance
    private Game game;

    //Constructor
    public MainComponent() {
        System.out.println(RenderUtil.getOpenGLVersion());
        RenderUtil.initGraphics();
        isRunning = false;
        game = new Game();
    }

    //Begins Application loop
    public void start() {
        if (isRunning) {
            return;
        }

        run();
    }

    //Stops Application loop
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
    }

    //Game loop
    private void run() {
        isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1.0 / FRAME_CAP;

        long lastTime = Time.getTime();
        double unprocessedTime = 0;

        while (isRunning) {
            Window.update();
            boolean render = false;

            long startTime = Time.getTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) Time.SECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;

                if (Window.isCloseRequested()) {
                    stop();
                }

                Time.setDelta(frameTime);

                game.input();
                Input.update();

                game.update();

                if (frameCounter >= Time.SECOND) {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                render();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    //Called for rendering
    private void render() {
        RenderUtil.clearScreen();
        game.render();
        Window.render();
    }

    //Disposes of the window when application closed
    private void cleanUp() {
        Window.dispose();
    }

    public static void main(String[] args) {
        VersionTracker.UpdateVersionData();
        Window.createWindow(TITLE + " | Build No: " + VersionTracker.version);
        Window.setFullscreen(false);

        MainComponent game = new MainComponent();

        game.start();
    }
}
