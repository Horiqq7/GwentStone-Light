package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cards.Card;
import cards.Hero;
import game.Game;
import game.GameBoard;
import game.Player;


import java.util.ArrayList;

public class GameHelper {

    public static ObjectNode getPlayerDeck(ObjectMapper mapper, String command, int playerIdx, ArrayList<Card> deck) {

        ObjectNode playerDeckObjectNode = mapper.createObjectNode();
        playerDeckObjectNode.put("command", command);
        playerDeckObjectNode.put("playerIdx", playerIdx);

        ArrayNode deckArrayNode = mapper.createArrayNode();
        for (Card card : deck) {

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
            deckArrayNode.add(cardNode);
        }

        playerDeckObjectNode.set("output", deckArrayNode);

        return playerDeckObjectNode;
    }

    public static ObjectNode getPlayerHero(ObjectMapper mapper, String command, int playerIdx, Hero hero) {

        ObjectNode heroObjectNode = mapper.createObjectNode();
        heroObjectNode.put("command", command);
        heroObjectNode.put("playerIdx", playerIdx);

        ObjectNode heroDetails = mapper.createObjectNode();
        heroDetails.put("mana", hero.getMana());
        heroDetails.put("description", hero.getDescription());

        ArrayNode heroColors = mapper.createArrayNode();
        for (String color : hero.getColors()) {
            heroColors.add(color);
        }
        heroDetails.set("colors", heroColors);

        heroDetails.put("name", hero.getName());
        heroDetails.put("health", hero.getHealth());

        heroObjectNode.set("output", heroDetails);

        return heroObjectNode;
    }

    public static ObjectNode getPlayerTurn(ObjectMapper mapper, String command, Object currentPlayer, Object playerOne) {
        ObjectNode playerTurnObjectNode = mapper.createObjectNode();

        playerTurnObjectNode.put("command", command);

        int output;
        if (currentPlayer == playerOne) {
            output = 1;
        } else {
            output = 2;
        }

        playerTurnObjectNode.put("output", output);

        return playerTurnObjectNode;
    }

    public static int endPlayerTurn(Hero currentPlayerHero, GameBoard gameBoard, int playerIndex, int turns, int round) {
        currentPlayerHero.setHasAttacked(false);
        if (playerIndex == 1) {
            gameBoard.resetStatusOfCardsOnRow(2);
            gameBoard.resetStatusOfCardsOnRow(3);
        } else {
            gameBoard.resetStatusOfCardsOnRow(0);
            gameBoard.resetStatusOfCardsOnRow(1);
        }

        turns++;
        if (turns % 2 == 0) {
            round++;
        }

        return round;
    }

    public static void endRound(Player playerOne, Player playerTwo, int round) {
        playerOne.drawCard();
        playerTwo.drawCard();

        int manaToAdd = Math.min(round, 10);
        playerOne.setMana(playerOne.getMana() + manaToAdd);
        playerTwo.setMana(playerTwo.getMana() + manaToAdd);
    }

    public static void placeCard(ObjectMapper mapper, Actions action, GameBoard gameBoard, Player currentPlayer, Player playerOne, Player playerTwo, ArrayNode output) {
        ObjectNode playerPlaceCardObjectNode = mapper.createObjectNode();
        int handIdx = action.getHandIdx();
        boolean errorMana = false;
        boolean errorRow = false;

        if (handIdx < 0 || handIdx >= currentPlayer.getHand().size()) {
            playerPlaceCardObjectNode.put("error", "Invalid card index.");
            output.add(playerPlaceCardObjectNode);
            return;
        }

        Card card = currentPlayer.getHand().get(handIdx);

        if (currentPlayer.getMana() < card.getMana()) {
            errorMana = true;
        }

        int row = 0;

        if (currentPlayer == playerOne) {
            if (card.getName().equals("Goliath") || card.getName().equals("Warden")
                    || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
                row = 2;
                if (gameBoard.isRowFull(row)) {
                    errorRow = true;
                }
            }
            if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
                    || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
                row = 3;
                if (gameBoard.isRowFull(3)) {
                    errorRow = true;
                }
            }
        } else {
            if (card.getName().equals("Goliath") || card.getName().equals("Warden")
                    || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
                row = 1;
                if (gameBoard.isRowFull(row)) {
                    errorRow = true;
                }
            }
            if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
                    || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
                row = 0;
                if (gameBoard.isRowFull(0)) {
                    errorRow = true;
                }
            }
        }

        if (errorMana) {
            playerPlaceCardObjectNode.put("command", action.getCommand());
            playerPlaceCardObjectNode.put("handIdx", action.getHandIdx());
            playerPlaceCardObjectNode.put("error", "Not enough mana to place card on table.");
            output.add(playerPlaceCardObjectNode);
            return;
        } else if (errorRow) {
            playerPlaceCardObjectNode.put("command", action.getCommand());
            playerPlaceCardObjectNode.put("handIdx", action.getHandIdx());
            playerPlaceCardObjectNode.put("error", "Cannot place card on table since row is full.");
            output.add(playerPlaceCardObjectNode);
            return;
        } else {

            gameBoard.placeCard(card, row);

            if (currentPlayer == playerOne) {
                if (card.isTankCard()) {
                    playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() + 1);
                }
                playerOne.setMana(currentPlayer.getMana() - card.getMana());
                playerOne.getHand().remove(handIdx);
            } else {
                if (card.isTankCard()) {
                    playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() + 1);
                }
                playerTwo.setMana(currentPlayer.getMana() - card.getMana());
                playerTwo.getHand().remove(handIdx);
            }
        }
    }

    public static void getCardsInHand(ObjectMapper mapper, Actions action, Player playerOne, Player playerTwo, ArrayNode output) {
        ObjectNode cardsInHandObjectNode = mapper.createObjectNode();
        cardsInHandObjectNode.put("command", action.getCommand());
        cardsInHandObjectNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode cardNode = mapper.createArrayNode();

        ArrayList<Card> hand;
        if (action.getPlayerIdx() == 1) {
            hand = playerOne.getHand();
        } else {
            hand = playerTwo.getHand();
        }

        for (Card card : hand) {
            ObjectNode cardInHand = mapper.createObjectNode();
            cardInHand.put("mana", card.getMana());
            cardInHand.put("attackDamage", card.getAttackDamage());
            cardInHand.put("health", card.getHealth());
            cardInHand.put("description", card.getDescription());

            ArrayNode colors = mapper.createArrayNode();
            for (String color : card.getColors()) {
                colors.add(color);
            }
            cardInHand.set("colors", colors);
            cardInHand.put("name", card.getName());

            cardNode.add(cardInHand);
        }

        cardsInHandObjectNode.set("output", cardNode);
        output.add(cardsInHandObjectNode);
    }

    public static void getPlayerMana(ObjectMapper mapper, Actions action, Player playerOne, Player playerTwo, ArrayNode output) {
        ObjectNode playerManaNode = mapper.createObjectNode();
        playerManaNode.put("command", action.getCommand());
        playerManaNode.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            playerManaNode.put("output", playerOne.getMana());
        } else {
            playerManaNode.put("output", playerTwo.getMana());
        }

        output.add(playerManaNode);
    }

    public static void getCardsOnTable(ObjectMapper mapper, Actions action, GameBoard gameBoard, ArrayNode output) {
        ObjectNode cardsOnTableNode = mapper.createObjectNode();
        cardsOnTableNode.put("command", action.getCommand());

        ArrayNode allCardsOnTable = mapper.createArrayNode();

        for (ArrayList<Card> rowTable : gameBoard.getBoard()) {
            ArrayNode allCardsOnRow = mapper.createArrayNode();

            for (Card cardOnTable : rowTable) {
                if (cardOnTable != null) {
                    ObjectNode cardNode = mapper.createObjectNode();
                    cardNode.put("mana", cardOnTable.getMana());
                    cardNode.put("attackDamage", cardOnTable.getAttackDamage());
                    cardNode.put("health", cardOnTable.getHealth());
                    cardNode.put("description", cardOnTable.getDescription());

                    ArrayNode colors = mapper.createArrayNode();
                    for (String color : cardOnTable.getColors()) {
                        colors.add(color);
                    }
                    cardNode.set("colors", colors);
                    cardNode.put("name", cardOnTable.getName());

                    allCardsOnRow.add(cardNode);
                }
            }

            allCardsOnTable.add(allCardsOnRow);
        }
        cardsOnTableNode.set("output", allCardsOnTable);
        output.add(cardsOnTableNode);
    }

    public static void getCardAtPosition(ObjectMapper mapper, Actions action, GameBoard gameBoard, ArrayNode output) {
        ObjectNode cardAtPositionNode = mapper.createObjectNode();
        cardAtPositionNode.put("command", action.getCommand());
        cardAtPositionNode.put("x", action.getX());
        cardAtPositionNode.put("y", action.getY());

        if (action.getX() < 0 || action.getX() >= gameBoard.getBoard().size()) {
            cardAtPositionNode.put("output", "Invalid row index");
            output.add(cardAtPositionNode);
            return;
        }

        if (action.getY() < 0 || action.getY() >= gameBoard.getBoard().get(action.getX()).size()) {
            cardAtPositionNode.put("output", "No card available at that position.");
            output.add(cardAtPositionNode);
            return;
        }

        Card cardAtPosition = gameBoard.getCard(action.getX(), action.getY());

        if (cardAtPosition == null) {
            cardAtPositionNode.put("output", "No card available at that position.");
        } else {
            ObjectNode cardNodeOnTable = mapper.createObjectNode();
            cardNodeOnTable.put("mana", cardAtPosition.getMana());
            cardNodeOnTable.put("attackDamage", cardAtPosition.getAttackDamage());
            cardNodeOnTable.put("health", cardAtPosition.getHealth());
            cardNodeOnTable.put("description", cardAtPosition.getDescription());

            ArrayNode colors = mapper.createArrayNode();
            for (String color : cardAtPosition.getColors()) {
                colors.add(color);
            }
            cardNodeOnTable.set("colors", colors);
            cardNodeOnTable.put("name", cardAtPosition.getName());

            cardAtPositionNode.set("output", cardNodeOnTable);
        }

        output.add(cardAtPositionNode);
    }

    public static void cardUsesAttack(ObjectMapper mapper, Actions action, GameBoard gameBoard, Player currentPlayer, Player playerOne, Player playerTwo, ArrayNode output) {
        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        int xAttacked = action.getCardAttacked().getX();
        int yAttacked = action.getCardAttacked().getY();

        if (xAttacker < 0 || xAttacker >= gameBoard.getBoard().size() || yAttacker < 0 || yAttacker >= gameBoard.getBoard().get(xAttacker).size()) {
            return;
        }
        if (xAttacked < 0 || xAttacked >= gameBoard.getBoard().size() || yAttacked < 0 || yAttacked >= gameBoard.getBoard().get(xAttacked).size()) {
            return;
        }

        Card attackerCard = gameBoard.getCard(xAttacker, yAttacker);
        Card attackedCard = gameBoard.getCard(xAttacked, yAttacked);

        ObjectNode errorCardUsesAttack = mapper.createObjectNode();
        errorCardUsesAttack.put("command", action.getCommand());

        ObjectNode attackerCoordinates = mapper.createObjectNode();
        attackerCoordinates.put("x", xAttacker);
        attackerCoordinates.put("y", yAttacker);
        errorCardUsesAttack.set("cardAttacker", attackerCoordinates);

        ObjectNode attackedCoordinates = mapper.createObjectNode();
        attackedCoordinates.put("x", xAttacked);
        attackedCoordinates.put("y", yAttacked);
        errorCardUsesAttack.set("cardAttacked", attackedCoordinates);

        boolean errorFrozen = attackerCard.isFrozen();
        boolean errorAlreadyAttacked = attackerCard.getHasAttacked();
        boolean errorNotEnemyCard = false;
        boolean errorTankCard = false;

        if (currentPlayer == playerOne) {
            if (xAttacked == 2 || xAttacked == 3) {
                errorNotEnemyCard = true;
            } else if (playerTwo.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                errorTankCard = true;
            }
        } else {
            if (xAttacked == 0 || xAttacked == 1) {
                errorNotEnemyCard = true;
            } else if (playerOne.getNumberOfTanks() > 0 && !attackedCard.isTankCard()) {
                errorTankCard = true;
            }
        }

        if (errorFrozen) {
            errorCardUsesAttack.put("error", "Attacker card is frozen.");
        } else if (errorNotEnemyCard) {
            errorCardUsesAttack.put("error", "Attacked card does not belong to the enemy.");
        } else if (errorAlreadyAttacked) {
            errorCardUsesAttack.put("error", "Attacker card has already attacked this turn.");
        } else if (errorTankCard) {
            errorCardUsesAttack.put("error", "Attacked card is not of type 'Tank'.");
        }

        if (!errorFrozen && !errorAlreadyAttacked && !errorTankCard && !errorNotEnemyCard) {
            attackerCard.attackCard(attackerCard, attackedCard);

            if (attackedCard.getHealth() <= 0) {
                gameBoard.getBoard().remove(attackedCard);

                if (attackedCard.isTankCard()) {
                    if (currentPlayer == playerTwo) {
                        playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() - 1);
                    } else {
                        playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() - 1);
                    }
                }
            }
        } else {

            output.add(errorCardUsesAttack);
        }
    }

    public static void cardUsesAbility(ObjectMapper mapper, Actions action, GameBoard gameBoard,
                                       Player currentPlayer, Player playerOne, Player playerTwo,
                                       ArrayNode output) {
        int xAttackerA = action.getCardAttacker().getX();
        int yAttackerA = action.getCardAttacker().getY();
        int xAttackedA = action.getCardAttacked().getX();
        int yAttackedA = action.getCardAttacked().getY();

        if (xAttackerA < 0 || xAttackerA >= gameBoard.getBoard().size() || yAttackerA < 0 || yAttackerA >= gameBoard.getBoard().get(xAttackerA).size()) {
            return;
        }
        if (xAttackedA < 0 || xAttackedA >= gameBoard.getBoard().size() || yAttackedA < 0 || yAttackedA >= gameBoard.getBoard().get(xAttackedA).size()) {
            return;
        }

        Card attackerCardA = gameBoard.getCard(xAttackerA, yAttackerA);
        Card attackedCardA = gameBoard.getCard(xAttackedA, yAttackedA);

        ObjectNode errorCardUsesAbility = mapper.createObjectNode();
        errorCardUsesAbility.put("command", action.getCommand());

        ObjectNode attackerCoordinatesA = mapper.createObjectNode();
        attackerCoordinatesA.put("x", xAttackerA);
        attackerCoordinatesA.put("y", yAttackerA);
        errorCardUsesAbility.set("cardAttacker", attackerCoordinatesA);

        ObjectNode attackedCoordinatesA = mapper.createObjectNode();
        attackedCoordinatesA.put("x", xAttackedA);
        attackedCoordinatesA.put("y", yAttackedA);
        errorCardUsesAbility.set("cardAttacked", attackedCoordinatesA);

        boolean errorFrozenA = attackerCardA.isFrozen();
        boolean errorAlreadyAttackedA = attackerCardA.getHasAttacked();
        boolean errorDisciple = false;
        boolean errorNotEnemyCardA = false;
        boolean errorTankCardA = false;

        if (currentPlayer == playerOne) {
            if (attackerCardA.getName().equals("Disciple")) {
                if (xAttackedA == 1 || xAttackedA == 0) {
                    errorDisciple = true;
                }
            } else if (attackerCardA.getName().equals("The Cursed One") || attackerCardA.getName().equals("The Ripper") || attackerCardA.getName().equals("Miraj")) {
                if (xAttackedA == 2 || xAttackedA == 3) {
                    errorNotEnemyCardA = true;
                } else if (playerTwo.getNumberOfTanks() > 0 && !attackedCardA.isTankCard()) {
                    errorTankCardA = true;
                }
            }
        } else {
            if (attackerCardA.getName().equals("Disciple")) {
                if (xAttackedA == 2 || xAttackedA == 3) {
                    errorDisciple = true;
                }
            } else if (attackerCardA.getName().equals("The Cursed One") || attackerCardA.getName().equals("The Ripper") || attackerCardA.getName().equals("Miraj")) {
                if (xAttackedA == 0 || xAttackedA == 1) {
                    errorNotEnemyCardA = true;
                } else if (playerOne.getNumberOfTanks() > 0 && !attackedCardA.isTankCard()) {
                    errorTankCardA = true;
                }
            }
        }

        if (errorFrozenA) {
            errorCardUsesAbility.put("error", "Attacker card is frozen.");
        } else if (errorNotEnemyCardA) {
            errorCardUsesAbility.put("error", "Attacked card does not belong to the enemy.");
        } else if (errorAlreadyAttackedA) {
            errorCardUsesAbility.put("error", "Attacker card has already attacked this turn.");
        } else if (errorTankCardA) {
            errorCardUsesAbility.put("error", "Attacked card is not of type 'Tank'.");
        } else if (errorDisciple) {
            errorCardUsesAbility.put("error", "Attacked card does not belong to the current player.");
        }

        if (!errorFrozenA && !errorAlreadyAttackedA && !errorTankCardA && !errorNotEnemyCardA && !errorDisciple) {
            if (attackerCardA.getName().equals("Disciple")) {
                attackerCardA.useDiscipleAbility(attackedCardA);
            } else if (attackerCardA.getName().equals("The Cursed One")) {
                attackerCardA.useTheCursedOneAbility(attackedCardA);
            } else if (attackerCardA.getName().equals("The Ripper")) {
                attackerCardA.useTheRipperAbility(attackedCardA);
            } else if (attackerCardA.getName().equals("Miraj")) {
                attackerCardA.useMirajAbility(attackedCardA);
            }

            if (attackedCardA.getHealth() <= 0) {
                if (attackedCardA.isTankCard()) {
                    if (currentPlayer == playerTwo) {
                        playerOne.setNumberOfTanks(playerOne.getNumberOfTanks() - 1);
                    } else {
                        playerTwo.setNumberOfTanks(playerTwo.getNumberOfTanks() - 1);
                    }
                }
                gameBoard.getBoard().remove(attackedCardA);
            }

            attackerCardA.setHasAttacked(true);
        } else {
            output.add(errorCardUsesAbility);
        }
    }

}


