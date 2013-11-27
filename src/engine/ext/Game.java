package engine.ext;

import VOXEL.core.VoxelGame;

public class Game {
    VoxelGame game;

    public Game() {
        game = new VoxelGame();
    }

    public void input() {
        game.input();
    }

    public void update() {
        game.update();
    }

    public void render() {
        game.render();
    }

    public void CleanUp() {
    }
}