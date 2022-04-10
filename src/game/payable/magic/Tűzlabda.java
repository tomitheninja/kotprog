package game.payable.magic;

public class Tűzlabda implements Magic {
    @Override
    public int getPrice() {
        return 120;
    }

    @Override
    public int getMannaCost() {
        return 9;
    }

    @Override
    public String getName() {
        return "Tűzlabda";
    }

    @Override
    public String getDescription() {
        return "Sebzés a mező körülötti egységekre";
    }
}
