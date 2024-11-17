package cards.heroes;

import cards.Card;
import cards.Hero;
import fileio.CardInput;
import game.GameBoard;

public class EmpressThorina extends Hero {

    public EmpressThorina(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useAbility(final GameBoard gameBoard, final int affectedRow) {
        Card maxHealthCard = null;
        for (Card card : gameBoard.getBoard().get(affectedRow)) {
            if (maxHealthCard == null || card.getHealth() > maxHealthCard.getHealth()) {
                maxHealthCard = card;
            }
        }
        if (maxHealthCard != null) {
            gameBoard.getBoard().get(affectedRow).remove(maxHealthCard);
        }
    }
}