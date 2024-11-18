package cards;

public abstract class Minion {
    /**
     * Executa abilitatea specifica a unui minion asupra unei tinte.
     *
     * @param card cartea care folosește abilitatea
     * @param target cartea asupra căreia se aplică abilitatea
     */
    public abstract void use(Card card, Card target);
}
