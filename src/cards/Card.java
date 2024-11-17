package cards;

import cards.minions.Disciple;
import cards.minions.Miraj;
import cards.minions.TheCursedOne;
import cards.minions.TheRipper;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa Card reprezinta o carte care poate fi folosita intr-un joc de carti.
 * Aceasta clasa este extinsa de catre alte clase care definesc comportamente
 * si abilitatile speciale ale cartilor.
 */
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
     * Constructor de copiere pentru crearea unei noi carti ca o copie a alteia.
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

    /**
     * Foloseste abilitatea Disciple care creste viata unei carti aliate cu 2.
     *
     * @param ally cartea care va beneficia de abilitatea Disciple
     */
    public void useDiscipleAbility(final Card ally) {
        new Disciple().use(this, ally);
    }

    /**
     * Foloseste abilitatea The Cursed One care schimba atacul si viata unei carti tinta.
     *
     * @param target cartea care va fi afectata de abilitatea The Cursed One
     */
    public void useTheCursedOneAbility(final Card target) {
        new TheCursedOne().use(this, target);
    }

    /**
     * Foloseste abilitatea Miraj care schimba viata dintre atacator si tinta.
     *
     * @param target cartea tinta ale carei valori ale vietii vor fi schimbate cu atacatorul
     */
    public void useMirajAbility(final Card target) {
        new Miraj().use(this, target);
    }

    /**
     * Foloseste abilitatea The Ripper care reduce atacul unei carti tinta cu 2.
     *
     * @param target cartea tinta ale carei atacuri vor fi reduse
     */
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
     * Obtine valoarea manei pentru aceasta carte.
     *
     * @return mana valorii curente ale carții
     */
    public int getMana() {
        return mana;
    }

    /**
     * Seteaza valoarea manei pentru aceasta carte.
     *
     * @param mana valoarea de mana pentru a fi setata
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Obtine valoarea daunei de atac pentru aceasta carte.
     *
     * @return atacul curent al cartii
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Seteaza valoarea daunei de atac pentru aceasta carte.
     *
     * @param attackDamage valoarea de atac care trebuie setata
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Seteaza valoarea vietii pentru aceasta carte.
     *
     * @param health valoarea de viata pentru a fi setata
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Obtine valoarea vietii pentru aceasta carte.
     *
     * @return valoarea curenta a vietii
     */
    public int getHealth() {
        return health;
    }

    /**
     * Obtine descrierea acestei carti.
     *
     * @return descrierea curenta a cartii
     */
    public String getDescription() {
        return description;
    }

    /**
     * Seteaza descrierea pentru aceasta carte.
     *
     * @param description descrierea care trebuie setata
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Obtine lista de culori asociate acestei carti.
     *
     * @return lista culorilor cartii
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     * Obtine numele acestei carti.
     *
     * @return numele cartii
     */
    public String getName() {
        return name;
    }

    /**
     * Verifica daca aceasta carte este inghetata.
     *
     * @return true daca este inghetata, false in caz contrar
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Seteaza starea de inghet a acestei carti.
     *
     * @param frozen true pentru inghetat, false pentru cand nu e inghetat
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Seteaza starea de atac al acestei carti.
     *
     * @param hasAttacked true daca a atacat, false in caz contrar
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Obtine starea de atac al acestei carti.
     *
     * @return true daca a atacat, false in caz contrar
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }
}
