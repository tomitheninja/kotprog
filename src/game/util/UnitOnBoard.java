package game.util;

import game.payable.Hero;
import game.payable.Unit;

public class UnitOnBoard extends Unit {
    public boolean moved = false;
    public boolean reattack = true;
    public BoardLocation coordinate;
    Team team;

    public UnitOnBoard(Unit unit, BoardLocation coordinate, Team team) {
        super(unit);
        if (coordinate == null) throw new IllegalArgumentException("coordinate can't be null");
        if (team == null) throw new IllegalArgumentException("team can't be null");
        this.coordinate = coordinate;
        this.team = team;
    }

    public static boolean fight(UnitOnBoard attacker, UnitOnBoard defender, Hero attackerHero, Hero defenderHero, boolean allowReattack) {
        assert attacker.isAlive() && defender.isAlive();
        assert !attacker.moved;
        assert attacker.team != defender.team;
        assert attackerHero != null && defenderHero != null;
        assert attacker.initiative >= defender.initiative || defender.moved;

        boolean isCriticalDamage = 100 * Math.random() < attackerHero.luck;
        double attackModifier = (1 + attackerHero.attack * 0.1) * (1 - defenderHero.defence * 0.05) * (isCriticalDamage ? 2 : 1);
        int damage = (int) (attacker.getAttack() * attackModifier);
        defender.takeDamage(damage);
        if (defender.isAlive() && defender.reattack && allowReattack) {
            defender.reattack = defender.type == Unit.Type.GRIFF;
            double defenderAttackModifier = (1 + defenderHero.attack * 0.1) * (1 - attackerHero.defence * 0.05);
            int defenderDamage = (int) (defender.getAttack() * defenderAttackModifier);
            attacker.takeDamage(defenderDamage);
        }
        attacker.moved = true;
        return isCriticalDamage;
    }

    public boolean isPlayer() {
        return team == Team.PLAYER;
    }

    public boolean isEnemy() {
        return team == Team.ENEMY;
    }

    public boolean canMove() {
        return !this.moved;
    }

    public boolean canReach(BoardLocation target, BoardLocation[] obstacles) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        int tx = target.getX();
        int ty = target.getY();
        int dx = Math.abs(x - tx);
        int dy = Math.abs(y - ty);
        return dx + dy <= movement;
    }
}