package cards.minions;

import cards.Card;
import cards.Minion;

public class Disciple extends Minion {
    public void use(final Card user, final Card target) {
        target.setHealth(target.getHealth() + 2);
    }
}