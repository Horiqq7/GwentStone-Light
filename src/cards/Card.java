package cards;

import cards.minions.Disciple;
import cards.minions.Miraj;
import cards.minions.TheCursedOne;
import cards.minions.TheRipper;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private int mana;
    private int attackDamage;
    protected int health;
    private String description;
    private final String name;
    private boolean frozen;
    protected boolean hasAttacked;
    private final List<String> colors;

    /**
     * Constructorul care initializeaza o carte folosind datele din CardInput.
     *
     * @param cardInput obiectul care contine datele pentru creare
     */
    public Card(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.attackDamage = cardInput.getAttackDamage();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.frozen = false;
    }

    /**
     * Constructor de copiere pentru crearea unei noi carți ca o copie a alteia.
     *
     * @param other cartea care se copiaza
     */
    public Card(final Card other) {
        this.mana = other.mana;
        this.attackDamage = other.attackDamage;
        this.health = other.health;
        this.description = other.description;
        this.colors = new ArrayList<>(other.colors);
        this.name = other.name;
        this.frozen = other.frozen;
        this.hasAttacked = other.hasAttacked;
    }

    /**
     * Permite unui atacator sa atace o alta carte, reducand viata tintei.
     *
     * @param attacker cartea care efectueaza atacul
     * @param target cartea care este atacata
     */
    public void attackCard(final Card attacker, final Card target) {
        target.takeDamage(attacker.getAttackDamage());
        attacker.hasAttacked = true;
    }

    public void useDiscipleAbility(final Card ally) {
        new Disciple().use(this, ally);
    }

    public void useTheCursedOneAbility(final Card target) {
        new TheCursedOne().use(this, target);
    }

    public void useMirajAbility(final Card target) {
        new Miraj().use(this, target);
    }

    public void useTheRipperAbility(final Card target) {
        new TheRipper().use(this, target);
    }

    /**
     * Verifica daca aceasta carte este un tank (Goliath sau Warden).
     *
     * @return true daca este un tank, false in caz contrar
     */
    public boolean isTankCard() {
        return this.name.equals("Goliath") || this.name.equals("Warden");
    }

    /**
     * Aplica daune carții, reducandu-i viata.
     *
     * @param damage cantitatea de daune primite
     */
    public void takeDamage(final int damage) {
        health -= damage;
    }
    /**
     * Obține valoarea manei pentru această carte.
     *
     * @return mana valorii curente ale carții
     */
    public int getMana() {
        return mana;
    }

    /**
     * Setează valoarea manei pentru această carte.
     *
     * @param mana valoarea de mana pentru a fi setata
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Obține valoarea daunei de atac pentru această carte.
     *
     * @return atacul curent al carții
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Setează valoarea daunei de atac pentru această carte.
     *
     * @param attackDamage valoarea de atac care trebuie setata
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Setează valoarea vieții pentru această carte.
     *
     * @param health valoarea de viata pentru a fi setata
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Obține valoarea vieții pentru această carte.
     *
     * @return valoarea curentă a vieții
     */
    public int getHealth() {
        return health;
    }

    /**
     * Obține descrierea acestei cărți.
     *
     * @return descrierea curentă a carții
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setează descrierea pentru această carte.
     *
     * @param description descrierea care trebuie setata
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Obține lista de culori asociate acestei cărți.
     *
     * @return lista culorilor carții
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     * Obține numele acestei cărți.
     *
     * @return numele carții
     */
    public String getName() {
        return name;
    }

    /**
     * Verifică dacă această carte este înghețată.
     *
     * @return true dacă este înghețată, false în caz contrar
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Setează starea de îngheț a acestei cărți.
     *
     * @param frozen true pentru înghețat, false pentru nu înghețat
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Setează starea de atac al acestei cărți.
     *
     * @param hasAttacked true dacă a atacat, false în caz contrar
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Obține starea de atac al acestei cărți.
     *
     * @return true dacă a atacat, false în caz contrar
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }
}
