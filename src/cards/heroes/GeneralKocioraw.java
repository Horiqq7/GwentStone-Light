package cards.heroes;

import cards.Card;
import cards.Hero;
import fileio.CardInput;
import game.GameBoard;

public class GeneralKocioraw extends Hero {

    public GeneralKocioraw(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useAbility(final GameBoard gameBoard, final int affectedRow) {
        for (Card card : gameBoard.getBoard().get(affectedRow)) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }
}