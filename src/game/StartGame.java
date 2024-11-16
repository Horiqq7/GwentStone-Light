package game;

import fileio.CardInput;
import fileio.StartGameInput;

public final class StartGame {
    private final int playerOneDeckIdx;
    private final int playerTwoDeckIdx;
    private final int shuffleSeed;
    private final CardInput playerOneHero;
    private final CardInput playerTwoHero;
    private final int startingPlayer;
    public StartGame(final StartGameInput startGameInput) {
        this.playerOneDeckIdx = startGameInput.getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = startGameInput.getPlayerTwoDeckIdx();
        this.shuffleSeed = startGameInput.getShuffleSeed();
        this.playerOneHero = startGameInput.getPlayerOneHero();
        this.playerTwoHero = startGameInput.getPlayerTwoHero();
        this.startingPlayer = startGameInput.getStartingPlayer();
    }

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public CardInput getPlayerOneHero() {
        return playerOneHero;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public CardInput getPlayerTwoHero() {
        return playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }
}
