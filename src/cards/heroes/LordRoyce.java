package cards.heroes;

import cards.Card;
import cards.Hero;
import fileio.CardInput;
import game.GameBoard;

public final class LordRoyce extends Hero {

    public LordRoyce(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useAbility(final GameBoard gameBoard, final int affectedRow) {
        for (Card card : gameBoard.getBoard().get(affectedRow)) {
            if (!card.isFrozen()) {
                card.setFrozen(true);
            }
        }
    }
}
