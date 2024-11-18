package cards.minions;

import cards.Card;
import cards.Minion;

public final class Disciple extends Minion {
    /**
     * Foloseste abilitatea minionului Disciple asupra unuei carti tinta.
     * Adauga 2 la viata tintei.
     *
     * @param card cardul care foloseste abilitatea
     * @param target cardul asupra caruia se aplica abilitatea
     */
    public void use(final Card card, final Card target) {
        target.setHealth(target.getHealth() + 2);
    }
}
