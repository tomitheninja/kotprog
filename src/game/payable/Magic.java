package game.payable;

public class Magic {
    public static final Magic Villamcsapas = new Magic(Type.Lighting, 60, 5);
    public static final Magic Tuzlabda = new Magic(Type.Fireball, 120, 9);
    public static final Magic Feltamasztas = new Magic(Type.Healing, 120, 6);
    public final int price;
    public final Type type;
    public final int mana;

    private Magic(Type type, int price, int mana) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (price < 1) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (mana < 1) {
            throw new IllegalArgumentException("mana must be positive");
        }
        this.type = type;
        this.price = price;
        this.mana = mana;
    }

    public enum Type {
        Lighting,
        Fireball,
        Healing;

        @Override
        public String toString() {
            return switch (this) {
                case Lighting -> "Villámcsapás";
                case Fireball -> "Tűzlabda";
                case Healing -> "Gyógyítás";
            };
        }

        public String getDescription() {
            return "TODO: implement Magic.description";
        }
    }
}
