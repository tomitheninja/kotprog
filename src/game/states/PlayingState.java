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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PlayingState extends GameState {
    protected Hero hero;
    protected Hero otherHero;
    protected int manna = 5;
    protected ArrayList<UnitWithCoordinate> units = new ArrayList<>();
    protected Feltamasztas feltamasztas;
    protected Tűzlabda tuzlabda;
    protected Coordinate cursor = new Coordinate(0, 0);
    protected long tick = 0;
    protected VillamCsapas villamCsapas;
    protected int otherManna;
    protected Feltamasztas otherFeltamasztas;
    protected Tűzlabda otherTuzlabda;
    protected VillamCsapas otherVillamCsapas;
    public PlayingState(GameStateManager gsm, Hero hero, ArrayList<Unit> units, boolean feltamasztas, boolean tuzlabda, boolean villamCsapas) {
        super(gsm);
        this.hero = hero;
        int i = 0;
        for (Unit unit : units) {
            this.units.add(new UnitWithCoordinate(unit, new Coordinate(0, i++)));
        }
        if (feltamasztas) this.feltamasztas = new Feltamasztas();
        if (tuzlabda) this.tuzlabda = new Tűzlabda();
        if (villamCsapas) this.villamCsapas = new VillamCsapas();

        Random random = new Random();

        otherHero = new Hero(random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4), random.nextInt(1, 4));
    }

    protected boolean isCursorVisible() {
        return tick % 4 != 0;
    }

    public int getManna() {
        return manna;
    }

    @Override
    protected void loop() {
        tick = (tick + 1) % Integer.MAX_VALUE;
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

        // units
        for (UnitWithCoordinate unit : units) {
            graphics.setColor(new Color(0, 255, 0, 127));
            graphics.fillRect(196 + unit.coordinate.x * 32, 5 + unit.coordinate.y * 32, 32, 32);
        }

        // cursor
        if (isCursorVisible()) {
            graphics.setColor(new Color(255, 255, 0, 127));
            graphics.fillRect(196 + cursor.x * 32, 5 + cursor.y * 32, 32, 32);
        }

        // instructions
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Use arrows to navigate, space or enter to select and escape or backspace to go back", 10, WindowManager.HEIGHT - 30);

        // unit info
        for (UnitWithCoordinate unit : units) {
            if (unit.coordinate.equals(cursor)) {
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Arial", Font.BOLD, 12));
                graphics.drawString("Unit: " + unit.name + " hp: " + unit.getHealth(), 10, WindowManager.HEIGHT - 10);
            }
        }
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> cursor.setY(cursor.getY() - 1);
            case KeyEvent.VK_DOWN -> cursor.setY(cursor.getY() + 1);
            case KeyEvent.VK_LEFT -> cursor.setX(cursor.getX() - 1);
            case KeyEvent.VK_RIGHT -> cursor.setX(cursor.getX() + 1);
        }

    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public static class Coordinate {
        private int x;
        private int y;

        public Coordinate(int x, int y) {
            setX(x);
            setY(y);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = Math.min(11, Math.max(0, x));
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = Math.min(11, Math.max(0, y));
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    protected static class UnitWithCoordinate extends Unit {

        Coordinate coordinate;

        public UnitWithCoordinate(Unit unit, Coordinate coordinate) {
            super(unit);
            this.coordinate = coordinate;
        }
    }
}
