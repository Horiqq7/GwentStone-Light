package game;

import fileio.CardInput;
import fileio.DecksInput;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import cards.Card;

public class Decks {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<Card>> decks;
    public Decks(DecksInput decksInput) {
        this.nrCardsInDeck = decksInput.getNrCardsInDeck();
        this.nrDecks = decksInput.getNrDecks();
        this.decks = new ArrayList<ArrayList<Card>>();
        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> deck = new ArrayList<>();
            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput);
                deck.add(card);
            }
            decks.add(deck);
        }
    }

    public ArrayList<Card> deckIndex (int index){
        return this.decks.get(index);
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }
}