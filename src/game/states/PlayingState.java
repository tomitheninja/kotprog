package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
import game.payable.Unit;
import game.payable.magic.Feltamasztas;
import game.payable.magic.Tűzlabda;
import game.payable.magic.VillamCsapas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayingState extends GameState {
    protected Hero hero;
    protected Hero otherHero;
    protected int manna = 5;
    protected ArrayList<Unit> units;
    protected Feltamasztas feltamasztas;
    protected Tűzlabda tuzlabda;
    protected VillamCsapas villamCsapas;
    protected int otherManna;
    protected Feltamasztas otherFeltamasztas;
    protected Tűzlabda otherTuzlabda;
    protected VillamCsapas otherVillamCsapas;

    public PlayingState(GameStateManager gsm, Hero hero, ArrayList<Unit> units, boolean feltamasztas, boolean tuzlabda, boolean villamCsapas) {
        super(gsm);
        this.hero = hero;
        this.units = units;
        if (feltamasztas) this.feltamasztas = new Feltamasztas();
        if (tuzlabda) this.tuzlabda = new Tűzlabda();
        if (villamCsapas) this.villamCsapas = new VillamCsapas();

        Random random = new Random();

        otherHero = new Hero(random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4));
    }

    public int getManna() {
        return manna;
    }

    @Override
    protected void loop() {

    }

    @Override
    protected void render(Graphics graphics) {
        // black background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);

        // player title
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Player", 10, 20);

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Manna: " + manna, 10, 45);
        graphics.drawString("Attack: " + hero.getAttack(), 10, 65);
        graphics.drawString("Defence: " + hero.getDefence(), 10, 80);
        graphics.drawString("Magic: " + hero.getMagic(), 10, 100);
        graphics.drawString("Knowledge: " + hero.getKnowledge(), 10, 120);
        graphics.drawString("Morale: " + hero.getMorale(), 10, 140);
        graphics.drawString("Luck: " + hero.getLuck(), 10, 160);
        int nextY = 180;
        if (tuzlabda != null) {
            graphics.drawString("Tűzlabda (" + tuzlabda.getMannaCost() + " manna)", 10, nextY);
            nextY += 20;
        }
        if (villamCsapas != null) {
            graphics.drawString("VillamCsapas (" + villamCsapas.getMannaCost() + " manna)", 10, nextY);
            nextY += 20;
        }
        if (feltamasztas != null) {
            graphics.drawString("Feltamasztás (" + feltamasztas.getMannaCost() + " manna)", 10, nextY);
            nextY += 20;
        }

        // enemy title
        nextY = 20;
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Enemy", WindowManager.WIDTH - 200, nextY);

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Manna: " + otherManna, WindowManager.WIDTH - 200, nextY + 20);
        graphics.drawString("Attack: " + otherHero.getAttack(), WindowManager.WIDTH - 200, nextY + 40);
        graphics.drawString("Defence: " + otherHero.getDefence(), WindowManager.WIDTH - 200, nextY + 55);
        graphics.drawString("Magic: " + otherHero.getMagic(), WindowManager.WIDTH - 200, nextY + 75);
        graphics.drawString("Knowledge: " + otherHero.getKnowledge(), WindowManager.WIDTH - 200, nextY + 95);
        graphics.drawString("Morale: " + otherHero.getMorale(), WindowManager.WIDTH - 200, nextY + 115);
        graphics.drawString("Luck: " + otherHero.getLuck(), WindowManager.WIDTH - 200, nextY + 135);
        nextY += 170;
        if (otherTuzlabda != null) {
            graphics.drawString("Tűzlabda (" + otherTuzlabda.getMannaCost() + " manna)", WindowManager.WIDTH - 200, nextY);
            nextY += 20;
        }
        if (otherVillamCsapas != null) {
            graphics.drawString("VillamCsapas (" + otherVillamCsapas.getMannaCost() + " manna)", WindowManager.WIDTH - 200, nextY);
            nextY += 20;
        }
        if (otherFeltamasztas != null) {
            graphics.drawString("Feltamasztás (" + otherFeltamasztas.getMannaCost() + " manna)", WindowManager.WIDTH - 200, nextY);
            nextY += 20;
        }

        // 12x12 grid
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                graphics.drawRect(196 + i * 32, 5 + j * 32, 32, 32);
            }
        }

        // instructions
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Use arrows to navigate, space or enter to select and escape or backspace to go back", 10, WindowManager.HEIGHT - 30);
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

    public static class Coordinate {
        protected int x;
        protected int y;

        public Coordinate(int x, int y) {
            setX(x);
            setY(y);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = Math.min(12, Math.max(0, x));
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = Math.min(12, Math.max(0, y));
        }


    }
}
