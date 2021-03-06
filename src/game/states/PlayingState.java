package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
import game.payable.Magic;
import game.payable.Unit;
import game.util.BoardLocation;
import game.util.InstructionAlert;
import game.util.Team;
import game.util.UnitOnBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class PlayingState extends GameState {

    int numRound = 1;
    protected Hero hero;
    protected Hero enemyHero;
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
        this.enemyHero = enemyHero;
        int i = 0;
        for (Unit unit : units) {
            this.units.add(new UnitOnBoard(unit, new BoardLocation(0, 4 + i++), Team.PLAYER, hero));
        }
        for (Unit unit : enemyUnits) {
            this.units.add(new UnitOnBoard(unit, new BoardLocation(11, 4 + i++), Team.ENEMY, enemyHero));
        }
    }

    protected boolean isCursorVisible() {
        return tick % 4 != 0;
    }

    @Override
    protected void loop() {
        // game over
        if (this.units.stream().noneMatch(UnitOnBoard::isPlayer)) {
            JOptionPane.showMessageDialog(null, "Game over!");
            System.exit(0);
        }
        if (this.units.stream().noneMatch(UnitOnBoard::isEnemy)) {
            JOptionPane.showMessageDialog(null, "You won!");
            System.exit(0);
        }

        obstacles = this.units.stream().map(unit -> unit.coordinate).toArray(BoardLocation[]::new);
        current = units.stream()
                .filter(UnitOnBoard::isPlayer)
                .filter(UnitOnBoard::canMove)
                .max(Comparator.comparingInt(UnitOnBoard::getInitiative)).orElse(null);

        UnitOnBoard enemyUnit = this.units.stream()
                .filter(UnitOnBoard::isEnemy)
                .filter(UnitOnBoard::canMove)
                .filter(unit -> current == null || unit.getInitiative() > current.getInitiative())
                .max(Comparator.comparing(UnitOnBoard::getInitiative))
                .orElse(null);
        if (enemyUnit != null) {
            UnitOnBoard toAttack = this.units.stream()
                    .filter(UnitOnBoard::isPlayer)
                    .filter(unit -> unit.coordinate.isNeighbor(enemyUnit.coordinate))
                    .findFirst().orElse(null);
            if (toAttack != null) {
                boolean isCriticalDamage = UnitOnBoard.fight(enemyUnit, toAttack, enemyHero, hero, true);
                if (isCriticalDamage) alert = new InstructionAlert("Critical attack!", 50);
                if (!enemyUnit.isAlive()) {
                    this.units.remove(enemyUnit);
                    enemyUnit.coordinate = new BoardLocation(toAttack.coordinate);
                }
                if (!toAttack.isAlive()) this.units.remove(toAttack);
            } else {
                // step next to archer
                enemyUnit.moved = true;
            }
            return;
        }

        if (units.stream().noneMatch(UnitOnBoard::canMove)) {
            units.forEach(unit -> unit.moved = false);
            alert = new InstructionAlert("A new round has started", 50);
            numRound++;
        }

        hovered = units.stream().filter(unit -> unit.coordinate.equals(cursor)).findFirst().orElse(null);
        tick = (tick + 1) % Integer.MAX_VALUE;
        if (alert != null) {
            if (alert.isDone()) alert = null;
            else alert.tick();
        }
    }

    @Override
    protected void render(Graphics graphics) {
        drawBackground(graphics);
        drawHero(graphics);
        drawEnemyHero(graphics);
        drawUnits(graphics);
        if (current == null) return; // waiting for loop()
        drawReachableFields(graphics);
        if (isCursorVisible()) drawCursor(graphics);
        drawBoard(graphics);
        drawCurrentUnit(graphics);
        drawInstructions(graphics);
        if (hovered != null) drawUnitStats(graphics, hovered);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.setColor(Color.WHITE);
        graphics.drawString("ROUND: " + numRound, WindowManager.WIDTH - 100, WindowManager.HEIGHT - 20);
    }

    private void drawCurrentUnit(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.drawRect(196 + current.coordinate.getX() * 32, 5 + current.coordinate.getY() * 32, 32, 32 - 4);
    }

    private void drawUnitStats(Graphics graphics, UnitOnBoard unit) {
        if (unit == null) throw new NullPointerException("Unit must be defined");
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        graphics.drawString((unit.isEnemy() ? "Enemy u" : "U") + "nit: " + unit, 10, WindowManager.HEIGHT - 10);
    }

    private void drawInstructions(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        String txt;
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        if (alert != null) txt = alert.text;
        else txt = "Use arrows then space to move or attack. Or press Q to skip";
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
                        .filter(UnitOnBoard::isPlayer)
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
            graphics.drawImage(unit.getImage(), topLeftX, topLeftY, 32, 32, null);
            // health bar
            graphics.setColor(unit.isPlayer() ? new Color(0, 255, 0, 127) : new Color(255, 0, 0, 127));
            graphics.fillRect(topLeftX, topLeftY + 32 - 4, 32 * unit.getHealth() / unit.getMaxHealth(), 4);
            // initiative
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.PLAIN, 10));
            graphics.drawString(unit.getInitiative() + "", topLeftX + 2, topLeftY + 32 - 2);
        }
    }

    private void drawEnemyHero(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Enemy", WindowManager.WIDTH - 200, 20);

        drawAbstractHero(graphics, enemyHero, WindowManager.WIDTH - 200, 45);
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
            graphics.drawString("(" + (i + 2) + ") " + hero.magics[i].type, 10, nextY);
            nextY += 20;
        }
    }

    private void drawBackground(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);
    }

    @Override
    protected void keyPressed(int keyCode) {
        if (current == null) return;
        Magic m = null;
        switch (keyCode) {
            case KeyEvent.VK_Q -> current.moved = true;
            case KeyEvent.VK_UP -> cursor.setY(cursor.getY() - 1);
            case KeyEvent.VK_DOWN -> cursor.setY(cursor.getY() + 1);
            case KeyEvent.VK_LEFT -> cursor.setX(cursor.getX() - 1);
            case KeyEvent.VK_RIGHT -> cursor.setX(cursor.getX() + 1);
            case KeyEvent.VK_SPACE -> {
                if (hovered != null) {
                    if (hovered.isEnemy()) {
                        if (current.coordinate.isNeighbor(hovered.coordinate)) {
                            boolean isCriticalDamage = UnitOnBoard.fight(current, hovered, hero, enemyHero, true);
                            if (isCriticalDamage) alert = new InstructionAlert("Critical attack!", 50);
                            if (current.isAlive()) current.moved = true;
                            else {
                                this.units.remove(current);
                                current = null;
                            }
                            if (!hovered.isAlive()) {
                                current.coordinate = new BoardLocation(hovered.coordinate);
                                this.units.remove(hovered);
                            }
                        } else if (current.getType() == Unit.Type.IJASZ) {
                            boolean noEnemyNeighbor = units.stream()
                                    .filter(UnitOnBoard::isEnemy)
                                    .noneMatch(unit -> unit.coordinate.isNeighbor(current.coordinate));
                            if (noEnemyNeighbor) {
                                boolean isCriticalDamage = UnitOnBoard.fight(current, hovered, hero, enemyHero, hovered.getType() == Unit.Type.IJASZ);
                                if (isCriticalDamage) alert = new InstructionAlert("Critical attack!", 50);
                                if (!current.isAlive()) this.units.remove(current);
                                if (!hovered.isAlive()) this.units.remove(hovered);
                            } else {
                                alert = new InstructionAlert("Enemy nearby!", 50);
                            }
                        } else alert = new InstructionAlert("Out of range!", 50);
                    }
                } else if (current.canReach(cursor, obstacles)) {
                    current.coordinate = new BoardLocation(cursor);
                    current.moved = true;
                } else {
                    alert = new InstructionAlert("Can't move there", 50);
                }
            }
            case KeyEvent.VK_1 -> {
                if (hovered == null || hovered.isPlayer()) {
                    alert = new InstructionAlert("Can't attack here", 50);
                } else {
                    hovered.takeDamage(hero.attack * 10);
                    if (!hovered.isAlive()) this.units.remove(hovered);
                    this.current.moved = true;
                }
            }
            case KeyEvent.VK_2 -> {
                if (hero.magics.length > 0) m = hero.magics[0];
                else alert = new InstructionAlert("No magic!", 50);
            }
            case KeyEvent.VK_3 -> {
                if (hero.magics.length > 1) m = hero.magics[1];
                else alert = new InstructionAlert("No magic!", 50);
            }
            case KeyEvent.VK_4 -> {
                if (hero.magics.length > 2) m = hero.magics[2];
                else alert = new InstructionAlert("No magic!", 50);
            }
            case KeyEvent.VK_5 -> {
                if (hero.magics.length > 3) m = hero.magics[3];
                else alert = new InstructionAlert("No magic!", 50);
            }
        }
        if (m != null) {
            if (m.mana > hero.getManna()) {
                alert = new InstructionAlert("Not enough mana!", 50);
            } else {
                switch (m.type) {
                    case Lighting:
                        if (hovered == null || hovered.isPlayer()) {
                            alert = new InstructionAlert("Can't attack here", 50);
                        } else {
                            hovered.takeDamage(hero.magic * 30);
                            hero.spendManna(m.mana);
                        }
                        break;

                    case Fireball:
                        this.units.stream()
                                .filter(unit -> unit.coordinate.distanceTo(cursor) <= 1)
                                .forEach(unit -> {
                                    unit.takeDamage(hero.magic * 20);
                                });
                        this.units.removeIf(unit -> !unit.isAlive());
                        hero.spendManna(m.mana);
                        break;

                    case Healing:
                        if (hovered == null || hovered.isEnemy()) {
                            alert = new InstructionAlert("Can't heal here", 50);
                        } else {
                            hovered.heal(hero.magic * 50);
                            hero.spendManna(m.mana);
                        }
                        break;

                }
            }
        }
    }

    @Override
    protected void keyReleased(int keyCode) {

    }


}
