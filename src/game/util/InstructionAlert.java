package game.util;

public class InstructionAlert {
    public final String text;
    public final int numTicks;
    protected int ticks;

    public InstructionAlert(String text, int numTicks) {
        this.numTicks = numTicks;
        this.text = text;
        this.ticks = 0;
    }

    public void tick() {
        ticks++;
    }

    public boolean isDone() {
        return ticks >= numTicks;
    }
}