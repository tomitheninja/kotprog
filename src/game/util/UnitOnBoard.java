package game.util;

import game.payable.Hero;
import game.payable.Unit;

public class UnitOnBoard extends Unit {
    public boolean moved = false;
    public boolean reattack = true;
    public BoardLocation coordinate;
    protected Team team;
    protected Hero hero;

    public UnitOnBoard(Unit unit, BoardLocation coordinate, Team team, Hero hero) {
        super(unit);
        if (coordinate == null) throw new IllegalArgumentException("coordinate can't be null");
        if (team == null) throw new IllegalArgumentException("team can't be null");
        if (hero == null) throw new IllegalArgumentException("hero can't be null");
        this.coordinate = coordinate;
        this.team = team;
        this.hero = hero;
    }

    @Override
    public int getInitiative() {
        return super.getInitiative() + hero.morale;
    }

    public static boolean fight(UnitOnBoard attacker, UnitOnBoard defender, Hero attackerHero, Hero defenderHero, boolean allowReattack) {
        assert attacker.isAlive() && defender.isAlive();
        assert !attacker.moved;
        assert attacker.team != defender.team;
        assert attackerHero != null && defenderHero != null;
        assert attacker.getInitiative() >= defender.getInitiative() || defender.moved;

        boolean isCriticalDamage = 100 * Math.random() < attackerHero.luck * 5;
        int damage = attacker.getRealAttack(attackerHero, defenderHero, isCriticalDamage);
        defender.takeDamage(damage);
        if (defender.isAlive() && defender.reattack && allowReattack) {
            defender.reattack = defender.getType() == Unit.Type.GRIFF;
            int defenderDamage = defender.getRealAttack(defenderHero, attackerHero, false);
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
        return dx + dy <= getMovement();
    }

    public int getRealAttack(Hero attackerHero, Hero defenderHero, boolean isCriticalDamage) {
        double attackModifier = (1 + attackerHero.attack * 0.1) * (1 - defenderHero.defence * 0.05) * (isCriticalDamage ? 2 : 1);
        return (int) (getAttack() * attackModifier);
    }
}