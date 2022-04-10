package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class ShopState extends GameState {

    final static protected Color selectedColor = Color.red;
    public final int initialGold;
    private final UnitType[] unitTypes = new UnitType[]{new UnitType("Foldmuves", 2), new UnitType("Ijasz", 5), new UnitType("Griff", 10), new UnitType("Unit4", 15), new UnitType("Unit5", 20), new UnitType("Unit6", 30)};
    protected int cursorIndex = 0;
    Hero hero = new Hero();
    private int gold;

    protected ShopState(GameStateManager manager, int initialGold) {
        super(manager);
        if (initialGold < 0) {
            throw new IllegalArgumentException("Initial gold must not be negative!");
        }
        this.initialGold = this.gold = initialGold;
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
        graphics.drawString("Shop", 15, 15);

        // gold
        graphics.setColor(Color.BLACK);
        graphics.drawString("Gold: " + gold, 15, 30);

        // hero image
        ImageIcon myIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("hero.png")));

        graphics.drawImage(myIcon.getImage(), 15, 50, 180, 360, null);

        // draw hero stats
        graphics.setColor(Color.BLACK);
        Hero.Skill[] values = Hero.Skill.values();
        for (int i = 0; i < values.length; i++) {
            Hero.Skill skill = values[i];
            graphics.setFont(new Font("Arial", Font.PLAIN, 25));
            graphics.setColor(i == cursorIndex ? selectedColor : Color.BLACK);
            graphics.drawString(skill.name() + ": " + hero.getSkill(skill), 200, 50 + (skill.ordinal() + 1) * 30);
        }

        // draw units
        for (int i = 0; i < unitTypes.length; i++) {
            graphics.setColor(i + values.length == cursorIndex ? selectedColor : Color.BLACK);
            String name = unitTypes[i].name + " (" + unitTypes[i].cost + " arany): " + unitTypes[i].num;
            graphics.drawString(name, 200, 50 + (i + 1 + values.length) * 30);
        }

        // instructions
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 15));
        graphics.drawString("Use the arrow keys to buy. Press ENTER to start", 15, WindowManager.HEIGHT - 15);
    }

    @Override
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> cursorIndex = Math.max(0, cursorIndex - 1);
            case KeyEvent.VK_DOWN -> cursorIndex = Math.min(Hero.Skill.values().length + 5, cursorIndex + 1);
            case KeyEvent.VK_RIGHT -> {
                if (cursorIndex < Hero.Skill.values().length) {
                    Hero.SkillAction action = hero.new SkillAction(Hero.Skill.values()[cursorIndex]);
                    if (!action.canEnhance()) {
                        JOptionPane.showMessageDialog(null, "You can't enhance this skill!");
                    } else if (gold < action.getEnhanceCost()) {
                        JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                    } else {
                        gold -= action.getEnhanceCost();
                        action.enhance();
                    }
                } else {
                    int i = cursorIndex - Hero.Skill.values().length;
                    for (int j = 0; j < unitTypes.length; j++) {
                        if (i == j) {
                            if (gold < unitTypes[j].cost) {
                                JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                            } else {
                                gold -= unitTypes[j].cost;
                                unitTypes[j].num++;
                            }
                        }
                    }
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (cursorIndex < Hero.Skill.values().length) {
                    Hero.SkillAction action = hero.new SkillAction(Hero.Skill.values()[cursorIndex]);
                    if (!action.canDecrease()) {
                        JOptionPane.showMessageDialog(null, "You can't decrease this skill!");
                    } else {
                        gold += action.getDecreaseCost();
                        action.decrease();
                    }
                } else {
                    int i = cursorIndex - Hero.Skill.values().length;
                    for (int j = 0; j < unitTypes.length; j++) {
                        if (i == j) {
                            gold -= unitTypes[j].cost;
                            unitTypes[j].num++;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    protected class UnitType {
        public String name;
        public int cost;
        public int num;

        public UnitType(String name, int cost) {
            this.name = name;
            this.cost = cost;
            this.num = 0;
        }
    }


}
