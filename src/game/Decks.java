package game;

import fileio.CardInput;
import fileio.DecksInput;
import java.util.ArrayList;
import cards.Card;

public final class Decks {
    private final ArrayList<ArrayList<Card>> decks;

    public Decks(final DecksInput decksInput) {
        this.decks = new ArrayList<>();
        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> deck = new ArrayList<>();
            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput);
                deck.add(card);
            }
            decks.add(deck);
        }
    }

    /**
     * Returneaza deck-ul de la un index specificat.
     *
     * @param index Indexul deck-ului dorit.
     * @return Lista de obiecte Card care reprezinta deck-ul de la indexul dat.
     */
    public ArrayList<Card> deckIndex(final int index) {
        return this.decks.get(index);
    }
}
