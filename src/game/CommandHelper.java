package game;

import actions.Actions;
import cards.Card;
import cards.Hero;
import cards.heroes.EmpressThorina;
import cards.heroes.GeneralKocioraw;
import cards.heroes.KingMudface;
import cards.heroes.LordRoyce;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.CardInput;

import java.util.ArrayList;
import static game.GameCommands.*;


public abstract class CommandHelper {
    /**
     * Construieste un obiect JSON pentru o carte.
     *
     * @param mapper Obiectul ObjectMapper folosit pentru a crea nodurile JSON.
     * @param card Cartea pentru care se construieste obiectul JSON.
     * @return Obiectul JSON care reprezinta cartea.
     */
    public static ObjectNode buildCardNode(final ObjectMapper mapper, final Card card) {
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("mana", card.getMana());
        cardNode.put("attackDamage", card.getAttackDamage());
        cardNode.put("health", card.getHealth());
        cardNode.put("description", card.getDescription());

        ArrayNode colors = mapper.createArrayNode();
        for (String color : card.getColors()) {
            colors.add(color);
        }
        cardNode.set("colors", colors);
        cardNode.put("name", card.getName());

        return cardNode;
    }

    /**
     * Creeaza un obiect JSON care reprezinta un pachet de carti.
     *
     * @param mapper Obiectul ObjectMapper folosit pentru a crea nodurile JSON.
     * @param deck Lista de carti care formeaza pachetul.
     * @return Obiectul JSON care reprezinta pachetul de carti.
     */
    public static ArrayNode createDeckArrayNode(final ObjectMapper mapper,
                                                final ArrayList<Card> deck) {
        ArrayNode deckArrayNode = mapper.createArrayNode();

        for (Card card : deck) {
            ObjectNode cardNode = buildCardNode(mapper, card);
            deckArrayNode.add(cardNode);
        }

        return deckArrayNode;
    }

    /**
     * Creeaza un obiect JSON in care include culorile unui erou.
     *
     * @param mapper Obiectul ObjectMapper folosit pentru a crea nodurile JSON.
     * @param hero Eroul pentru care se creeaza obiectul JSON.
     * @return Obiectul JSON care contine culorile eroului.
     */
    public static ArrayNode createHeroColorsArrayNode(final ObjectMapper mapper, final Hero hero) {
        ArrayNode heroColors = mapper.createArrayNode();
        for (String color : hero.getColors()) {
            heroColors.add(color);
        }
        return heroColors;
    }

    /**
     * Plaseaza o carte pe tabla de joc si actualizeaza mana jucatorului.
     *
     * @param gameBoard Tabla de joc unde va fi plasata cartea.
     * @param currentPlayer Jucatorul care joaca.
     * @param playerOne Jucatorul 1.
     * @param playerTwo Jucatorul 2.
     * @param card Cartea care se va plasa pe masa.
     * @param handIdx Indicele cartii in mana jucatorului.
     */
    public static void playCard(final GameBoard gameBoard, final Player currentPlayer,
                                final Player playerOne, final Player playerTwo,
                                final Card card, final int handIdx) {
        int row = determineRowForPlayer(card, currentPlayer == playerOne);
        gameBoard.placeCard(card, row);

        if (currentPlayer == playerOne) {
            playerOne.setMana(playerOne.getMana() - card.getMana());
            playerOne.getHand().remove(handIdx);
            if (card.isTankCard()) {
                playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() + 1);
            }
        } else {
            playerTwo.setMana(playerTwo.getMana() - card.getMana());
            playerTwo.getHand().remove(handIdx);
            if (card.isTankCard()) {
                playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() + 1);
            }
        }
    }

    /**
     * Determina randul pe care trebuie plasata o carte, in functie de numele acesteia.
     *
     * @param card Cartea care trebuie plasata.
     * @param isPlayerOne True daca jucatorul curent este jucatorul 1, false daca este jucatorul 2.
     * @return Randul pe care trebuie plasata cartea.
     */
    static int determineRowForPlayer(final Card card, final boolean isPlayerOne) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden")
                || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
            if (isPlayerOne) {
                return THIRD_ROW;
            } else {
                return SECOND_ROW;
            }
        } else if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
                || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
            if (isPlayerOne) {
                return FOURTH_ROW;
            } else {
                return FIRST_ROW;
            }
        }
        return -1;
    }

    /**
     * Construieste un raspuns de eroare pentru o actiune de tip atac, sub forma unui obiect JSON.
     *
     * @param mapper Obiectul ObjectMapper folosit pentru a crea nodurile JSON.
     * @param action Actiunea care a generat eroarea, continand comanda asociata.
     * @param xAttacker Coordonata x a cartii atacatoare.
     * @param yAttacker Coordonata y a cartii atacatoare.
     * @return Obiectul JSON care reprezinta raspunsul de eroare.
     */
    public static ObjectNode buildErrorResponse(final ObjectMapper mapper, final Actions action,
                                                final int xAttacker, final int yAttacker) {
        ObjectNode errorResponse = mapper.createObjectNode();
        errorResponse.put("command", action.getCommand());

        ObjectNode attackerCoordinates = mapper.createObjectNode();
        attackerCoordinates.put("x", xAttacker);
        attackerCoordinates.put("y", yAttacker);
        errorResponse.set("cardAttacker", attackerCoordinates);

        return errorResponse;
    }


    /**
     * Pregateste un raspuns de eroare pentru o actiune de tip atac, sub forma unui obiect JSON.
     *
     * @param mapper Obiectul ObjectMapper folosit pentru a crea nodurile JSON.
     * @param action Actiunea care a generat eroarea.
     * @param xAttacker Coordonata x a cartii atacatoare.
     * @param yAttacker Coordonata y a cartii atacatoare.
     * @param xAttacked Coordonata x a cartii atacate.
     * @param yAttacked Coordonata y a cartii atacate.
     * @return Obiectul JSON care reprezinta raspunsul de eroare.
     */
    public static ObjectNode prepareErrorResponse(final ObjectMapper mapper, final Actions action,
                                                  final int xAttacker, final int yAttacker,
                                                  final int xAttacked, final int yAttacked) {
        ObjectNode errorResponse = buildErrorResponse(mapper, action, xAttacker, yAttacker);

        ObjectNode attackedCoordinates = mapper.createObjectNode();
        attackedCoordinates.put("x", xAttacked);
        attackedCoordinates.put("y", yAttacked);
        errorResponse.set("cardAttacked", attackedCoordinates);

        return errorResponse;
    }

    /**
     * Verifica daca exista erori legate de cartile atacatoare sau atacate.
     *
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Jucatorul 1.
     * @param playerTwo Jucatorul 2.
     * @param attackerCard Cartea atacatoare.
     * @param attackedCard Cartea atacata.
     * @param xAttacked Coordonata x a cartii atacate (randul).
     * @param errorResponse Obiectul JSON care va contine eroarea.
     * @return True daca exista erori, false altfel.
     */
    static boolean hasCardErrors(final Player currentPlayer, final Player playerOne,
                                 final Player playerTwo, final Card attackerCard,
                                 final Card attackedCard, final int xAttacked,
                                 final ObjectNode errorResponse) {
        boolean errorFrozen = attackerCard.isFrozen();
        boolean errorAlreadyAttacked = attackerCard.getHasAttacked();
        boolean errorNotEnemyCard = false;
        boolean errorTankCard = false;

        if (currentPlayer == playerOne) {
            if (xAttacked == THIRD_ROW || xAttacked == FOURTH_ROW) {
                errorNotEnemyCard = true;
            } else if (playerTwo.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                errorTankCard = true;
            }
        } else {
            if (xAttacked == FIRST_ROW || xAttacked == SECOND_ROW) {
                errorNotEnemyCard = true;
            } else if (playerOne.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                errorTankCard = true;
            }
        }

        if (errorFrozen) {
            errorResponse.put("error", "Attacker card is frozen.");
        } else if (errorNotEnemyCard) {
            errorResponse.put("error", "Attacked card does not belong to the enemy.");
        } else if (errorAlreadyAttacked) {
            errorResponse.put("error", "Attacker card has already attacked this turn.");
        } else if (errorTankCard) {
            errorResponse.put("error", "Attacked card is not of type 'Tank'.");
        }

        return errorFrozen || errorAlreadyAttacked || errorTankCard || errorNotEnemyCard;
    }

    /**
     * Verifica daca exista erori legate de abilitatile cartilor.
     *
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Jucatorul 1.
     * @param playerTwo Jucatorul 2.
     * @param attackerCard Cartea atacatoare.
     * @param attackedCard Cartea atacata.
     * @param xAttacked Coordonata x a cartii atacate.
     * @param errorResponse Obiectul JSON care va contine eroarea.
     * @return True daca exista erori, false altfel.
     */
    static boolean hasAbilityErrors(final Player currentPlayer, final Player playerOne,
                                    final Player playerTwo, final Card attackerCard,
                                    final Card attackedCard, final int xAttacked,
                                    final ObjectNode errorResponse) {
        boolean errorFrozen = attackerCard.isFrozen();
        boolean errorAlreadyAttacked = attackerCard.getHasAttacked();
        boolean errorInvalidDiscipleTarget = false;
        boolean errorNotEnemyCard = false;
        boolean errorInvalidTankCard = false;

        if (currentPlayer == playerOne) {
            if (attackerCard.getName().equals("Disciple")) {
                if (xAttacked == FIRST_ROW || xAttacked == SECOND_ROW) {
                    errorInvalidDiscipleTarget = true;
                }
            } else if (attackerCard.getName().equals("The Cursed One")
                    || attackerCard.getName().equals("The Ripper")
                    || attackerCard.getName().equals("Miraj")) {
                if (xAttacked == THIRD_ROW || xAttacked == FOURTH_ROW) {
                    errorNotEnemyCard = true;
                } else if (playerTwo.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                    errorInvalidTankCard = true;
                }
            }
        } else {
            if (attackerCard.getName().equals("Disciple")) {
                if (xAttacked == THIRD_ROW || xAttacked == FOURTH_ROW) {
                    errorInvalidDiscipleTarget = true;
                }
            } else if (attackerCard.getName().equals("The Cursed One")
                    || attackerCard.getName().equals("The Ripper")
                    || attackerCard.getName().equals("Miraj")) {
                if (xAttacked == FIRST_ROW || xAttacked == SECOND_ROW) {
                    errorNotEnemyCard = true;
                } else if (playerOne.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                    errorInvalidTankCard = true;
                }
            }
        }
        if (errorFrozen) {
            errorResponse.put("error", "Attacker card is frozen.");
        } else if (errorNotEnemyCard) {
            errorResponse.put("error", "Attacked card does not belong to the enemy.");
        } else if (errorAlreadyAttacked) {
            errorResponse.put("error", "Attacker card has already attacked this turn.");
        } else if (errorInvalidTankCard) {
            errorResponse.put("error", "Attacked card is not of type 'Tank'.");
        } else if (errorInvalidDiscipleTarget) {
            errorResponse.put("error", "Attacked card does not belong to the current player.");
        }

        return errorFrozen || errorAlreadyAttacked || errorInvalidTankCard
                || errorNotEnemyCard || errorInvalidDiscipleTarget;
    }

    /**
     * Executa abilitatea unei carti asupra unei alte carti.
     *
     * @param attackerCard Cartea care ataca.
     * @param attackedCard Cartea care este atacata.
     * @param gameBoard Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Jucatorul 1.
     * @param playerTwo Jucatorul 2.
     */
    static void performAbility(final Card attackerCard, final Card attackedCard,
                               final GameBoard gameBoard, final Player currentPlayer,
                               final Player playerOne, final Player playerTwo) {
        switch (attackerCard.getName()) {
            case "Disciple" -> attackerCard.useDiscipleAbility(attackedCard);
            case "The Cursed One" -> attackerCard.useTheCursedOneAbility(attackedCard);
            case "The Ripper" -> attackerCard.useTheRipperAbility(attackedCard);
            case "Miraj" -> attackerCard.useMirajAbility(attackedCard);
        }
        if (attackedCard.getHealth() <= 0) {
            if (attackedCard.isTankCard()) {
                if (currentPlayer == playerTwo) {
                    playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() - 1);
                } else {
                    playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() - 1);
                }
            }
            gameBoard.getBoard().remove(attackedCard);
        }
        attackerCard.setHasAttacked(true);
    }

    /**
     * Executa un atac asupra unei carti.
     *
     * @param attackerCard Cartea care ataca.
     * @param attackedCard Cartea care este atacata.
     * @param gameBoard Tabla de joc.
     * @param xAttacked Coordonata x a cartii atacate.
     * @param yAttacked Coordonata y a cartii atacate.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Jucatorul 1.
     * @param playerTwo Jucatorul 2.
     */
    static void performAttack(final Card attackerCard, final Card attackedCard,
                              final GameBoard gameBoard, final int xAttacked,
                              final int yAttacked, final Player currentPlayer,
                              final Player playerOne, final Player playerTwo) {
        attackerCard.attackCard(attackerCard, attackedCard);
        if (attackedCard.getHealth() <= 0) {
            gameBoard.getBoard().get(xAttacked).remove(yAttacked);
            if (attackedCard.isTankCard()) {
                if (currentPlayer == playerTwo) {
                    playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() - 1);
                } else {
                    playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() - 1);
                }
            }
        }
    }

    /**
     * Verifica daca o carte este inghetata sau daca a atacat deja.
     *
     * @param attackerCard Cartea atacatoare.
     * @param errorResponse Obiectul JSON care va contine eroarea.
     * @return True e inghetata sau a atacat deja, false altfel
     */
    public static boolean isCardFrozenOrAttacked(final Card attackerCard,
                                                 final ObjectNode errorResponse) {
        if (attackerCard.isFrozen()) {
            errorResponse.put("error", "Attacker card is frozen.");
            return true;
        }

        if (attackerCard.getHasAttacked()) {
            errorResponse.put("error", "Attacker card has already attacked this turn.");
            return true;
        }

        return false;
    }

    /**
     * Obtine eroul inamic in functie de jucatorul curent.
     *
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Primul jucator.
     * @param playerTwo Al doilea jucator.
     * @return Eroul inamic.
     */
    public static Hero getEnemyHero(final Player currentPlayer, final Player playerOne,
                                    final Player playerTwo) {
        if (currentPlayer == playerOne) {
            return playerTwo.getHero();
        } else {
            return playerOne.getHero();
        }
    }

    /**
     * Verifica daca inamicii au un tank pe tabla de joc.
     *
     * @param gameBoard Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Primul jucator.
     * @return true daca inamicii au un tank, altfel false.
     */
    public static boolean checkForEnemyTanks(final GameBoard gameBoard, final Player currentPlayer,
                                             final Player playerOne) {
        boolean enemyHasTank = false;
        int enemyRowsStart;
        int enemyRowsEnd;
        if (currentPlayer == playerOne) {
            enemyRowsStart = 0;
            enemyRowsEnd = 2;
        } else {
            enemyRowsStart = 2;
            enemyRowsEnd = gameBoard.getBoard().size();
        }
        for (int i = enemyRowsStart; i < enemyRowsEnd; i++) {
            for (Card card : gameBoard.getBoard().get(i)) {
                if (card.isTankCard()) {
                    enemyHasTank = true;
                    break;
                }
            }
            if (enemyHasTank) {
                break;
            }
        }
        return enemyHasTank;
    }

    /**
     * Verifica daca atacul cu un tank este invalid.
     *
     * @param enemyHasTank Indica daca inamicul are un tank.
     * @param errorResponse Raspunsul de eroare in cazul unui atac invalid.
     * @return true daca atacul este invalid, altfel false.
     */
    public static boolean isInvalidTankAttack(final boolean enemyHasTank,
                                              final ObjectNode errorResponse) {
        if (enemyHasTank) {
            errorResponse.put("error", "Attacked card is not of type 'Tank'.");
            return true;
        }
        return false;
    }

    /**
     * Incheie jocul in cazul in care un jucator a castigat.
     *
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Primul jucator.
     * @param gameBoard Tabla de joc.
     * @param output Lista de iesire pentru a adauga rezultatul jocului.
     * @param mapper Obiectul ObjectMapper pentru crearea obiectului JSON.
     */
    public static void endGame(final Player currentPlayer, final Player playerOne,
                               final GameBoard gameBoard, final ArrayNode output,
                               final ObjectMapper mapper) {
        if (!gameBoard.isGameEnded()) {
            gameBoard.endGame();
            ObjectNode gameEndedNode = mapper.createObjectNode();
            if (currentPlayer == playerOne) {
                incrementPlayerOneWins();
                gameEndedNode.put("gameEnded", "Player one killed the enemy hero.");
            } else {
                incrementPlayerTwoWins();
                gameEndedNode.put("gameEnded", "Player two killed the enemy hero.");
            }
            output.add(gameEndedNode);
        }
    }

    /**
     * Efectueaza atacul eroului asupra eroului inamic.
     *
     * @param attackerCard Cardul atacator.
     * @param enemyHero Eroul inamic.
     * @param gameBoard Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Primul jucator.
     * @param output Lista de iesire pentru a adauga rezultatele.
     * @param mapper Obiectul ObjectMapper pentru crearea obiectelor JSON.
     */
    public static void performAttackHero(final Card attackerCard, final Hero enemyHero,
                                         final GameBoard gameBoard, final Player currentPlayer,
                                         final Player playerOne, final ArrayNode output,
                                         final ObjectMapper mapper) {
        enemyHero.setHealth(enemyHero.getHealth() - attackerCard.getAttackDamage());
        if (enemyHero.getHealth() <= 0) {
            endGame(currentPlayer, playerOne, gameBoard, output, mapper);
        } else {
            attackerCard.setHasAttacked(true);
        }
    }

    /**
     * Verifica daca jucatorul are suficienta mana pentru a folosi abilitatea eroului.
     *
     * @param currentPlayer Jucatorul curent.
     * @param hero Eroul curent.
     * @param errorUseHeroAbility Obiectul JSON pentru eroare
     * in cazul in care nu are suficienta mana.
     * @param output Lista de iesire pentru a adauga mesajul de eroare.
     * @return true daca nu are suficienta mana, altfel false.
     */
    public static boolean checkHeroMana(final Player currentPlayer, final Hero hero,
                                        final ObjectNode errorUseHeroAbility,
                                        final ArrayNode output) {
        if (currentPlayer.getMana() < hero.getMana()) {
            errorUseHeroAbility.put("error", "Not enough mana to use hero's ability.");
            output.add(errorUseHeroAbility);
            return true;
        }
        return false;
    }

    /**
     * Verifica daca eroul a atacat deja in acea tura.
     *
     * @param hero Eroul curent.
     * @param errorUseHeroAbility Obiectul JSON pentru eroare in cazul in care eroul a atacat deja.
     * @param output Lista de iesire pentru a adauga mesajul de eroare.
     * @return true daca eroul a atacat deja, altfel false.
     */
    public static boolean checkHeroHasAttacked(final Hero hero,
                                               final ObjectNode errorUseHeroAbility,
                                               final ArrayNode output) {
        if (hero.getHasAttacked()) {
            errorUseHeroAbility.put("error", "Hero has already attacked this turn.");
            output.add(errorUseHeroAbility);
            return true;
        }
        return false;
    }

    /**
     * Verifica daca randul selectat pentru abilitatea eroului este valid.
     *
     * @param hero Eroul curent.
     * @param affectedRow Randul afectat de abilitatea eroului.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne Primul jucator.
     * @param playerTwo Al doilea jucator.
     * @param errorUseHeroAbility Obiectul JSON pentru eroare in cazul in care randul nu este valid.
     * @param output Lista de iesire pentru a adauga mesajul de eroare.
     * @return true daca randul nu este valid, altfel false.
     */
    public static boolean checkValidRow(final Hero hero, final int affectedRow,
                                        final Player currentPlayer, final Player playerOne,
                                        final Player playerTwo,
                                        final ObjectNode errorUseHeroAbility,
                                        final ArrayNode output) {
        boolean errorEnemyRow = false;
        boolean errorPlayerRow = false;

        if (hero.getName().equals("Empress Thorina")
                || hero.getName().equals("Lord Royce")) {
            if (currentPlayer == playerOne) {
                if (affectedRow == THIRD_ROW || affectedRow == FOURTH_ROW) {
                    errorEnemyRow = true;
                }
            } else if (currentPlayer == playerTwo) {
                if (affectedRow == FIRST_ROW || affectedRow == SECOND_ROW) {
                    errorEnemyRow = true;
                }
            }
        } else if (hero.getName().equals("General Kocioraw")
                || hero.getName().equals("King Mudface")) {
            if (currentPlayer == playerOne) {
                if (affectedRow == FIRST_ROW || affectedRow == SECOND_ROW) {
                    errorPlayerRow = true;
                }
            } else if (currentPlayer == playerTwo) {
                if (affectedRow == THIRD_ROW || affectedRow == FOURTH_ROW) {
                    errorPlayerRow = true;
                }
            }
        }

        if (errorEnemyRow) {
            errorUseHeroAbility.put("error", "Selected row does not belong to the enemy.");
            output.add(errorUseHeroAbility);
            return true;
        }

        if (errorPlayerRow) {
            errorUseHeroAbility.put("error", "Selected row does not belong to the current player.");
            output.add(errorUseHeroAbility);
            return true;
        }

        return false;
    }

    /**
     * Creeaza un erou in functie de datele primite.
     *
     * @param heroInput Datele de intrare pentru erou.
     * @return Eroul creat.
     * @throws IllegalArgumentException Daca numele eroului este necunoscut.
     */
    public static Hero createHero(final CardInput heroInput) {
        switch (heroInput.getName()) {
            case "Lord Royce":
                return new LordRoyce(heroInput);
            case "Empress Thorina":
                return new EmpressThorina(heroInput);
            case "King Mudface":
                return new KingMudface(heroInput);
            case "General Kocioraw":
                return new GeneralKocioraw(heroInput);
            default:
                return null;
        }
    }
}
