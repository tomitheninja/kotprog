package game.util;

public class ShopItem<T> {
    public T type;
    public int cost;
    public int amount;

    public ShopItem(T type, int cost) {
        this.type = type;
        this.cost = cost;
        this.amount = 0;
    }
}