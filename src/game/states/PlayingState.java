package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
import game.payable.Unit;
import game.util.BoardLocation;
import game.util.Team;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PlayingState extends GameState {
    private final Set<Unit.Type> movedUnits = new HashSet<>();
    protected Hero hero;
    protected Hero otherHero;
    protected ArrayList<UnitOnBoard> units = new ArrayList<>();
    // TODO: protected ArrayList<UnitOnBoard> enemyUnits = new ArrayList<>();
    protected BoardLocation cursor = new BoardLocation(0, 0);
    protected long tick = 0;
    InstructionAlert alert;
    UnitOnBoard current;
    UnitOnBoard hovered;
    BoardLocation[] obstacles;

    public PlayingState(GameStateManager gsm, Hero hero, Hero enemyHero, ArrayList<Unit> units, ArrayList<Unit> enemyUnits) {
        super(gsm);
        this.hero = hero;
        int i = 0;
        for (Unit unit : units) {
            this.units.add(new UnitOnBoard(unit, new BoardLocation(0, 4 + i++), Team.PLAYER));
        }
        for (Unit unit : enemyUnits) {
            this.units.add(new UnitOnBoard(unit, new BoardLocation(11, 4 + i++), Team.ENEMY));
        }
        otherHero = enemyHero;
    }

    protected boolean isCursorVisible() {
        return tick % 4 != 0;
    }

    @Override
    protected void loop() {
        if (units.stream().allMatch(unit -> movedUnits.contains(unit.type))) {
            alert = new InstructionAlert("A new round has started", 50);
            movedUnits.clear();
        }
        obstacles = this.units.stream().map(unit -> unit.coordinate).toArray(BoardLocation[]::new);
        current = units.stream()
                .filter(unit -> unit.team == Team.PLAYER)
                .filter(unit -> !movedUnits.contains(unit.type))
                .max(Comparator.comparing(UnitOnBoard::getInitiative))
                .orElse(null);
        if (current == null) {
            System.out.println("Game over!");
            System.exit(0);
        }
        if (this.units.stream().noneMatch(unit -> unit.team == Team.ENEMY)) {
            System.out.println("You won!");
            System.exit(0);
        }
        hovered = units.stream()
                .filter(unit -> unit.coordinate.equals(cursor))
                .findFirst().orElse(null);
        tick = (tick + 1) % Integer.MAX_VALUE;
        if (alert != null) {
            if (alert.isDone()) alert = null;
            else alert.tick();
        }
    }

    @Override
    protected void render(Graphics graphics) {
        if (current == null) return; // waiting for loop()
        drawBackground(graphics);
        drawHero(graphics);
        drawEnemyHero(graphics);
        drawUnits(graphics);
        drawReachableFields(graphics);
        if (isCursorVisible()) drawCursor(graphics);
        drawBoard(graphics);
        drawCurrentUnit(graphics);
        drawInstructions(graphics);
        if (hovered != null) drawUnitStats(graphics, hovered);
    }

    private void drawCurrentUnit(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.drawRect(196 + current.coordinate.getX() * 32, 5 + current.coordinate.getY() * 32, 32, 32 - 4);
    }

    private void drawUnitStats(Graphics graphics, UnitOnBoard unit) {
        if (unit == null) throw new NullPointerException("Unit must be defined");
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        graphics.drawString((unit.team == Team.ENEMY ? "Enemy u" : "U") + "nit: " + unit, 10, WindowManager.HEIGHT - 10);
    }

    private void drawInstructions(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        String txt = "";
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        if (alert != null) txt = alert.text;
        else txt = "Use arrows then space to move or attack. Press enter to use special action. Or press Q to skip";
        graphics.drawString(txt, 10, WindowManager.HEIGHT - 30);
    }

    private void drawBoard(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < BoardLocation.WIDTH; i++) {
            for (int j = 0; j < BoardLocation.HEIGHT; j++) {
                graphics.drawRect(196 + i * 32, 5 + j * 32, 32, 32);
            }
        }
    }

    private void drawCursor(Graphics graphics) {
        graphics.setColor(new Color(255, 255, 0, 96));
        graphics.fillRect(196 + cursor.getX() * 32, 5 + cursor.getY() * 32, 32, 32);
    }

    private void drawReachableFields(Graphics graphics) {
        graphics.setColor(new Color(255, 255, 255, 48));
        for (int x = 0; x < BoardLocation.WIDTH; x++) {
            for (int y = 0; y < BoardLocation.HEIGHT; y++) {
                BoardLocation loc = new BoardLocation(x, y);
                boolean noFriendlyUnitHere = units.stream()
                        .filter(unit -> unit.team == Team.PLAYER)
                        .noneMatch(unit -> unit.coordinate.equals(loc));
                if (current.canReach(loc, obstacles) && noFriendlyUnitHere || current.coordinate.equals(loc)) {
                    graphics.fillRect(196 + x * 32, 5 + y * 32, 32, 32);
                }
            }
        }
    }

    private void drawUnits(Graphics graphics) {
        graphics.setFont(new Font("Arial", Font.BOLD, 12));
        for (UnitOnBoard unit : units) {
            final int topLeftX = 196 + unit.coordinate.getX() * 32;
            final int topLeftY = 5 + unit.coordinate.getY() * 32;
            graphics.drawImage(unit.img.getImage(), topLeftX, topLeftY, 32, 32, null);
            // health bar
            graphics.setColor(unit.team == Team.ENEMY ? new Color(255, 0, 0, 127) : new Color(0, 255, 0, 127));
            graphics.fillRect(topLeftX, topLeftY + 32 - 4, 32 * unit.getHealth() / unit.maxHealth, 4);
        }
    }

    private void drawEnemyHero(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Enemy", WindowManager.WIDTH - 200, 20);

        drawAbstractHero(graphics, otherHero, WindowManager.WIDTH - 200, 45);
    }

    private void drawAbstractHero(Graphics graphics, Hero hero, int x, int y) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("Manna: " + hero.getManna(), x, y);
        graphics.drawString("Attack: " + hero.attack, x, y + 20);
        graphics.drawString("Defence: " + hero.defence, x, y + 40);
        graphics.drawString("Magic: " + hero.magic, x, y + 60);
        graphics.drawString("Knowledge: " + hero.knowledge, x, y + 80);
        graphics.drawString("Morale: " + hero.morale, x, y + 100);
        graphics.drawString("Luck: " + hero.luck, x, y + 120);
    }

    private void drawHero(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Player", 10, 20);

        drawAbstractHero(graphics, hero, 10, 45);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("(1) Attack: " + hero.attack * 10, 10, 200);
        int nextY = 220;
        for (int i = 0; i < hero.magics.length; i++) {
            graphics.drawString("(" + (i + 2) + ") " + hero.magics[i].name, 10, nextY);
            nextY += 20;
        }
    }

    private void drawBackground(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_Q -> {
                movedUnits.add(current.type);
            }
            case KeyEvent.VK_UP -> cursor.setY(cursor.getY() - 1);
            case KeyEvent.VK_DOWN -> cursor.setY(cursor.getY() + 1);
            case KeyEvent.VK_LEFT -> cursor.setX(cursor.getX() - 1);
            case KeyEvent.VK_RIGHT -> cursor.setX(cursor.getX() + 1);
            case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                // TODO: has selected unit & cursor above enemy unit & dist(a - b) == 1 -> attack
                if (hovered != null) {
                    if (hovered.coordinate.equals(current.coordinate)) {
                        current.specialAction.action();
                    } else if (hovered.team == Team.ENEMY) {
                        if (current.coordinate.isNeighbor(hovered.coordinate)) {
                            boolean isCriticalDamage = fight(current, hovered, hero, otherHero);
                            if (isCriticalDamage)
                                alert = new InstructionAlert("Critical attack!", 50);
                            if (current.isAlive()) this.movedUnits.add(current.type);
                            else current = null;
                        } else {
                            alert = new InstructionAlert("Out of range!", 50);
                        }
                    }
                } else if (movedUnits.contains(current.type)) {
                    alert = new InstructionAlert("Unit already moved", 50);
                } else if (current.canReach(cursor, obstacles)) {
                    current.coordinate = new BoardLocation(cursor);
                    movedUnits.add(current.type);
                } else {
                    alert = new InstructionAlert("Can't move there", 50);
                }
            }
            //        case KeyEvent.VK_ESCAPE -> selectedCoordinate = null;
        }

    }

    private boolean fight(UnitOnBoard attacker, UnitOnBoard defender, Hero attackerHero, Hero defenderHero) {
        boolean isCriticalDamage = 100 * Math.random() < attackerHero.luck;
        double attackModifier = (1 + attackerHero.attack * 0.1) * (1 - defenderHero.defence * 0.05) * (isCriticalDamage ? 2 : 1);
        int damage = (int) (attacker.getAttack() * attackModifier);
        defender.takeDamage(damage);
        if (defender.isAlive()) {
            double defenderAttackModifier = (1 + defenderHero.attack * 0.1) * (1 - attackerHero.defence * 0.05);
            int defenderDamage = (int) (defender.getAttack() * defenderAttackModifier);
            attacker.takeDamage(defenderDamage);
            if (!attacker.isAlive()) {
                System.out.println("Attacker unit died");
                this.units.remove(attacker);
            }
        } else {
            System.out.println("defender unit died");
            this.units.remove(defender);
            attacker.coordinate = new BoardLocation(defender.coordinate);
        }
        return isCriticalDamage;
    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private static class InstructionAlert {
        public final String text;
        public final int numTicks;
        protected int ticks;

        public InstructionAlert(String text, int numTicks) {
            this.numTicks = numTicks;
            this.text = text;
            this.ticks = 0;
        }

        public void tick() {
            ticks++;
        }

        public boolean isDone() {
            return ticks >= numTicks;
        }
    }

    protected static class UnitOnBoard extends Unit {


        BoardLocation coordinate;
        Team team;

        public UnitOnBoard(Unit unit, BoardLocation coordinate, Team team) {
            super(unit);
            if (coordinate == null) throw new IllegalArgumentException("coordinate can't be null");
            if (team == null) throw new IllegalArgumentException("team can't be null");
            this.coordinate = coordinate;
            this.team = team;
        }

        public boolean canReach(BoardLocation target, BoardLocation[] obstacles) {
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
