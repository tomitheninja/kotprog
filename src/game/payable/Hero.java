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

    private Hero(int attack, int defence, int magic, int knowledge, int morale, int luck, Magic[] magics) {
        this.attack = attack;
        this.defence = defence;
        this.magic = magic;
        this.knowledge = knowledge;
        this.morale = morale;
        this.luck = luck;
        this.magics = magics;
    }

    /**
     * random hero with random stats
     */
    public Hero() {
        Random r = new Random();

        this.attack = RandomSkillStrength.get();
        this.defence = RandomSkillStrength.get();
        this.magic = RandomSkillStrength.get();
        this.knowledge = RandomSkillStrength.get();
        this.luck = RandomSkillStrength.get();
        this.morale = RandomSkillStrength.get();
        this.magics = Arrays.stream(new Magic[]{Magic.Villamcsapas, Magic.Tuzlabda, Magic.Feltamasztas,}).filter(s -> r.nextBoolean()).toArray(Magic[]::new);
    }

    public int getManna() {
        return manna;
    }

    public int addManna(int manna) {
        this.manna += manna;
        return this.manna;
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
