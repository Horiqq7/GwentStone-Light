package cards.minions;

import cards.Card;
import cards.Minion;

public final class TheRipper extends Minion {
    /**
     * Foloseste abilitatea minionului TheRipper pentru a reduce atacul tintei cu 2.
     * Daca rezultatul este mai mic decat 0, atacul tintei va fi setat la 0.
     *
     * @param user cardul care foloseste abilitatea
     * @param target cardul tinta care va avea atacul redus
     */
    public void use(final Card user, final Card target) {
        int reducedAttack = target.getAttackDamage() - 2;
        target.setAttackDamage(Math.max(reducedAttack, 0));
    }
}

