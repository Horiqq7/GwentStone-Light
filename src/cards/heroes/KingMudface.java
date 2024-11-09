package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.List;

public class KingMudface extends Hero {
    public KingMudface(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroSpecialAbility(List<Minion> playerRow) {
        for (Minion minion : playerRow) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }
}