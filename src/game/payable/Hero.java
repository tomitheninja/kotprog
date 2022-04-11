package game.payable;

import game.payable.magic.Magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Hero {
    protected HashMap<Skill, Integer> skillLevels = new HashMap<>();
    protected ArrayList<Magic> magic = new ArrayList<>();

    protected int manna = 10;

    public Hero() {
        Arrays.stream(Skill.values()).forEach(skill -> this.skillLevels.put(skill, 1));
    }

    public Hero(int attack, int defence, int magic, int knowledge, int morale, int luck) {
        this();
        if (0 > attack || attack > 10) throw new IllegalArgumentException("Attack must be between 0 and 10");
        this.skillLevels.put(Skill.ATTACK, attack);
        if (0 > defence || defence > 10) throw new IllegalArgumentException("Defence must be between 0 and 10");
        this.skillLevels.put(Skill.DEFENCE, defence);
        if (0 > magic || magic > 10) throw new IllegalArgumentException("Magic must be between 0 and 10");
        this.skillLevels.put(Skill.MAGIC, magic);
        if (0 > knowledge || knowledge > 10) throw new IllegalArgumentException("Knowledge must be between 0 and 10");
        this.skillLevels.put(Skill.KNOWLEDGE, knowledge);
        if (0 > morale || morale > 10) throw new IllegalArgumentException("Morale must be between 0 and 10");
        this.skillLevels.put(Skill.MORALE, morale);
        if (0 > luck || luck > 10) throw new IllegalArgumentException("Luck must be between 0 and 10");
        this.skillLevels.put(Skill.LUCK, luck);
    }

    public void addMagic(Magic magic) {
        this.magic.add(magic);
    }

    public int getSkill(Skill skill) {
        return this.skillLevels.get(skill);
    }

    public int getAttack() {
        return getSkill(Skill.ATTACK);
    }

    public int getDefence() {
        return getSkill(Skill.DEFENCE);
    }

    public int getMagic() {
        return getSkill(Skill.MAGIC);
    }

    public int getKnowledge() {
        return getSkill(Skill.KNOWLEDGE);
    }

    public int getMorale() {
        return getSkill(Skill.MORALE);
    }

    public int getLuck() {
        return getSkill(Skill.LUCK);
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
            return getHero().skillLevels.get(skill);
        }

        public Hero getHero() {
            return Hero.this;
        }

        public void enhance() {
            if (!canEnhance()) throw new IllegalStateException("Cannot enhance skill");
            getHero().skillLevels.put(skill, getLevel() + 1);
        }

        public void decrease() {
            if (!canDecrease()) throw new IllegalStateException("Cannot decrease skill");
            getHero().skillLevels.put(skill, getLevel() - 1);
        }
    }

}
