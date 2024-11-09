package cards;

import fileio.CardInput;

import java.util.List;

public class Hero extends Card {
    private int abilityCost;

    public Hero(CardInput cardInput) {
        super(cardInput);
        this.health = 30;
        this.hasAttacked = false;
    }

    public boolean isHero() {
        return true;
    }

    public int getAbilityCost() {
        return abilityCost;
    }
}