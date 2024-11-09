package cards.heroes;

import cards.Hero;
import cards.Minion;
import fileio.CardInput;

import java.util.List;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroSpecialAbility(List<Minion> playerRow) {
        for (Minion minion : playerRow) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }
}