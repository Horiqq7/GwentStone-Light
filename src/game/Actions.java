package game;

import fileio.ActionsInput;
import fileio.Coordinates;

public final class Actions {
    private final String command;
    private final int handIdx;
    private final Coordinates cardAttacker;
    private final Coordinates cardAttacked;
    private final int affectedRow;
    private final int playerIdx;
    private final int x;
    private final int y;

    public Actions(final ActionsInput actionsInput) {
        this.command = actionsInput.getCommand();
        this.handIdx = actionsInput.getHandIdx();
        this.cardAttacker = actionsInput.getCardAttacker();
        this.cardAttacked = actionsInput.getCardAttacked();
        this.affectedRow = actionsInput.getAffectedRow();
        this.playerIdx = actionsInput.getPlayerIdx();
        this.x = actionsInput.getX();
        this.y = actionsInput.getY();
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
