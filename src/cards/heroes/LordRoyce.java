package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.List;

public class LordRoyce extends Hero {
    public LordRoyce(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroSpecialAbility(List<Minion> targetRow) {
        for (Minion minion : targetRow) {
            minion.setFrozen(true);
        }
    }
}