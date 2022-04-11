package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class MainMenu extends GameState {
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color PRIMARY_COLOR = new Color(0, 255, 0);
    private static final Color ACTIVE_COLOR = new Color(0, 0, 255);

    private static final String[] DIFFICULTIES = new String[]{"easy", "normal", "hard"};

    private int selected = 1;

    public MainMenu(GameStateManager manager) {
        super(manager);
    }

    @Override
    protected void loop() {
    }

    @Override
    protected void render(Graphics graphics) {
        final int W = WindowManager.WIDTH;
        final int H = WindowManager.HEIGHT;
        final int HALF_W = W / 2;
        final int HALF_H = H / 2;


        final String TITLE = "Heroes";
        final String DIFFICULTY = "Choose difficulty";
        final String START = "Start";
        final String INSTRUCTIONS = "Use the arrow keys to change the difficulty and press enter to start the game";

        // black background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, W, H);

        // title
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(new Font("Arial", Font.PLAIN, 25));
        graphics.drawString(TITLE, HALF_W - graphics.getFontMetrics().stringWidth(TITLE) / 2, H / 3 - graphics.getFontMetrics().getHeight() / 2);

        // difficulty
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString(DIFFICULTY, HALF_W - graphics.getFontMetrics().stringWidth(DIFFICULTY) / 2, HALF_H - graphics.getFontMetrics().getHeight());

        // draw difficulty options
        int longest = 0;
        for (String s : DIFFICULTIES) {
            if (graphics.getFontMetrics().stringWidth(s) > longest) longest = graphics.getFontMetrics().stringWidth(s);
        }
        for (int i = 0; i < DIFFICULTIES.length; i++) {
            graphics.setColor(i == this.selected ? ACTIVE_COLOR : PRIMARY_COLOR);
            final int x = HALF_W - longest * DIFFICULTIES.length / 2 + longest * i + longest / 2 - graphics.getFontMetrics().stringWidth(DIFFICULTIES[i]) / 2;
            final int y = HALF_H + graphics.getFontMetrics().getHeight() / 2;
            graphics.drawString(DIFFICULTIES[i], x, y);
        }

        // start
        graphics.setColor(ACTIVE_COLOR);
        graphics.setFont(new Font("Arial", Font.PLAIN, 22));
        graphics.drawString(START, HALF_W - graphics.getFontMetrics().stringWidth(START) / 2, H * 2 / 3 + graphics.getFontMetrics().getHeight() * 2);

        // instructions
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(new Font("Arial", Font.BOLD, 15));

        graphics.drawString(INSTRUCTIONS, HALF_W - graphics.getFontMetrics().stringWidth(INSTRUCTIONS) / 2, H - graphics.getFontMetrics().getHeight() / 2);
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (this.selected < DIFFICULTIES.length - 1) this.selected++;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (this.selected > 0) this.selected--;
                break;
            case KeyEvent.VK_ENTER:
                super.gameStateManager.stackState(new ShopState(super.gameStateManager, 700 + (2 - selected) * 300));
                break;
        }
    }

    @Override
    protected void keyReleased(int keyCode) {
    }
}