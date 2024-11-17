package game;

import cards.Card;
import java.util.ArrayList;

public final class GameBoard {
    private static final int NUMBER_OF_ROWS = 4;
    private static final int MAX_CARDS_PER_ROW = 5;
    private final ArrayList<ArrayList<Card>> board;
    private boolean gameEnded;

    /**
     * Constructorul clasei GameBoard.
     * Creeaza o tabla de joc cu un numar fix de randuri si initializeaza starea jocului.
     */
    public GameBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            board.add(new ArrayList<>());
        }
        this.gameEnded = false;
    }

    /**
     * Verifica daca jocul s-a terminat.
     *
     * @return True daca jocul s-a terminat, false altfel.
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Marcheaza jocul ca fiind terminat.
     */
    public void endGame() {
        this.gameEnded = true;
    }

    /**
     * Returneaza cartea aflata la o anumita pozitie pe tabla.
     *
     * @param x Indexul randului.
     * @param y Indexul pozitiei pe rand.
     * @return Cartea de la pozitia specificata.
     */
    public Card getCard(final int x, final int y) {
        return board.get(x).get(y);
    }

    /**
     * Reseteaza starea cartilor de pe un rand (le dezgheata si le reseteaza starea de atac).
     *
     * @param row Indexul randului care trebuie resetat.
     */
    public void resetStatusOfCardsOnRow(final int row) {
        for (Card card : board.get(row)) {
            card.setFrozen(false);
            card.setHasAttacked(false);
        }
    }

    /**
     * Reseteaza tabla de joc, eliminand toate cartile si reseteaza starea jocului.
     */
    public void resetBoard() {
        for (ArrayList<Card> cards : board) {
            cards.clear(); // Goleste fiecare rand
        }
        this.gameEnded = false;
    }

    /**
     * Verifica daca un rand de pe tabla este plin (are numarul maxim de carti).
     *
     * @param row Indexul randului care trebuie verificat.
     * @return True daca randul este plin, false altfel.
     */
    public boolean isRowFull(final int row) {
        return board.get(row).size() >= MAX_CARDS_PER_ROW;
    }

    /**
     * Returneaza toate cartile de pe tabla, grupate pe randuri.
     *
     * @return O lista de randuri care contin carti.
     */
    public ArrayList<ArrayList<Card>> getCardsOnTable() {
        ArrayList<ArrayList<Card>> nonEmptyRows = new ArrayList<>();
        for (ArrayList<Card> row : board) {
            if (!row.isEmpty()) {
                nonEmptyRows.add(new ArrayList<>(row));
            }
        }
        return nonEmptyRows;
    }

    /**
     * Returneaza intreaga tabla de joc.
     *
     * @return O lista de randuri, fiecare continand carti.
     */
    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    /**
     * Adauga o carte pe un rand specificat al tablei de joc.
     *
     * @param card Cartea care trebuie adaugata.
     * @param row Indexul randului unde va fi adaugata cartea.
     */
    public void placeCard(final Card card, final int row) {
        board.get(row).add(card);
    }
}
