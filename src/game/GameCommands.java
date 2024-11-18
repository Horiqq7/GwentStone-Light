package game;

import actions.Actions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cards.Card;
import cards.Hero;
import java.util.ArrayList;

import static game.CommandHelper.*;
/**
 * Clasa GameCommands contine metode care gestioneaza comenzile din joc.
 */
public abstract class GameCommands {
    static final int FIRST_ROW = 0;
    static final int SECOND_ROW = 1;
    static final int THIRD_ROW = 2;
    static final int FOURTH_ROW = 3;
    private static final int MAX_MANA_PER_ROUND = 10;
    private static int playerOneWins = 0;
    private static int playerTwoWins = 0;

    /**
     * Returneaza pachetul unui jucator.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param command   Numele comenzii executate.
     * @param playerIdx Indexul jucatorului.
     * @param deck      Pachetul jucatorului.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getPlayerDeck(final ObjectMapper mapper, final String command,
                                     final int playerIdx, final ArrayList<Card> deck,
                                     final ArrayNode output) {
        ObjectNode playerDeckObjectNode = mapper.createObjectNode();
        playerDeckObjectNode.put("command", command);
        playerDeckObjectNode.put("playerIdx", playerIdx);

        ArrayNode deckArrayNode = CommandHelper.createDeckArrayNode(mapper, deck);
        playerDeckObjectNode.set("output", deckArrayNode);

        output.add(playerDeckObjectNode);
    }

    /**
     * Returneaza informatiile despre eroul unui jucator.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param command   Numele comenzii executate.
     * @param playerIdx Indexul jucatorului.
     * @param hero      Eroul jucatorului.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getPlayerHero(final ObjectMapper mapper, final String command,
                                     final int playerIdx, final Hero hero,
                                     final ArrayNode output) {
        ObjectNode heroObjectNode = mapper.createObjectNode();
        heroObjectNode.put("command", command);
        heroObjectNode.put("playerIdx", playerIdx);

        ObjectNode heroDetails = mapper.createObjectNode();
        heroDetails.put("mana", hero.getMana());
        heroDetails.put("description", hero.getDescription());

        ArrayNode heroColors = CommandHelper.createHeroColorsArrayNode(mapper, hero);
        heroDetails.set("colors", heroColors);

        heroDetails.put("name", hero.getName());
        heroDetails.put("health", hero.getHealth());

        heroObjectNode.set("output", heroDetails);
        output.add(heroObjectNode);
    }

    /**
     * Determina al cui jucator ii este randul.
     *
     * @param mapper        Obiectul ObjectMapper pentru gestionarea JSON.
     * @param command       Numele comenzii executate.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne     Referinta la primul jucator.
     * @param output        Lista in care se adauga raspunsul.
     */
    public static void getPlayerTurn(final ObjectMapper mapper, final String command,
                                     final Object currentPlayer, final Object playerOne,
                                     final ArrayNode output) {
        ObjectNode playerTurnObjectNode = mapper.createObjectNode();
        playerTurnObjectNode.put("command", command);
        int turn;
        if (currentPlayer == playerOne) {
            turn = 1;
        } else {
            turn = 2;
        }
        playerTurnObjectNode.put("output", turn);
        output.add(playerTurnObjectNode);
    }

    /**
     * Incheie tura unui jucator si returneaza numarul rundei curente.
     *
     * @param currentPlayerHero Eroul jucatorului curent.
     * @param gameBoard         Tabla de joc.
     * @param playerIndex       Indexul jucatorului curent.
     * @param turns             Numarul total de ture.
     * @param round             Numarul rundei curente.
     * @return                  Numarul rundei curente.
     */
    public static int endPlayerTurn(final Hero currentPlayerHero, final GameBoard gameBoard,
                                    final int playerIndex, int turns, int round) {
        currentPlayerHero.setHasAttacked(false);

        if (playerIndex == 1) {
            gameBoard.resetStatusOfCardsOnRow(THIRD_ROW);
            gameBoard.resetStatusOfCardsOnRow(FOURTH_ROW);
        } else {
            gameBoard.resetStatusOfCardsOnRow(FIRST_ROW);
            gameBoard.resetStatusOfCardsOnRow(SECOND_ROW);
        }
        turns++;
        if (turns % 2 == 0) {
            round++;
        }
        return round;
    }

    /**
     * Incheie runda si adauga mana jucatorilor.
     *
     * @param playerOne Primul jucator.
     * @param playerTwo Al doilea jucator.
     * @param round     Numarul curent al rundei.
     */
    public static void endRound(final Player playerOne, final Player playerTwo, final int round) {
        playerOne.drawCard();
        playerTwo.drawCard();
        int manaToAdd;
        if (round < MAX_MANA_PER_ROUND) {
            manaToAdd = round;
        } else {
            manaToAdd = MAX_MANA_PER_ROUND;
        }
        playerOne.setMana(playerOne.getMana() + manaToAdd);
        playerTwo.setMana(playerTwo.getMana() + manaToAdd);
    }

    /**
     * Plaseaza o carte pe tabla de joc, verificand mana si disponibilitatea randului.
     *
     * @param mapper        Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action        Actiunea curenta.
     * @param gameBoard     Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne     Primul jucator.
     * @param playerTwo     Al doilea jucator.
     * @param output        Lista in care se adauga raspunsul.
     */
    public static void placeCard(final ObjectMapper mapper, final Actions action,
                                 final GameBoard gameBoard, final Player currentPlayer,
                                 final Player playerOne, final Player playerTwo,
                                 final ArrayNode output) {
        ObjectNode playerPlaceCardObjectNode = mapper.createObjectNode();
        int handIdx = action.getHandIdx();
        boolean errorMana = false;
        boolean errorRow = false;

        Card card = currentPlayer.getHand().get(handIdx);
        if (currentPlayer.getMana() < card.getMana()) {
            errorMana = true;
        }

        int row = CommandHelper.determineRowForPlayer(card, currentPlayer == playerOne);
        if (row != -1) {
            if (gameBoard.isRowFull(row)) {
                errorRow = true;
            }
        }

        if (errorMana) {
            playerPlaceCardObjectNode.put("command", action.getCommand());
            playerPlaceCardObjectNode.put("handIdx", action.getHandIdx());
            playerPlaceCardObjectNode.put("error", "Not enough mana to place card on table.");
            output.add(playerPlaceCardObjectNode);
        } else if (errorRow) {
            playerPlaceCardObjectNode.put("command", action.getCommand());
            playerPlaceCardObjectNode.put("handIdx", action.getHandIdx());
            playerPlaceCardObjectNode.put("error", "Cannot place card on table since row is full.");
            output.add(playerPlaceCardObjectNode);
        } else {
            playCard(gameBoard, currentPlayer, playerOne, playerTwo, card, handIdx);
        }
    }

    /**
     * Returneaza cartile din mana unui jucator.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action    Actiunea curenta.
     * @param playerOne Primul jucator.
     * @param playerTwo Al doilea jucator.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getCardsInHand(final ObjectMapper mapper, final Actions action,
                                      final Player playerOne, final Player playerTwo,
                                      final ArrayNode output) {
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
            cardNode.add(buildCardNode(mapper, card));
        }

        cardsInHandObjectNode.set("output", cardNode);
        output.add(cardsInHandObjectNode);
    }

    /**
     * Returneaza mana unui jucator.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action    Actiunea curenta.
     * @param playerOne Primul jucator.
     * @param playerTwo Al doilea jucator.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getPlayerMana(final ObjectMapper mapper, final Actions action,
                                     final Player playerOne, final Player playerTwo,
                                     final ArrayNode output) {
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

    /**
     * Returneaza toate cartile aflate pe tabla de joc.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action    Actiunea curenta.
     * @param gameBoard Tabla de joc.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getCardsOnTable(final ObjectMapper mapper, final Actions action,
                                       final GameBoard gameBoard, final ArrayNode output) {
        ObjectNode cardsOnTableNode = mapper.createObjectNode();
        cardsOnTableNode.put("command", action.getCommand());

        ArrayNode allCardsOnTable = mapper.createArrayNode();

        for (ArrayList<Card> rowTable : gameBoard.getBoard()) {
            ArrayNode allCardsOnRow = mapper.createArrayNode();
            for (Card cardOnTable : rowTable) {
                if (cardOnTable != null) {
                    allCardsOnRow.add(buildCardNode(mapper, cardOnTable));
                }
            }
            allCardsOnTable.add(allCardsOnRow);
        }
        cardsOnTableNode.set("output", allCardsOnTable);
        output.add(cardsOnTableNode);
    }

    /**
     * Returneaza cartea aflata intr-o pozitie specifica pe tabla de joc.
     *
     * @param mapper    Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action    Actiunea curenta.
     * @param gameBoard Tabla de joc.
     * @param output    Lista in care se adauga raspunsul.
     */
    public static void getCardAtPosition(final ObjectMapper mapper, final Actions action,
                                         final GameBoard gameBoard, final ArrayNode output) {
        ObjectNode cardAtPositionNode = mapper.createObjectNode();
        cardAtPositionNode.put("command", action.getCommand());
        cardAtPositionNode.put("x", action.getX());
        cardAtPositionNode.put("y", action.getY());

        if (action.getY() < 0 || action.getY() >= gameBoard.getBoard().get(action.getX()).size()) {
            cardAtPositionNode.put("output", "No card available at that position.");
            output.add(cardAtPositionNode);
            return;
        }
        Card cardAtPosition = gameBoard.getCard(action.getX(), action.getY());
        if (cardAtPosition == null) {
            cardAtPositionNode.put("output", "No card available at that position.");
        } else {
            ObjectNode cardNodeOnTable = buildCardNode(mapper, cardAtPosition);
            cardAtPositionNode.set("output", cardNodeOnTable);
        }
        output.add(cardAtPositionNode);
    }

    /**
     * Permite unei carti sa atace o alta carte.
     *
     * @param mapper        Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action        Actiunea curenta.
     * @param gameBoard     Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne     Primul jucator.
     * @param playerTwo     Al doilea jucator.
     * @param output        Lista in care se adauga eroarea daca exista.
     */
    public static void cardUsesAttack(final ObjectMapper mapper, final Actions action,
                                      final GameBoard gameBoard, final Player currentPlayer,
                                      final Player playerOne, final Player playerTwo,
                                      final ArrayNode output) {
        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        int xAttacked = action.getCardAttacked().getX();
        int yAttacked = action.getCardAttacked().getY();

        Card attackerCard = gameBoard.getCard(xAttacker, yAttacker);
        Card attackedCard = gameBoard.getCard(xAttacked, yAttacked);

        ObjectNode errorCardUsesAttack = prepareErrorResponse(mapper,
                action, xAttacker, yAttacker, xAttacked, yAttacked);
        if (hasCardErrors(currentPlayer, playerOne, playerTwo,
                attackerCard, attackedCard, xAttacked, errorCardUsesAttack)) {
            output.add(errorCardUsesAttack);
            return;
        }
        performAttack(attackerCard, attackedCard, gameBoard, xAttacked, yAttacked,
                currentPlayer, playerOne, playerTwo);
    }

    /**
     * Permite unei carti sa foloseasca o abilitate asupra altei carti.
     *
     * @param mapper        Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action        Actiunea curenta.
     * @param gameBoard     Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne     Primul jucator.
     * @param playerTwo     Al doilea jucator.
     * @param output        Lista in care se adauga eroarea daca exista.
     */
    public static void cardUsesAbility(final ObjectMapper mapper, final Actions action,
                                       final GameBoard gameBoard, final Player currentPlayer,
                                       final Player playerOne, final Player playerTwo,
                                       final ArrayNode output) {
        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        int xAttacked = action.getCardAttacked().getX();
        int yAttacked = action.getCardAttacked().getY();

        Card attackerCard = gameBoard.getCard(xAttacker, yAttacker);
        Card attackedCard = gameBoard.getCard(xAttacked, yAttacked);

        ObjectNode errorResponse = prepareErrorResponse(mapper, action,
                xAttacker, yAttacker, xAttacked, yAttacked);

        if (hasAbilityErrors(currentPlayer, playerOne, playerTwo,
                attackerCard, attackedCard, xAttacked, errorResponse)) {
            output.add(errorResponse);
            return;
        }

        performAbility(attackerCard, attackedCard, gameBoard, currentPlayer, playerOne, playerTwo);
    }

    /**
     * Permite unui jucator sa atace eroul advers.
     *
     * @param mapper        Obiectul ObjectMapper pentru gestionarea JSON.
     * @param action        Actiunea curenta.
     * @param gameBoard     Tabla de joc.
     * @param currentPlayer Jucatorul curent.
     * @param playerOne     Primul jucator.
     * @param playerTwo     Al doilea jucator.
     * @param output        Lista in care se adauga raspunsul.
     */
    public static void useAttackHero(final ObjectMapper mapper, final Actions action,
                                     final GameBoard gameBoard, final Player currentPlayer,
                                     final Player playerOne, final Player playerTwo,
                                     final ArrayNode output) {
        if (gameBoard.isGameEnded()) {
            return;
        }

        int xAttacker = action.getCardAttacker().getX();
        int yAttacker = action.getCardAttacker().getY();
        Card attackerCard = gameBoard.getCard(xAttacker, yAttacker);

        ObjectNode errorUseAttackHero = buildErrorResponse(mapper, action, xAttacker, yAttacker);

        if (isCardFrozenOrAttacked(attackerCard, errorUseAttackHero)) {
            output.add(errorUseAttackHero);
            return;
        }

        Hero enemyHero = getEnemyHero(currentPlayer, playerOne, playerTwo);
        boolean enemyHasTank = checkForEnemyTanks(gameBoard, currentPlayer, playerOne);

        if (isInvalidTankAttack(enemyHasTank, errorUseAttackHero)) {
            output.add(errorUseAttackHero);
            return;
        }

        performAttackHero(attackerCard, enemyHero, gameBoard,
                currentPlayer, playerOne, output, mapper);
    }

    /**
     * Utilizeaza abilitatea eroului pentru a afecta o linie specificata pe tabla de joc.
     *
     * @param mapper      Mapperul utilizat pentru crearea obiectelor JSON.
     * @param action      Actiunea curenta care contine informatiile necesare.
     * @param gameBoard   Tabla de joc curenta.
     * @param currentPlayer Jucatorul care executa abilitatea.
     * @param playerOne   Primul jucator.
     * @param playerTwo   Al doilea jucator.
     * @param output      Lista de raspunsuri care va contine rezultatul actiunii.
     */
    public static void useHeroAbility(final ObjectMapper mapper, final Actions action,
                                      final GameBoard gameBoard, final Player currentPlayer,
                                      final Player playerOne, final Player playerTwo,
                                      final ArrayNode output) {

        int affectedRow = action.getAffectedRow();
        ObjectNode errorUseHeroAbility = mapper.createObjectNode();
        errorUseHeroAbility.put("command", action.getCommand());
        errorUseHeroAbility.put("affectedRow", affectedRow);

        Hero hero = currentPlayer.getHero();
        if (CommandHelper.checkHeroMana(currentPlayer, hero, errorUseHeroAbility, output)) {
            return;
        }
        if (CommandHelper.checkHeroHasAttacked(hero, errorUseHeroAbility, output)) {
            return;
        }
        if (CommandHelper.checkValidRow(hero, affectedRow, currentPlayer, playerOne, playerTwo,
                errorUseHeroAbility, output)) {
            return;
        }

        hero.useAbility(gameBoard, affectedRow);

        currentPlayer.setMana(currentPlayer.getMana() - hero.getMana());
        hero.setHasAttacked(true);
    }

    /**
     * Obtine toate cartile inghetate de pe tabla de joc.
     *
     * @param mapper    Mapperul utilizat pentru crearea obiectelor JSON.
     * @param action    Actiunea curenta care contine informatiile necesare.
     * @param gameBoard Tabla de joc curenta.
     * @param output    Lista de raspunsuri care va contine cartile inghetate.
     */
    public static void getFrozenCardsOnTable(final ObjectMapper mapper, final Actions action,
                                             final GameBoard gameBoard, final ArrayNode output) {
        ArrayNode frozenCardsArrayNode = mapper.createArrayNode();

        for (ArrayList<Card> row : gameBoard.getBoard()) {
            for (Card card : row) {
                if (card.isFrozen()) {
                    frozenCardsArrayNode.add(CommandHelper.buildCardNode(mapper, card));
                }
            }
        }

        ObjectNode frozenCardsObjectNode = mapper.createObjectNode();
        frozenCardsObjectNode.put("command", action.getCommand());
        frozenCardsObjectNode.set("output", frozenCardsArrayNode);
        output.add(frozenCardsObjectNode);
    }

    /**
     * Incrementeaza numarul de victorii pentru primul jucator.
     */
    public static void incrementPlayerOneWins() {
        playerOneWins++;
    }

    /**
     * Incrementeaza numarul de victorii pentru al doilea jucător.
     */
    public static void incrementPlayerTwoWins() {
        playerTwoWins++;
    }

    /**
     * Adauga numarul total de jocuri jucate in lista de raspunsuri.
     *
     * @param mapper   Mapperul utilizat pentru crearea obiectelor JSON.
     * @param command  Comanda curenta executata.
     * @param output   Lista de raspunsuri care va contine rezultatul.
     */
    public static void addTotalGamesPlayed(final ObjectMapper mapper,
                                           final String command, final ArrayNode output) {
        ObjectNode response = mapper.createObjectNode();
        response.put("command", command);
        response.put("output", playerOneWins + playerTwoWins);
        output.add(response);
    }

    /**
     * Adauga numarul de victorii ale primului jucator in lista de raspunsuri.
     *
     * @param mapper   Mapperul utilizat pentru crearea obiectelor JSON.
     * @param command  Comanda curenta executata.
     * @param output   Lista de raspunsuri care va contine rezultatul.
     */
    public static void addPlayerOneWins(final ObjectMapper mapper,
                                        final String command, final ArrayNode output) {
        ObjectNode response = mapper.createObjectNode();
        response.put("command", command);
        response.put("output", playerOneWins);
        output.add(response);
    }

    /**
     * Adauga numarul de victorii ale celui de-al doilea jucator in lista de raspunsuri.
     *
     * @param mapper   Mapperul utilizat pentru crearea obiectelor JSON.
     * @param command  Comanda curenta executata.
     * @param output   Lista de raspunsuri care va contine rezultatul.
     */
    public static void addPlayerTwoWins(final ObjectMapper mapper,
                                        final String command, final ArrayNode output) {
        ObjectNode response = mapper.createObjectNode();
        response.put("command", command);
        response.put("output", playerTwoWins);
        output.add(response);
    }

    /**
     * Reseteaza numarul de victorii pentru ambii jucatori.
     */
    public static void resetWins() {
        playerOneWins = 0;
        playerTwoWins = 0;
    }
}
