package cards;

import cards.minions.Disciple;
import cards.minions.Miraj;
import cards.minions.TheCursedOne;
import cards.minions.TheRipper;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa Card reprezinta o carte care poate fi folosita intr-un joc.
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
     * Foloseste abilitatea minionului Disciple.
     *
     * @param ally cartea care va beneficia de abilitatea minionului
     */
    public void useDiscipleAbility(final Card ally) {
        new Disciple().use(this, ally);
    }

    /**
     * Foloseste abilitatea minionului The Cursed One.
     *
     * @param target cartea care va fi afectata de abilitatea acestuia
     */
    public void useTheCursedOneAbility(final Card target) {
        new TheCursedOne().use(this, target);
    }

    /**
     * Foloseste abilitatea minionului Miraj.
     *
     * @param target cartea tinta ale carei valori ale vietii vor fi schimbate cu atacatorul
     */
    public void useMirajAbility(final Card target) {
        new Miraj().use(this, target);
    }

    /**
     * Foloseste abilitatea minionului The Ripper.
     *
     * @param target cartea tinta al carui atac va fi redus
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
     * Scade viata unei carti.
     *
     * @param damage cu cat de mult este scazuta viata
     */
    public void takeDamage(final int damage) {
        health -= damage;
    }

    /**
     * Obtine valoarea manei pentru aceasta carte.
     *
     * @return mana valorii curente ale cartii
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
     * Obtine valoarea attack damage-ului pentru aceasta carte.
     *
     * @return attack damage-ul curent al cartii
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Seteaza valoarea attack damage-ului pentru aceasta carte.
     *
     * @param attackDamage attack damage-ul care trebuie setat
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Seteaza valoarea vietii pentru aceasta carte.
     *
     * @param health valoarea vietii ce este setata
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
     * Seteaza cartea ca fiind inghetata.
     *
     * @param frozen true pentru cand e inghetata, false pentru cand nu e inghetata
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Seteaza starea de atac a acestei carti.
     *
     * @param hasAttacked true daca a atacat, false in caz contrar
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Obtine starea de atac a acestei carti.
     *
     * @return true daca a atacat, false in caz contrar
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }
}
