package game.payable.magic;

public class VillamCsapas implements Magic {
    @Override
    public int getPrice() {
        return 60;
    }

    @Override
    public int getMannaCost() {
        return 5;
    }

    @Override
    public String getName() {
        return "VillamCsapas";
    }

    @Override
    public String getDescription() {
        return "Sebzés egy kiválasztott egységre";
    }
}
