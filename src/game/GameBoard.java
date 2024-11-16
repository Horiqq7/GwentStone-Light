package game;

import cards.Card;
import java.util.ArrayList;
import java.util.Collections;

public final class GameBoard {
    private final ArrayList<ArrayList<Card>> board;
    private boolean gameEnded = false;

    public GameBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            board.add(new ArrayList<>());
        }
        this.gameEnded = false;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void endGame() {
        this.gameEnded = true;
    }

    public Card getCard(final int x, final int y) {
            return board.get(x).get(y);
    }

    public void resetStatusOfCardsOnRow(final int row) {
        for (Card card : board.get(row)) {
            card.setFrozen(false);
            card.setHasAttacked(false);
        }
    }

    public void resetBoard() {
        for (ArrayList<Card> cards : board) {
            Collections.fill(cards, null);
        }
        this.gameEnded = false;
    }

    public boolean isRowFull(final int row) {
        return board.get(row).size() >= 5;
    }

    public ArrayList<ArrayList<Card>> getCardsOnTable() {
        ArrayList<ArrayList<Card>> nonEmptyRows = new ArrayList<>();
        for (ArrayList<Card> row : board) {
            if (!row.isEmpty()) {
                nonEmptyRows.add(new ArrayList<>(row));
            }
        }
        return nonEmptyRows;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public void placeCard(final Card card, final int row) {
        board.get(row).add(card);
    }
}
