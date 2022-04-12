package game.payable;

import game.payable.magic.Magic;
import game.payable.magic.Tűzlabda;
import game.states.ShopState;

import java.util.ArrayList;

public class ShopItemToGameItem {
    public static ArrayList<Unit> createUnits(ShopState.ShopItem<ShopState.UnitNames>[] units) {
        ArrayList<Unit> result = new ArrayList<>();
        for (var unit : units) {
            if (unit.num == 0) continue;
            result.add(switch (unit.name) {
                case Foldmuves -> new Unit("Földműves", 1, 1, 3*unit.num, 4, 8, new Unit.SpecialAction() {
                    @Override
                    public void action() {
                    }
                });
                case Ijasz -> new Unit("Íjász", 2, 4, 7, 4*unit.num, 9, new Unit.SpecialAction() {
                    @Override
                    public void action() {
                    }
                });
                case Griff -> new Unit("Griff", 5*unit.num, 10*unit.num, 30*unit.num, 7, 15, new Unit.SpecialAction() {
                    @Override
                    public void action() {
                    }
                });
            });
        }
        return result;
    }

}
