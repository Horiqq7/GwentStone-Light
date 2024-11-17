package game;

import actions.Actions;
import fileio.ActionsInput;
import fileio.GameInput;
import java.util.ArrayList;

public final class Game {
    private final StartGame startGame;
    private final ArrayList<Actions> actions;

    public Game(final GameInput gameInput) {
        ArrayList<ActionsInput> actionsInput = gameInput.getActions();
        this.actions = new ArrayList<>();
        for (ActionsInput actionInput : actionsInput) {
            this.actions.add(new Actions(actionInput));
        }
        this.startGame = new StartGame(gameInput.getStartGame());
    }

    public StartGame getStartGame() {
        return startGame;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }
}
