package game;

import cards.Card;
import cards.Hero;
import java.util.ArrayList;

public final class Player {
    private final ArrayList<Card> deck; // Deck-ul jucatorului
    private final ArrayList<Card> hand; // Cartile in mana
    private int mana; // Mana disponibila
    private final Hero hero; // Eroul jucatorului
    private int numberOfTanks; // Numarul de carti Tank pe teren

    public Player(final ArrayList<Card> playerDeck, final Hero hero) {
        this.hero = hero;
        this.deck = new ArrayList<>();
        deck.addAll(playerDeck);
        this.hand = new ArrayList<>();
        this.mana = 1;
        this.numberOfTanks = 0;
    }

    /**
     * Trage o carte din pachetul jucatorului si o adauga in mana acestuia.
     * Daca pachetul nu este gol, prima carte este eliminata din pachet
     * si adaugata in mana jucatorului.
     */
    public void drawCard() {
        if (!deck.isEmpty()) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }

    public void setNumberOfTanks(final int numberOfTanks) {
        this.numberOfTanks = numberOfTanks;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public Hero getHero() {
        return hero;
    }
}
