package game.util;

import java.util.Objects;

public class BoardLocation {
    public static final int WIDTH = 12;
    public static final int HEIGHT = 10;
    private int x;
    private int y;
    public BoardLocation(BoardLocation other) {
        this.x = other.x;
        this.y = other.y;
    }

    public BoardLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public int distanceTo(BoardLocation other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

    public boolean isNeighbor(BoardLocation other) {
        return distanceTo(other) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardLocation that = (BoardLocation) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.min(WIDTH - 1, Math.max(0, x));
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = Math.min(HEIGHT - 1, Math.max(0, y));
    }


}