package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import framework.gui.WindowManager;
import game.payable.Hero;
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
    public final int initialGold;
    private final ShopItem<Unit.Type>[] units = new ShopItem[]{new ShopItem<>(Unit.Type.FOLDMUVES, 2), new ShopItem<>(Unit.Type.IJASZ, 5), new ShopItem<>(Unit.Type.GRIFF, 10),
            /*, new ShopItem("Unit4", 15), new ShopItem("Unit5", 20), new ShopItem("Unit6", 30)*/};
    protected boolean boughtVillamcsapas = false;
    protected boolean boughtTuzlabda = false;
    protected boolean boughtFeltamasztas = false;
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
        graphics.drawString("Shop", 15, 20);

        // gold
        graphics.setColor(Color.BLACK);
        graphics.drawString("Gold: " + gold, 15, 35);

        // hero image
        ImageIcon myIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("hero.png")));

        graphics.drawImage(myIcon.getImage(), 15, 50, 180, 350, null);

        // draw hero stats
        graphics.setColor(Color.BLACK);
        Hero.Skill[] values = Hero.Skill.values();
        for (int i = 0; i < values.length; i++) {
            Hero.Skill skill = values[i];
            graphics.setFont(new Font("Arial", Font.PLAIN, 25));
            graphics.setColor(i == cursorIndex ? selectedColor : Color.BLACK);
            graphics.drawString(skill.name() + ": " + hero.getSkill(skill), 200, 80 + (skill.ordinal()) * 60);
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
        graphics.drawString("Villámcsapás (60 arany): " + (boughtVillamcsapas ? "van" : "nincs"), 450, 90 + units.length * 30);

        graphics.setColor(values.length + units.length + 1 == cursorIndex ? selectedColor : Color.BLACK);
        graphics.drawString("Tuzlabda (120 arany): " + (boughtTuzlabda ? "van" : "nincs"), 450, 120 + units.length * 30);

        graphics.setColor(values.length + units.length + 2 == cursorIndex ? selectedColor : Color.BLACK);
        graphics.drawString("Feltamasztás (120 arany): " + (boughtFeltamasztas ? "van" : "nincs"), 450, 150 + units.length * 30);

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
                    for (var u : units)
                        if (u.amount > 0) us.add(new Unit(u.type, u.amount));
                    gameStateManager.stackState(new PlayingState(gameStateManager, hero, us, boughtVillamcsapas, boughtTuzlabda, boughtFeltamasztas));
                }
            }
            case KeyEvent.VK_UP -> cursorIndex = Math.max(0, cursorIndex - 1);
            case KeyEvent.VK_DOWN ->
                    cursorIndex = Math.min(Hero.Skill.values().length + units.length + 3 - 1, cursorIndex + 1);
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
                } else if (cursorIndex < Hero.Skill.values().length + units.length) {
                    int i = cursorIndex - Hero.Skill.values().length;
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
                    int i = cursorIndex - Hero.Skill.values().length - units.length;
                    switch (i) {
                        case 0:
                            if (gold < 60) {
                                JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                            } else if (boughtVillamcsapas) {
                                JOptionPane.showMessageDialog(null, "You already bought this!");
                            } else {
                                gold -= 60;
                                boughtVillamcsapas = true;
                            }
                            break;
                        case 1:
                            if (gold < 120) {
                                JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                            } else if (boughtTuzlabda) {
                                JOptionPane.showMessageDialog(null, "You already bought this!");
                            } else {
                                gold -= 120;
                                boughtTuzlabda = true;
                            }
                            break;
                        case 2:
                            if (gold < 120) {
                                JOptionPane.showMessageDialog(null, "Nincs elég aranyad!");
                            } else if (boughtFeltamasztas) {
                                JOptionPane.showMessageDialog(null, "You already bought this!");
                            } else {
                                gold -= 120;
                                boughtFeltamasztas = true;
                            }
                            break;
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
                } else if (cursorIndex < Hero.Skill.values().length + units.length) {
                    int i = cursorIndex - Hero.Skill.values().length;
                    for (int j = 0; j < units.length; j++) {
                        if (i == j && units[j].amount > 0) {
                            gold += units[j].cost;
                            units[j].amount--;
                        }
                    }
                } else {
                    int i = cursorIndex - Hero.Skill.values().length - units.length;
                    switch (i) {
                        case 0:
                            if (boughtVillamcsapas) {
                                gold += 60;
                                boughtVillamcsapas = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "You haven't bought this!");
                            }
                            break;
                        case 1:
                            if (boughtTuzlabda) {
                                gold += 120;
                                boughtTuzlabda = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "You haven't bought this!");
                            }
                            break;
                        case 2:
                            if (boughtFeltamasztas) {
                                gold += 120;
                                boughtFeltamasztas = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "You haven't bought this!");
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void keyReleased(int keyCode) {

    }


    public enum MagicNames {
        Villamcsapas, Tuzlabda, Feltamasztas;

        @Override
        public String toString() {
            return switch (this) {
                case Villamcsapas -> "Villámcsapás";
                case Tuzlabda -> "Tűzlabda";
                case Feltamasztas -> "Feltámasztás";
            };
        }
    }


}
