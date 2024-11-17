package main;

import cards.Hero;
import checker.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.GameInput;
import fileio.Input;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import actions.Actions;
import game.Game;
import game.GameCommands;
import game.Player;
import game.GameBoard;
import game.Decks;
import cards.Card;
import java.util.Collections;
import java.util.Random;

import static game.CommandHelper.createHero;

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
    public static void action(final String filePath1, final String filePath2) throws IOException {
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
        GameCommands.resetWins();

        for (GameInput newGame : inputData.getGames()) {
            games.add(new Game(newGame));
        }

        for (Game game : games) {
            int index1 = game.getStartGame().getPlayerOneDeckIdx();
            int index2 = game.getStartGame().getPlayerTwoDeckIdx();

            Hero heroOne = createHero(game.getStartGame().getPlayerOneHero());
            Hero heroTwo = createHero(game.getStartGame().getPlayerTwoHero());

            ArrayList<Card> playerOneDeck = resetDeck(playerOneDecks.deckIndex(index1));
            ArrayList<Card> playerTwoDeck = resetDeck(playerTwoDecks.deckIndex(index2));

            playerOneDeck = shuffleDeck(playerOneDeck, game.getStartGame().getShuffleSeed());
            playerTwoDeck = shuffleDeck(playerTwoDeck, game.getStartGame().getShuffleSeed());

            GameBoard gameBoard = new GameBoard();
            gameBoard.resetBoard();

            Player playerOne = new Player(new ArrayList<>(playerOneDeck), heroOne);
            Player playerTwo = new Player(new ArrayList<>(playerTwoDeck), heroTwo);
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
                        int deckIndex = action.getPlayerIdx();
                        ArrayList<Card> deck;
                        if (deckIndex == 1) {
                            deck = playerOne.getDeck();
                        } else {
                            deck = playerTwo.getDeck();
                        }
                        GameCommands.getPlayerDeck(mapper,
                                action.getCommand(), deckIndex, deck, output);
                        break;

                    case "getPlayerHero":
                        int heroIndex = action.getPlayerIdx();
                        Hero hero;
                        if (heroIndex == 1) {
                            hero = playerOne.getHero();
                        } else {
                            hero = playerTwo.getHero();
                        }
                        GameCommands.getPlayerHero(mapper,
                                action.getCommand(), heroIndex, hero, output);
                        break;

                    case "getPlayerTurn":
                        GameCommands.getPlayerTurn(mapper,
                                action.getCommand(), currentPlayer, playerOne, output);
                        break;

                    case "endPlayerTurn":
                        int playerIndex;
                        if (currentPlayer == playerOne) {
                            playerIndex = 1;
                        } else {
                            playerIndex = 2;
                        }
                        round = GameCommands.endPlayerTurn(currentPlayer.getHero(),
                                gameBoard, playerIndex, turns, round);
                        turns++;
                        if (currentPlayer == playerOne) {
                            currentPlayer = playerTwo;
                        } else {
                            currentPlayer = playerOne;
                        }
                        if (turns % 2 == 0) {
                            GameCommands.endRound(playerOne, playerTwo, round);
                        }
                        break;

                    case "placeCard":
                        GameCommands.placeCard(mapper, action, gameBoard,
                                currentPlayer, playerOne, playerTwo, output);
                        break;

                    case "getCardsInHand":
                        GameCommands.getCardsInHand(mapper, action, playerOne, playerTwo, output);
                        break;

                    case "getPlayerMana":
                        GameCommands.getPlayerMana(mapper, action, playerOne, playerTwo, output);
                        break;

                    case "getCardsOnTable":
                        GameCommands.getCardsOnTable(mapper, action, gameBoard, output);
                        break;

                    case "getCardAtPosition":
                        GameCommands.getCardAtPosition(mapper, action, gameBoard, output);
                        break;

                    case "cardUsesAttack":
                        GameCommands.cardUsesAttack(mapper, action, gameBoard,
                                currentPlayer, playerOne, playerTwo, output);
                        break;

                    case "cardUsesAbility":
                        GameCommands.cardUsesAbility(mapper, action, gameBoard,
                                currentPlayer, playerOne, playerTwo, output);
                        break;

                    case "useAttackHero":
                        if (!gameBoard.isGameEnded()) {
                            GameCommands.useAttackHero(mapper, action, gameBoard,
                                    currentPlayer, playerOne, playerTwo, output);
                        }
                        break;

                    case "useHeroAbility":
                        if (!gameBoard.isGameEnded()) {
                            GameCommands.useHeroAbility(mapper, action, gameBoard,
                                    currentPlayer, playerOne, playerTwo, output);
                        }
                        break;

                    case "getFrozenCardsOnTable":
                        GameCommands.getFrozenCardsOnTable(mapper, action, gameBoard, output);
                        break;

                    case "getTotalGamesPlayed":
                        GameCommands.addTotalGamesPlayed(mapper, action.getCommand(), output);
                        break;

                    case "getPlayerOneWins":
                        GameCommands.addPlayerOneWins(mapper, action.getCommand(), output);
                        break;

                    case "getPlayerTwoWins":
                        GameCommands.addPlayerTwoWins(mapper, action.getCommand(), output);
                        break;

                    default:
                        break;
                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    private static ArrayList<Card> shuffleDeck(final ArrayList<Card> deck, final int seed) {
        Random random = new Random(seed);
        Collections.shuffle(deck, random);
        return deck;
    }

    private static ArrayList<Card> resetDeck(final ArrayList<Card> originalDeck) {
        ArrayList<Card> resetDeck = new ArrayList<>();
        for (Card card : originalDeck) {
            resetDeck.add(new Card(card));
        }
        return resetDeck;
    }

}
