package cards;

import fileio.CardInput;

import java.util.List;

public class Card {
    protected int mana;
    protected int attackDamage;
    protected int health;
    protected String description;
    protected String name;
    protected boolean frozen;
    protected boolean hasAttacked;
    protected List<String> colors;

    public Card(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.attackDamage = cardInput.getAttackDamage();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.frozen = false;
    }

    public void attackCard(Card attacker, Card target) {
            target.takeDamage(attacker.getAttackDamage());
            attacker.hasAttacked = true;
    }

    public void useDiscipleAbility(Card ally){
        ally.setHealth(ally.getHealth() + 2);
    }

    public void useTheCursedOneAbility(Card target) {
            int tempAttack = target.getAttackDamage();
            if (tempAttack == 0) {
                target.setHealth(0);
            } else {
                target.setAttackDamage(target.getHealth());
                target.setHealth(tempAttack);
            }
    }

    public void useMirajAbility(Card target) {

        int tempHealth = this.health;
        this.health = target.getHealth();
        target.setHealth(tempHealth);
    }

    public void useTheRipperAbility(Card target) {
        int reducedAttack = target.getAttackDamage() - 2;
        target.setAttackDamage(Math.max(reducedAttack, 0));
    }

    public boolean isTankCard() {
        if(this.name.equals("Goliath")) {
            return true;
        } else if(this.name.equals("Warden")) {
            return true;
        }
        return false;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public int getMana() { return mana; }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean getHasAttacked() {
        return hasAttacked;
    }

    public void useHeroSpecialAbility(List<Minion> targetRow) {
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void useMinionAbility(Card card) {

    }
}