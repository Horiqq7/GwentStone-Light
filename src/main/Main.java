package main;

import cards.Hero;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;
import fileio.GameInput;
import fileio.Input;
import game.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import cards.Card;

import java.util.Collections;
import java.util.Random;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         * ObjectMapper mapper = new ObjectMapper();
         *
         * ObjectNode objectNode = mapper.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = mapper.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        ObjectMapper mapper = new ObjectMapper();
        Decks playerOneDecks = new Decks(inputData.getPlayerOneDecks());
        Decks playerTwoDecks = new Decks(inputData.getPlayerTwoDecks());
        ArrayList<Game> games = new ArrayList<>();
        for (GameInput newGame : inputData.getGames()) {
            games.add(new Game(newGame));
        }
        for (Game game : games) {
            int index1 = game.getStartGame().getPlayerOneDeckIdx();
            int index2 = game.getStartGame().getPlayerTwoDeckIdx();

            Hero heroOne = new Hero(game.getStartGame().getPlayerOneHero());
            Hero heroTwo = new Hero(game.getStartGame().getPlayerTwoHero());

            ArrayList<Card> playerOneDeck = playerOneDecks.deckIndex(index1);
            ArrayList<Card> playerTwoDeck = playerTwoDecks.deckIndex(index2);

            GameBoard gameBoard = new GameBoard();

            Random random1 = new Random(game.getStartGame().getShuffleSeed());
            Collections.shuffle(playerOneDeck, random1);
            Random random2 = new Random(game.getStartGame().getShuffleSeed());
            Collections.shuffle(playerTwoDeck, random2);

            Player playerOne = new Player(playerOneDeck, heroOne);
            Player playerTwo = new Player(playerTwoDeck, heroTwo);
            Player currentPlayer;
            if (game.getStartGame().getStartingPlayer() == 1) {
                currentPlayer = playerOne;
            } else {
                currentPlayer = playerTwo;
            }


            playerOne.drawCard();
            playerTwo.drawCard();

            int turns = 0;
            int round = 1;

            for (Actions action : game.getActions()) {
                switch (action.getCommand()) {
                    case "getPlayerDeck":
                        int index = action.getPlayerIdx();
                        ArrayList<Card> deck = (index == 1) ? playerOne.getDeck() : playerTwo.getDeck();
                        ObjectNode playerDeckObjectNode = GameHelper.getPlayerDeck(mapper, action.getCommand(), index, deck);
                        output.add(playerDeckObjectNode);
                        break;
                    case "getPlayerHero":
                        int heroIndex = action.getPlayerIdx();
                        Hero hero = (heroIndex == 1) ? playerOne.getHero() : playerTwo.getHero();
                        ObjectNode heroObjectNode = GameHelper.getPlayerHero(mapper, action.getCommand(), heroIndex, hero);
                        output.add(heroObjectNode);
                        break;
                    case "getPlayerTurn":
                        ObjectNode playerTurnObjectNode = GameHelper.getPlayerTurn(mapper, action.getCommand(), currentPlayer, playerOne);
                        output.add(playerTurnObjectNode);
                        break;
                    case "endPlayerTurn":
                        round = GameHelper.endPlayerTurn(currentPlayer.getHero(), gameBoard,
                                (currentPlayer == playerOne) ? 1 : 2, turns, round);
                        turns++;

                        if (currentPlayer == playerOne) {
                            currentPlayer = playerTwo;
                        } else {
                            currentPlayer = playerOne;
                        }

                        if (turns % 2 == 0) {
                            GameHelper.endRound(playerOne, playerTwo, round);
                        }
                        break;
                    case "placeCard":
                        GameHelper.placeCard(mapper, action, gameBoard, currentPlayer, playerOne, playerTwo, output);
                        break;
                    case "getCardsInHand":
                        GameHelper.getCardsInHand(mapper, action, playerOne, playerTwo, output);
                        break;
                    case "getPlayerMana":
                        GameHelper.getPlayerMana(mapper, action, playerOne, playerTwo, output);
                        break;
                    case "getCardsOnTable":
                        GameHelper.getCardsOnTable(mapper, action, gameBoard, output);
                        break;
                    case "getCardAtPosition":
                        GameHelper.getCardAtPosition(mapper, action, gameBoard, output);
                        break;
                    case "cardUsesAttack":
                        GameHelper.cardUsesAttack(mapper, action, gameBoard, currentPlayer, playerOne, playerTwo, output);
                        break;
                    case "cardUsesAbility":
                        GameHelper.cardUsesAbility(mapper, action, gameBoard, currentPlayer, playerOne, playerTwo, output);
                        break;
                }
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}

