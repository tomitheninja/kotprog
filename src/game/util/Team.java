package game.util;

import java.awt.*;

public enum Team {
    PLAYER,
    ENEMY;

    public Color getColor() {
        return switch (this) {
            case PLAYER -> Color.GREEN;
            case ENEMY -> Color.RED;
        };
    }
}
