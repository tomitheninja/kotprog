package game.payable.magic;

public class Feltamasztas implements Magic {

    @Override
    public int getPrice() {
        return 120;
    }

    @Override
    public int getMannaCost() {
        return 6;
    }

    @Override
    public String getName() {
        return "Feltámasztás";
    }

    @Override
    public String getDescription() {
        return "Egy kiválaszott egység feltámasztása";
    }
}
