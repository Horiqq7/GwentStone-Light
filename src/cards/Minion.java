package cards;

public abstract class Minion {
    /**
     * Execută abilitatea specifică a unui minion asupra unei ținte.
     *
     * @param user cartea care folosește abilitatea
     * @param target cartea asupra căreia se aplică abilitatea
     */
    public abstract void use(Card user, Card target);
}
