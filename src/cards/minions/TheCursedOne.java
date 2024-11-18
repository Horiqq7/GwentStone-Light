package cards.minions;

import cards.Card;
import cards.Minion;

public final class TheCursedOne extends Minion {
    /**
     * Foloseste abilitatea minionului TheCursedOne pentru a schimba atacul si viata tintei.
     * Daca atacul tintei este 0, viata tintei va fi setata la 0.
     * In caz contrar, atacul este setat la valoarea vietii,
     * iar viata la valoarea initiala a atacului.
     *
     * @param card cardul care foloseste abilitatea
     * @param target cardul tinta care va avea atacul si viata schimbate
     */
    public void use(final Card card, final Card target) {
        int tempAttack = target.getAttackDamage();
        if (tempAttack == 0) {
            target.setHealth(0);
        } else {
            target.setAttackDamage(target.getHealth());
            target.setHealth(tempAttack);
        }
    }
}
