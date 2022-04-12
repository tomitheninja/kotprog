package game.payable;

public class Magic {
    public static final Magic Villamcsapas = new Magic("Villámcapás", 60, 5, "Egy kiválasztott ellenséges egységre (varázserő * 30) sebzés okozása", null);
    public static final Magic Tuzlabda = new Magic("Tűzlabda", 120, 9, "Egy kiválasztott mező körüli 3x3-as területen lévő összes (saját, illetve ellenséges) egységre (varázserő * 20) sebzés okozása", null);
    public static final Magic Feltamasztas = new Magic("Feltámasztás", 120, 6, "Egy kiválasztott saját egység feltámasztása. Maximális gyógyítás mértéke: (varázserő * 50)", null);
    private static final MagicAction emptyMagicAction = new MagicAction() {
        @Override
        public void action() {
        }
    };
    public final String description;
    public final int price;
    public final String name;
    public final int mana;
    public final MagicAction action;

    private Magic(String name, int price, int mana, String description, MagicAction action) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (price < 1) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (mana < 1) {
            throw new IllegalArgumentException("mana must be positive");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        this.name = name;
        this.price = price;
        this.mana = mana;
        this.description = description;
        this.action = action != null ? action : emptyMagicAction;
    }

    @Override
    public String toString() {
        return name;
    }

    public interface MagicAction {
        void action();
    }
}
