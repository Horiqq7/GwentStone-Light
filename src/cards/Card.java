package cards;

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

    public Card(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.attackDamage = cardInput.getAttackDamage();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.frozen = false;
    }

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

    public void attackCard(final Card attacker, final Card target) {
            target.takeDamage(attacker.getAttackDamage());
            attacker.hasAttacked = true;
    }

    public void useDiscipleAbility(final Card ally) {
        ally.setHealth(ally.getHealth() + 2);
    }

    public void useTheCursedOneAbility(final Card target) {
            int tempAttack = target.getAttackDamage();
            if (tempAttack == 0) {
                target.setHealth(0);
            } else {
                target.setAttackDamage(target.getHealth());
                target.setHealth(tempAttack);
            }
    }

    public void useMirajAbility(final Card target) {
        int tempHealth = this.health;
        this.health = target.getHealth();
        target.setHealth(tempHealth);
    }

    public void useTheRipperAbility(final Card target) {
        int reducedAttack = target.getAttackDamage() - 2;
        target.setAttackDamage(Math.max(reducedAttack, 0));
    }

    public boolean isTankCard() {
        return this.name.equals("Goliath") || this.name.equals("Warden");
    }

    public void takeDamage(final int damage) {
        health -= damage;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean getHasAttacked() {
        return hasAttacked;
    }
}
