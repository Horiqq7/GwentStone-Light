package cards.minions;

import cards.Card;
import cards.Minion;

public class TheRipper extends Minion {
    public void use(final Card user, final Card target) {
        int reducedAttack = target.getAttackDamage() - 2;
        target.setAttackDamage(Math.max(reducedAttack, 0));
    }
}
