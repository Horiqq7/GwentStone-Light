package cards;

import fileio.CardInput;

import java.util.List;

public class Minion extends Card {
    public Minion(CardInput cardInput) {
        super(cardInput);
        this.frozen = false;
        this.hasAttacked = false;
    }

    public void resetTurn() {
        hasAttacked = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }

}