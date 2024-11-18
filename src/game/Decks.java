package game;

import fileio.CardInput;
import fileio.DecksInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

    /**
     * Cartile dintr-un deck sunt amestecate folosind un seed specificat.
     *
     * @param deck Deck-ul care contine cartile ce urmeaza sa fie amestecate.
     * @param seed Seed-ul folosit pentru generarea aleatorie.
     * @return Deck-ul amestecat.
     */
    public static ArrayList<Card> shuffleDeck(final ArrayList<Card> deck, final int seed) {
        Random random = new Random(seed);
        Collections.shuffle(deck, random);
        return deck;
    }

    /**
     * Creaza o copie a unui deck de carti.
     *
     * @param originalDeck Deck-ul original care va fi copiat.
     * @return O copie a deck-ului original.
     */
    public static ArrayList<Card> resetDeck(final ArrayList<Card> originalDeck) {
        ArrayList<Card> resetDeck = new ArrayList<>();
        for (Card card : originalDeck) {
            resetDeck.add(new Card(card));
        }
        return resetDeck;
    }
}
