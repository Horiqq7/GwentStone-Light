package game;

import actions.Actions;
import cards.*;
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

public class CommandHelper {

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

    public static ArrayNode createDeckArrayNode(final ObjectMapper mapper, final ArrayList<Card> deck) {
        ArrayNode deckArrayNode = mapper.createArrayNode();

        for (Card card : deck) {
            ObjectNode cardNode = buildCardNode(mapper, card);
            deckArrayNode.add(cardNode);
        }

        return deckArrayNode;
    }

    public static ArrayNode createHeroColorsArrayNode(final ObjectMapper mapper, final Hero hero) {
        ArrayNode heroColors = mapper.createArrayNode();
        for (String color : hero.getColors()) {
            heroColors.add(color);
        }
        return heroColors;
    }

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

    static int determineRowForPlayer(final Card card, boolean isPlayerOne) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden")
                || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
            return isPlayerOne ? THIRD_ROW : SECOND_ROW;
        } else if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
                || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
            return isPlayerOne ? FOURTH_ROW : FIRST_ROW;
        }
        return -1;
    }

    public static ObjectNode prepareErrorResponse(ObjectMapper mapper, Actions action,
                                                  int xAttacker, int yAttacker, int xAttacked, int yAttacked) {
        ObjectNode errorResponse = mapper.createObjectNode();
        errorResponse.put("command", action.getCommand());

        ObjectNode attackerCoordinates = mapper.createObjectNode();
        attackerCoordinates.put("x", xAttacker);
        attackerCoordinates.put("y", yAttacker);
        errorResponse.set("cardAttacker", attackerCoordinates);

        ObjectNode attackedCoordinates = mapper.createObjectNode();
        attackedCoordinates.put("x", xAttacked);
        attackedCoordinates.put("y", yAttacked);
        errorResponse.set("cardAttacked", attackedCoordinates);

        return errorResponse;
    }

    static boolean hasCardErrors(Player currentPlayer, Player playerOne, Player playerTwo, Card attackerCard, Card attackedCard, int xAttacked, ObjectNode errorResponse, ObjectMapper mapper) {
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

    static boolean hasAbilityErrors(Player currentPlayer, Player playerOne, Player playerTwo, Card attackerCard, Card attackedCard, int xAttacked, ObjectNode errorResponse, ObjectMapper mapper) {
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
            } else if (attackerCard.getName().equals("The Cursed One") || attackerCard.getName().equals("The Ripper") || attackerCard.getName().equals("Miraj")) {
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
            } else if (attackerCard.getName().equals("The Cursed One") || attackerCard.getName().equals("The Ripper") || attackerCard.getName().equals("Miraj")) {
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

        return errorFrozen || errorAlreadyAttacked || errorInvalidTankCard || errorNotEnemyCard || errorInvalidDiscipleTarget;
    }

    static void performAbility(Card attackerCard, Card attackedCard, GameBoard gameBoard, int xAttacked, int yAttacked, Player currentPlayer, Player playerOne, Player playerTwo) {
        if (attackerCard.getName().equals("Disciple")) {
            attackerCard.useDiscipleAbility(attackedCard);
        } else if (attackerCard.getName().equals("The Cursed One")) {
            attackerCard.useTheCursedOneAbility(attackedCard);
        } else if (attackerCard.getName().equals("The Ripper")) {
            attackerCard.useTheRipperAbility(attackedCard);
        } else if (attackerCard.getName().equals("Miraj")) {
            attackerCard.useMirajAbility(attackedCard);
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

    static void performAttack(Card attackerCard, Card attackedCard, GameBoard gameBoard, int xAttacked, int yAttacked, Player currentPlayer, Player playerOne, Player playerTwo) {
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

    public static ObjectNode buildErrorResponse(ObjectMapper mapper, Actions action, int xAttacker, int yAttacker) {
        ObjectNode errorResponse = mapper.createObjectNode();
        errorResponse.put("command", action.getCommand());

        ObjectNode attackerCoordinates = mapper.createObjectNode();
        attackerCoordinates.put("x", xAttacker);
        attackerCoordinates.put("y", yAttacker);
        errorResponse.set("cardAttacker", attackerCoordinates);

        return errorResponse;
    }

    public static boolean isCardFrozenOrAttacked(Card attackerCard, ObjectNode errorResponse) {
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

    public static Hero getEnemyHero(Player currentPlayer, Player playerOne, Player playerTwo) {
        return (currentPlayer == playerOne) ? playerTwo.getHero() : playerOne.getHero();
    }

    public static boolean checkForEnemyTanks(GameBoard gameBoard, Player currentPlayer, Player playerOne, Player playerTwo) {
        boolean enemyHasTank = false;
        int enemyRowsStart = (currentPlayer == playerOne) ? 0 : 2;
        int enemyRowsEnd = (currentPlayer == playerOne) ? 2 : gameBoard.getBoard().size();

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

    public static boolean isInvalidTankAttack(Card attackerCard, boolean enemyHasTank, ObjectNode errorResponse) {
        if (enemyHasTank && !attackerCard.isTankCard()) {
            errorResponse.put("error", "Attacked card is not of type 'Tank'.");
            return true;
        }

        return false;
    }

    public static void endGame(Player currentPlayer, Player playerOne,
                               GameBoard gameBoard, ArrayNode output, ObjectMapper mapper) {
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

    public static void performAttackHero(Card attackerCard, Hero enemyHero, GameBoard gameBoard,
                                         Player currentPlayer, Player playerOne, Player playerTwo,
                                         ArrayNode output, ObjectMapper mapper) {
        enemyHero.setHealth(enemyHero.getHealth() - attackerCard.getAttackDamage());

        if (enemyHero.getHealth() <= 0) {
            endGame(currentPlayer, playerOne, gameBoard, output, mapper);
        } else {
            attackerCard.setHasAttacked(true);
        }
    }

    public static boolean checkHeroMana(Player currentPlayer, Hero hero, ObjectNode errorUseHeroAbility, ArrayNode output) {
        if (currentPlayer.getMana() < hero.getMana()) {
            errorUseHeroAbility.put("error", "Not enough mana to use hero's ability.");
            output.add(errorUseHeroAbility);
            return true;
        }
        return false;
    }

    public static boolean checkHeroHasAttacked(Hero hero, ObjectNode errorUseHeroAbility, ArrayNode output) {
        if (hero.getHasAttacked()) {
            errorUseHeroAbility.put("error", "Hero has already attacked this turn.");
            output.add(errorUseHeroAbility);
            return true;
        }
        return false;
    }

    public static boolean checkValidRow(Hero hero, int affectedRow, Player currentPlayer,
                                        Player playerOne, Player playerTwo, ObjectNode errorUseHeroAbility,
                                        ArrayNode output) {
        boolean errorEnemyRow = false;
        boolean errorPlayerRow = false;

        if (hero.getName().equals("Empress Thorina") || hero.getName().equals("Lord Royce")) {
            if (currentPlayer == playerOne) {
                if (affectedRow == THIRD_ROW || affectedRow == FOURTH_ROW) {
                    errorEnemyRow = true;
                }
            } else if (currentPlayer == playerTwo) {
                if (affectedRow == FIRST_ROW || affectedRow == SECOND_ROW) {
                    errorEnemyRow = true;
                }
            }
        } else if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
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
                throw new IllegalArgumentException("Unknown hero: " + heroInput.getName());
        }
    }
}
