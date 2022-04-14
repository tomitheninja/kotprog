package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
import game.payable.Hero.ShopHero;
import game.payable.Magic;
import game.payable.Unit;
import game.util.ShopItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ShopState extends GameState {
    final static protected Color selectedColor = Color.red;
    public final Hero enemyHero;
    public final ArrayList<Unit> enemyUnits;
    public final int initialGold;
    protected final ShopItem<Unit.Type>[] units = new ShopItem[]{new ShopItem<>(Unit.Type.FOLDMUVES, 2), new ShopItem<>(Unit.Type.IJASZ, 5), new ShopItem<>(Unit.Type.GRIFF, 10),};
    protected BuyableMagic[] magics = new BuyableMagic[]{new BuyableMagic(Magic.Villamcsapas), new BuyableMagic(Magic.Tuzlabda), new BuyableMagic(Magic.Feltamasztas),};
    protected int cursorIndex = 0;
    ShopHero hero = new ShopHero();
    private int gold;

    protected ShopState(GameStateManager manager, int initialGold) {
        super(manager);
        if (initialGold < 0) {
            throw new IllegalArgumentException("Initial gold must not be negative!");
        }
        this.initialGold = this.gold = initialGold;

        enemyHero = new Hero(1, 1, 1, 1, 1, 1, new Magic[]{});
        enemyUnits = new ArrayList();
        enemyUnits.add(new Unit(Unit.Type.FOLDMUVES, 1));
        enemyUnits.add(new Unit(Unit.Type.IJASZ, 1));
        enemyUnits.add(new Unit(Unit.Type.GRIFF, 1));
    }

    @Override
    protected void loop() {

    }

    @Override
    protected void render(Graphics graphics) {
        int nextCursorIdx = 0;
        Toolkit t = Toolkit.getDefaultToolkit();

        // background
        graphics.setColor(Color.WHITE);
        graphics.clearRect(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT);

        // title
        graphics.setColor(Color.BLACK);
        graphics.drawString("Shop", 15, 20);

        // gold
        graphics.setColor(Color.BLACK);
        graphics.drawString("Gold: " + gold, 15, 35);

        // hero image
        ImageIcon myIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("hero.png")));

        graphics.drawImage(myIcon.getImage(), 15, 50, 180, 350, null);

        // draw hero stats
        graphics.setColor(Color.BLACK);
        ShopHero.Skill[] values = Hero.ShopHero.Skill.values();
        for (int i = 0; i < values.length; i++) {
            ShopHero.Skill skill = values[i];
            graphics.setFont(new Font("Arial", Font.PLAIN, 25));
            graphics.setColor(i == cursorIndex ? selectedColor : Color.BLACK);
            graphics.drawString(skill.name() + ": " + hero.getSkillLevel(skill), 200, 80 + (skill.ordinal()) * 60);
        }

        // draw seperator
        graphics.setColor(Color.BLACK);
        graphics.drawLine(15, 400, WindowManager.WIDTH - 15, 400);
        graphics.drawLine(420, 55, 420, 400);


        // draw units
        for (int i = 0; i < units.length; i++) {
            graphics.setColor(i + values.length == cursorIndex ? selectedColor : Color.BLACK);
            String name = units[i].type + " (" + units[i].cost + " arany): " + units[i].amount;
            graphics.drawString(name, 450, 80 + i * 30);
        }

        // draw seperator
        graphics.setColor(Color.BLACK);
        graphics.drawLine(420, 60 + units.length * 30, WindowManager.WIDTH, 60 + units.length * 30);

        // draw magics
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.setColor(values.length + units.length == cursorIndex ? selectedColor : Color.BLACK);

        for (int i = 0; i < magics.length; i++) {
            graphics.setColor(i + values.length + units.length == cursorIndex ? selectedColor : Color.BLACK);
            graphics.drawString(magics[i].magic.name + " (" + magics[i].magic.price + " arany): " + (magics[i].bought ? "van" : "nincs"), 450, 80 + (i + units.length) * 30);
        }

        // instructions
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 15));
        graphics.drawString("Use the arrow keys to buy. Press ENTER to start", 15, WindowManager.HEIGHT - 15);
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ENTER -> {
                if (Arrays.stream(units).allMatch((unit) -> unit.amount == 0)) {
                    JOptionPane.showMessageDialog(null, "You can't start the game without units!");
                } else {
                    ArrayList<Unit> us = new ArrayList<>();
                    for (var u : units) if (u.amount > 0) us.add(new Unit(u.type, u.amount));
                    Hero h = hero.toHero(Arrays.stream(magics).filter((magic) -> magic.bought).map((magic) -> magic.magic).toArray(Magic[]::new));
                    gameStateManager.stackState(new PlayingState(gameStateManager, h, enemyHero, us, enemyUnits));
                }
            }
            case KeyEvent.VK_UP -> cursorIndex = Math.max(0, cursorIndex - 1);
            case KeyEvent.VK_DOWN ->
                    cursorIndex = Math.min(ShopHero.Skill.values().length + units.length + 3 - 1, cursorIndex + 1);
            case KeyEvent.VK_RIGHT -> {
                if (cursorIndex < ShopHero.Skill.values().length) {
                    ShopHero.SkillAction action = hero.new SkillAction(ShopHero.Skill.values()[cursorIndex]);
                    if (!action.canEnhance()) {
                        JOptionPane.showMessageDialog(null, "You can't enhance this skill!");
                    } else if (gold < action.getEnhanceCost()) {
                        JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                    } else {
                        gold -= action.getEnhanceCost();
                        action.enhance();
                    }
                } else if (cursorIndex < ShopHero.Skill.values().length + units.length) {
                    int i = cursorIndex - ShopHero.Skill.values().length;
                    for (int j = 0; j < units.length; j++) {
                        if (i == j) {
                            if (gold < units[j].cost) {
                                JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                            } else {
                                gold -= units[j].cost;
                                units[j].amount++;
                            }
                        }
                    }
                } else {
                    magics[cursorIndex - ShopHero.Skill.values().length - units.length].buy();
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (cursorIndex < ShopHero.Skill.values().length) {
                    ShopHero.SkillAction action = hero.new SkillAction(ShopHero.Skill.values()[cursorIndex]);
                    if (!action.canDecrease()) {
                        JOptionPane.showMessageDialog(null, "You can't decrease this skill!");
                    } else {
                        gold += action.getDecreaseCost();
                        action.decrease();
                    }
                } else if (cursorIndex < ShopHero.Skill.values().length + units.length) {
                    int i = cursorIndex - ShopHero.Skill.values().length;
                    for (int j = 0; j < units.length; j++) {
                        if (i == j && units[j].amount > 0) {
                            gold += units[j].cost;
                            units[j].amount--;
                        }
                    }
                } else {
                    magics[cursorIndex - ShopHero.Skill.values().length - units.length].sell();
                }
            }
        }
    }

    @Override
    protected void keyReleased(int keyCode) {

    }


    private class BuyableMagic {
        public final Magic magic;
        public boolean bought = false;

        public BuyableMagic(Magic magic) {
            this.magic = magic;
        }

        public void buy() {
            if (bought) {
                JOptionPane.showMessageDialog(null, "You already bought this!");
            } else if (gold < magic.price) {
                JOptionPane.showMessageDialog(null, "You don't have enough gold!");
            } else {
                gold -= magic.price;
                bought = true;
            }
        }

        public void sell() {
            if (!bought) {
                JOptionPane.showMessageDialog(null, "You haven't bought this!");
            } else {
                gold += magic.price;
                bought = false;
            }
        }
    }
}
