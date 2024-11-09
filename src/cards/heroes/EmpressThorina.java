package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.List;

public class EmpressThorina extends Hero {
    public EmpressThorina(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroSpecialAbility(List<Minion> targetRow) {
        Minion maxHealthMinion = null;
        for (Minion minion : targetRow) {
            if (maxHealthMinion == null || minion.getHealth() > maxHealthMinion.getHealth()) {
                maxHealthMinion = minion;
            }
        }
        if (maxHealthMinion != null) {
            targetRow.remove(maxHealthMinion);
        }
    }
}