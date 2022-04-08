package framework.utils;

import java.util.Random;

public enum Direction {
    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    public final int x;
    public final int y;
    public Direction opposite;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        WEST.opposite = EAST;
        EAST.opposite = WEST;
    }

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private static final Random rand = new Random();
    public static Direction randomDirection() {
        return Direction.values()[rand.nextInt(Direction.values().length)];
    }
}
