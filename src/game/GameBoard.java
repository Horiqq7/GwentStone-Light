package game;

import cards.Card;
import cards.Hero;
import cards.Minion;
import fileio.Coordinates;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<ArrayList<Card>> board;
    private Hero playerOneHero;
    private Hero playerTwoHero;

    public GameBoard() {
        board = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            board.add(new ArrayList<>());
        }
    }

    public ArrayList<ArrayList<Card>> getAllCards() {
        return new ArrayList<>(board);
    }

    public Card getCard(int X, int Y) {
            return board.get(X).get(Y);
    }

    public void resetStatusOfCardsOnRow(int row) {
        for(Card card : board.get(row)) {
            card.setFrozen(false);
            card.setHasAttacked(false);
        }
    }

    public boolean isRowFull(int row) {
        if(board.get(row).size() >= 5) {
            return true;
        }
        return false;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public void placeCard(Card card, int row) {
        board.get(row).add(card);
    }

    public Hero getPlayerOneHero() {
        return playerOneHero;
    }

    public Hero getPlayerTwoHero() {
        return playerTwoHero;
    }
}