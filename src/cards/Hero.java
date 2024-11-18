package cards;

import fileio.CardInput;
import game.GameBoard;

public abstract class Hero extends Card {
    private final int heroHealth = 30;

    /**
     * Constructorul care initializeaza un obiect de tip Hero pe baza unui obiect CardInput.
     *
     * @param cardInput obiectul care contine datele pentru crearea unui Hero
     */
    public Hero(final CardInput cardInput) {
        super(cardInput);
        this.health = heroHealth;
        this.hasAttacked = false;
    }

    /**
     * Metoda abstracta pentru folosirea abilitatii eroului.
     *
     * @param gameBoard   tabla de joc
     * @param affectedRow rândul afectat
     */
    public abstract void useAbility(GameBoard gameBoard, int affectedRow);

}
