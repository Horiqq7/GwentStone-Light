package game;

import cards.Card;
import cards.Hero;
import cards.minions.Goliath;
import cards.minions.Warden;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private ArrayList<Card> field;
    private int mana;
    private int winCount;
    private final Hero hero;
    private boolean endedTurn;
    private int numberOfTanks;

    public Player(ArrayList<Card> deck, Hero hero) {
        this.hero = hero;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.endedTurn = false;
        this.mana = 1;
        this.numberOfTanks = 0;
    }

    public void drawCard() {
        if(!deck.isEmpty()) {
            hand.add(deck.getFirst());
            deck.removeFirst();
        }
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }

    public void setNumberOfTanks(int numberOfTanks) {
        this.numberOfTanks = numberOfTanks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getField() {
        return field;
    }

    public void setField(ArrayList<Card> field) {
        this.field = field;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public Hero getHero() {
        return hero;
    }

    public boolean isEndedTurn() {
        return endedTurn;
    }

    public void setEndedTurn(boolean endedTurn) {
        this.endedTurn = endedTurn;
    }

    private static final int MAX_CARDS_PER_ROW = 5;

}