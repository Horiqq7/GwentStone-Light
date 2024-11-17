package cards.minions;

import cards.Card;
import cards.Minion;

public class Miraj extends Minion {
    public void use(final Card user, final Card target) {
        int tempHealth = user.getHealth();
        user.setHealth(target.getHealth());
        target.setHealth(tempHealth);
    }
}
