package framework.gamestates;

import framework.gui.WindowManager;

import java.awt.*;

public abstract class GameState {

    protected GameStateManager gameStateManager;

    protected GameState(GameStateManager manager) {
        this.gameStateManager = manager;
    }

    protected abstract void loop();

    protected void prerender(Graphics graphics) {
        graphics.clearRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);
        graphics.setColor(new Color(0, 0, 0));
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);
        render(graphics);
    }
    protected abstract void render(Graphics graphics);

    protected abstract void keyPressed(int keyCode);

    protected abstract void keyReleased(int keyCode);
}