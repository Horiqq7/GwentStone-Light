package cards.minions;

import cards.Card;
import cards.Minion;

public class TheCursedOne extends Minion {
    public void use(final Card user, final Card target) {
        int tempAttack = target.getAttackDamage();
        if (tempAttack == 0) {
            target.setHealth(0);
        } else {
            target.setAttackDamage(target.getHealth());
            target.setHealth(tempAttack);
        }
    }
}
