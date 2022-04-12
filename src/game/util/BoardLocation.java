package game.util;

import java.util.Objects;

public class BoardLocation {
    private int x;
    private int y;

    public BoardLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.min(11, Math.max(0, x));
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = Math.min(11, Math.max(0, y));
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
}