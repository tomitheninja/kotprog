package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
import game.payable.Magic;
import game.payable.Unit;
import game.util.BoardLocation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlayingState extends GameState {
    // respect the lifetime of unit by using coordinate to look up the unit
    protected BoardLocation selectedCoordinate;
    protected Hero hero;
    protected Hero otherHero;
    protected int manna = 5;
    protected ArrayList<UnitOnBoard> units = new ArrayList<>();
    protected BoardLocation cursor = new BoardLocation(0, 0);
    protected long tick = 0;
    private Set<Unit.Type> movedUnits = new HashSet<>();

    public PlayingState(GameStateManager gsm, Hero hero, ArrayList<Unit> units) {
        super(gsm);
        this.hero = hero;
        int i = 0;
        for (Unit unit : units) {
            this.units.add(new UnitOnBoard(unit, new BoardLocation(0, 4 + i++)));
        }

        otherHero = new Hero();
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
        UnitOnBoard u = units.stream().filter(unit -> unit.coordinate.equals(selectedCoordinate)).findFirst().orElse(null);


        // black background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);

        // player title
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Player", 10, 20);

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Manna: " + manna, 10, 45);
        graphics.drawString("Attack: " + hero.attack, 10, 65);
        graphics.drawString("Defence: " + hero.defence, 10, 80);
        graphics.drawString("Magic: " + hero.magic, 10, 100);
        graphics.drawString("Knowledge: " + hero.knowledge, 10, 120);
        graphics.drawString("Morale: " + hero.morale, 10, 140);
        graphics.drawString("Luck: " + hero.luck, 10, 160);
        int nextY = 180;
        for (Magic m : hero.magics) {
            graphics.drawString(m.name, 10, nextY);
            nextY += 20;
        }

        // enemy title
        nextY = 20;
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Enemy", WindowManager.WIDTH - 200, nextY);

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Manna: " + otherHero.getManna(), WindowManager.WIDTH - 200, nextY + 20);
        graphics.drawString("Attack: " + otherHero.attack, WindowManager.WIDTH - 200, nextY + 40);
        graphics.drawString("Defence: " + otherHero.defence, WindowManager.WIDTH - 200, nextY + 55);
        graphics.drawString("Magic: " + otherHero.magic, WindowManager.WIDTH - 200, nextY + 75);
        graphics.drawString("Knowledge: " + otherHero.knowledge, WindowManager.WIDTH - 200, nextY + 95);
        graphics.drawString("Morale: " + otherHero.morale, WindowManager.WIDTH - 200, nextY + 115);
        graphics.drawString("Luck: " + otherHero.luck, WindowManager.WIDTH - 200, nextY + 135);


        // units
        UnitOnBoard hoveredUnit = null;
        for (UnitOnBoard unit : units) {
            graphics.drawImage(unit.img.getImage(), 196 + unit.coordinate.getX() * 32, 5 + unit.coordinate.getY() * 32, 32, 32, null);
            graphics.setColor(new Color(0, 255, 0, 127));
            graphics.fillRect(196 + unit.coordinate.getX() * 32, 32 + 5 + unit.coordinate.getY() * 32 - 4, 32 * unit.getHealth() / unit.maxHealth, 4);
            if (cursor.equals(unit.coordinate)) hoveredUnit = unit;
        }

        // areas where unit can move
        if (selectedCoordinate != null && u != null) {
            graphics.setColor(new Color(255, 255, 255, 64));
            for (int x = 0; x < 12; x++) {
                for (int y = 0; y < 12; y++) {
                    BoardLocation loc = new BoardLocation(x, y);
                    if (u.canReach(loc) && units.stream().noneMatch(unit -> unit.coordinate.equals(loc))) {
                        graphics.fillRect(196 + x * 32, 5 + y * 32, 32, 32);
                    }
                }
            }

        }

        // cursor
        if (isCursorVisible()) {
            graphics.setColor(new Color(255, 255, 0, 96));
            graphics.fillRect(196 + cursor.getX() * 32, 5 + cursor.getY() * 32, 32, 32);
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
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        if (u == null) {
            graphics.drawString("Use arrows to navigate, space or enter to select and escape to undo selection", 10, WindowManager.HEIGHT - 30);
        } else {
            graphics.drawString("Use arrows then space to move or attack. Press space again to use special action. Or select an other unit", 10, WindowManager.HEIGHT - 30);
            // TODO: enemy units
            graphics.drawString("Unit: " + u, 10, WindowManager.HEIGHT - 10);
        }
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> cursor.setY(cursor.getY() - 1);
            case KeyEvent.VK_DOWN -> cursor.setY(cursor.getY() + 1);
            case KeyEvent.VK_LEFT -> cursor.setX(cursor.getX() - 1);
            case KeyEvent.VK_RIGHT -> cursor.setX(cursor.getX() + 1);
            case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                // selected unit
                UnitOnBoard selected = units.stream().filter(unit -> unit.coordinate.equals(selectedCoordinate)).findFirst().orElse(null);
                // unit under the cursor
                UnitOnBoard hovered = units.stream().filter(unit -> unit.coordinate.equals(cursor)).findFirst().orElse(null);

                // set selected unit
                if (selected == null && hovered != null)
                    selectedCoordinate = new BoardLocation(cursor);

                // clear selection. Not sure if this is actually needed
                if (selected == null && hovered == null)
                    selectedCoordinate = null;

                // move unit
                if (selected != null && hovered == null && selected.canReach(cursor)) {
                    if (!movedUnits.contains(selected.type)) {
                        selected.coordinate = new BoardLocation(cursor);
                        movedUnits.add(selected.type);
                    }  else {
                        System.out.println("Unit already moved this turn");
                    }
                }

                if (selected != null && hovered != null) {
                    if (hovered.coordinate.equals(selectedCoordinate)) {
                        // special action
                        selected.specialAction.action();
                        selectedCoordinate = null;
                    } else {
                        // change unit selection
                        if (units.stream().anyMatch(unit -> unit.coordinate.equals(cursor)))
                            selectedCoordinate = new BoardLocation(cursor);
                    }
                }
                ;
            }
            case KeyEvent.VK_ESCAPE -> selectedCoordinate = null;
        }

    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }


    protected static class UnitOnBoard extends Unit {

        BoardLocation coordinate;

        public UnitOnBoard(Unit unit, BoardLocation coordinate) {
            super(unit);
            this.coordinate = coordinate;
        }

        public boolean canReach(BoardLocation target) {
            int x = coordinate.getX();
            int y = coordinate.getY();
            int tx = target.getX();
            int ty = target.getY();
            int dx = Math.abs(x - tx);
            int dy = Math.abs(y - ty);
            return dx + dy <= movement;
        }
    }
}
