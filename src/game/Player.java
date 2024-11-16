package game;

import cards.Card;
import cards.Hero;
import java.util.ArrayList;

public final class Player {
    private String name;
    private final ArrayList<Card> deck;
    private final ArrayList<Card> hand;
    private ArrayList<Card> field;
    private int mana;
    private final Hero hero;
    private int numberOfTanks;

    public Player(final ArrayList<Card> playerDeck, final Hero hero) {
        this.hero = hero;
        this.deck = new ArrayList<>();
        deck.addAll(playerDeck);
        this.hand = new ArrayList<>();
        this.mana = 1;
        this.numberOfTanks = 0;
    }

    public void drawCard() {
        if (!deck.isEmpty()) {
            hand.add(deck.getFirst());
            deck.removeFirst();
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
