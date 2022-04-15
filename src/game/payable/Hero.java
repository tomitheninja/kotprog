package game.payable;

import game.util.RandomSkillStrength;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Hero {
    public final int attack;
    public final int defence;
    public final int magic;
    public final int knowledge;
    public final int morale;
    public final int luck;
    public final Magic[] magics;
    protected int manna;

    public Hero(int attack, int defence, int magic, int knowledge, int morale, int luck, Magic[] magics) {
        this.attack = Math.min(10, Math.max(0, attack));
        this.defence = Math.min(10, Math.max(0, defence));
        this.magic = Math.min(10, Math.max(0, magic));
        this.knowledge = Math.min(10, Math.max(0, knowledge));
        this.morale = Math.min(10, Math.max(0, morale));
        this.luck = Math.min(10, Math.max(0, luck));
        this.magics = magics;
        this.manna = this.knowledge * 10;
    }

    public int getManna() {
        return manna;
    }

    public int spendManna(int manna) {
        if (this.manna < manna) throw new RuntimeException("Not enough manna");
        this.manna -= manna;
        return this.manna;
    }


    public static class ShopHero {
        private final HashMap<Skill, Integer> skillLevels = new HashMap<>();


        public ShopHero() {
            Arrays.stream(Skill.values()).forEach(skill -> this.skillLevels.put(skill, 1));
        }

        public Hero toHero(Magic[] magics) {
            return new Hero(this.skillLevels.get(Skill.ATTACK), this.skillLevels.get(Skill.DEFENCE), this.skillLevels.get(Skill.MAGIC), this.skillLevels.get(Skill.KNOWLEDGE), this.skillLevels.get(Skill.MORALE), this.skillLevels.get(Skill.LUCK), magics);
        }

        public int getSkillLevel(Skill skill) {
            return this.skillLevels.get(skill);
        }

        public enum Skill {
            ATTACK, DEFENCE, MAGIC, KNOWLEDGE, MORALE, LUCK,
        }

        public class SkillAction {
            public final Skill skill;

            public SkillAction(Skill skill) {
                this.skill = skill;
            }

            /**
             * A legelső tulajdonságpont elköltése 5 aranyba kerül.
             * Onnantól kezdve minden további tulajdonságpont ára 10%-kal magasabb lesz.
             * Az árak mindig egész értékek lesznek, és minden tulajdonságpont esetében felfelé kerekítünk.
             * tehát 5, 6, 7, 8, 9, 10, 11, 13 ...
             */
            public static int getEnhanceCost(int nextLevel) {
                assert nextLevel > 1 : "nextLevel must be greater than 1";
                if (nextLevel == 2) {
                    return 5;
                }
                return (int) Math.ceil(getEnhanceCost(nextLevel - 1) * 1.1);
            }

            public boolean canEnhance() {
                return getLevel() < 10;
            }

            public boolean canDecrease() {
                return getLevel() > 1;
            }

            public int getEnhanceCost() {
                return getEnhanceCost(getLevel() + 1);
            }

            public int getDecreaseCost() {
                return getEnhanceCost(getLevel());
            }

            public int getLevel() {
                return skillLevels.get(skill);
            }

            public void enhance() {
                if (!canEnhance()) throw new IllegalStateException("Cannot enhance skill");
                skillLevels.put(skill, getLevel() + 1);
            }

            public void decrease() {
                if (!canDecrease()) throw new IllegalStateException("Cannot decrease skill");
                skillLevels.put(skill, getLevel() - 1);
            }
        }
    }
}
