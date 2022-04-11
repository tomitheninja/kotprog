package game.states;

import framework.gamestates.GameState;
import framework.gamestates.GameStateManager;
import game.payable.Hero;
import game.payable.Unit;
import game.payable.magic.Feltamasztas;
import game.payable.magic.Tűzlabda;
import game.payable.magic.VillamCsapas;

import java.awt.*;
import java.util.ArrayList;

public class PlayingState extends GameState {

    protected Hero hero;
    ArrayList<Unit> units;
    Feltamasztas feltamasztas;
    Tűzlabda tuzlabda;
    VillamCsapas villamCsapas;

    public PlayingState(GameStateManager gsm, Hero hero, ArrayList<Unit> units, boolean feltamasztas, boolean tuzlabda, boolean villamCsapas) {
        super(gsm);
        this.hero = hero;
        this.units = units;
        if (feltamasztas) this.feltamasztas = new Feltamasztas();
        if (tuzlabda) this.tuzlabda = new Tűzlabda();
        if (villamCsapas) this.villamCsapas = new VillamCsapas();
    }

    @Override
    protected void loop() {

    }

    @Override
    protected void render(Graphics graphics) {

    }

    @Override
    protected void keyPressed(int keyCode) {

    }

    @Override
    protected void keyReleased(int keyCode) {

    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
