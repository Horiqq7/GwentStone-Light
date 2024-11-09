package game;

import fileio.ActionsInput;
import fileio.GameInput;
import fileio.StartGameInput;
import game.Actions;
import game.StartGame;

import java.util.ArrayList;

public class Game {
    private StartGame startGame;
    private ArrayList<Actions> actions;
    private int round;
    public Game(GameInput gameInput) {
        ArrayList<ActionsInput> actionsInput = gameInput.getActions();
        this.actions = new ArrayList<>();
        for(ActionsInput actionInput : actionsInput) {
            this.actions.add(new Actions(actionInput));
        }
        this.startGame = new StartGame(gameInput.getStartGame());
    }


    public StartGame getStartGame() {
        return startGame;
    }

    public void setStartGame(StartGame startGame) {
        this.startGame = startGame;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Actions> actions) {
        this.actions = actions;
    }

}