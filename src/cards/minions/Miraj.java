package cards.minions;

import cards.Card;
import cards.Minion;

public final class Miraj extends Minion {
    /**
     * Foloseste abilitatea minionului Miraj pentru a schimba viata dintre doua carduri.
     * Face schimb intre viata lui si viata tintei.
     *
     * @param card cardul care foloseste abilitatea
     * @param target cardul tinta, al carui viata va fi schimbata cu cea a cardului utilizatorului.
     */
    public void use(final Card card, final Card target) {
        int tempHealth = card.getHealth();
        card.setHealth(target.getHealth());
        target.setHealth(tempHealth);
    }
}

