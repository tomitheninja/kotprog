package framework.gamestates;

import java.awt.Graphics;
import java.util.EmptyStackException;
import java.util.Stack;

public class GameStateManager {

    private final Stack<GameState> states;

    public GameStateManager() {
        this.states = new Stack<>();
    }

    public void stackState(GameState state) {
        this.states.add(state);
    }

    public void backToPreviousState() {
        this.states.pop();
    }

    public void clearStack() {
        this.states.clear();
    }

    public void loop() {
        if (this.states.isEmpty()) {
            System.err.println("[GameStateManager]: Error! GameState stack is empty!");
            System.exit(-1);
        }
        this.states.peek().loop();
    }


    public void render(Graphics graphics) {
        if (this.states.isEmpty()) {
            System.err.println("[GameStateManager]: Error! GameState stack is empty!");
            System.exit(-1);
        }
        this.states.peek().prerender(graphics);
    }


    public void keyPressed(int keyCode) {
        try {
            this.states.peek().keyPressed(keyCode);
        } catch (EmptyStackException e) {
            System.err.println("[GameStateManager]: Error! GameState stack is empty!");
            System.exit(-1);
        }
    }

    public void keyReleased(int keyCode) {
        try {
            this.states.peek().keyReleased(keyCode);
        } catch (EmptyStackException e) {
            System.err.println("[GameStateManager]: Error! GameState stack is empty!");
            System.exit(-1);
        }
    }
}