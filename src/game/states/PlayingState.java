package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;

import java.awt.*;

public class PlayingState extends GameState {

    private final int gold;

    public PlayingState(GameStateManager gsm, int initialGoldAmount) {
        super(gsm);
        gold = initialGoldAmount;

    }

    @Override
    protected void loop() {

    }

    @Override
    protected void render(Graphics graphics) {

    }

    @Override
    protected void keyPressed(int keyCode) {

    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
