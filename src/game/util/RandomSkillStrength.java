package game.util;

public class RandomSkillStrength {
    public static int get() {
        return (int) Math.floor(Math.log(1 - Math.random()) / (-.4) % 9 + 1);
    }
}
