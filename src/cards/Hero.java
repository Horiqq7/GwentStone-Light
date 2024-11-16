package cards;
import fileio.CardInput;
public class Hero extends Card {
    public Hero(final CardInput cardInput) {
        super(cardInput);
        this.health = 30;
        this.hasAttacked = false;
    }
}
