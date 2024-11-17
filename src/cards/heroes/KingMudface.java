package cards.heroes;

import cards.Card;
import cards.Hero;
import fileio.CardInput;
import game.GameBoard;

public class KingMudface extends Hero {

    public KingMudface(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useAbility(final GameBoard gameBoard, final int affectedRow) {
        for (Card card : gameBoard.getBoard().get(affectedRow)) {
            card.setHealth(card.getHealth() + 1);
        }
    }
}