package cz.muni.fi.pb162.hw01.impl;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.muni.fi.pb162.hw01.Utils;
import cz.muni.fi.pb162.hw01.cmd.Messages;

/**
 * Class representing the most important object of the game. Manager
 * manages the whole game, stores configuration, current board and takes
 * care of history.
 * @author Michaela Lodnanova
 */
public class Manager implements GameManager {

    private Configuration configuration;
    private TicTacToeBoard currentBoard;
    private Deque<Board> history;

    /**
     * Creates Manager object that manages the whole game.
     * Sets current board, creates history as a linked list.
     * @param configuration is an input parameter for creating
     *                      current board.
     */
    public Manager(Configuration configuration){

        this.configuration = configuration;
        currentBoard = new TicTacToeBoard(configuration);
        history = new LinkedList<>();

    }

    /**
     * The most complex method that looks a bit like spaghetti, but is the
     * most important in the program. Actually playGame method plays the game.
     * Prints everything important to the output and decides which command is
     * supposed to be provided now.
     */
    public void playGame(){

        int playerIndex = 0;
        int gameTurn = 1;

        boolean isFinished = false;

        while (!isFinished){

            //starting over and over till isFinished
            if (playerIndex > configuration.getPlayers().length-1) playerIndex = 0;
            char currentPlayer = configuration.getPlayers()[playerIndex];

            System.out.printf(Messages.TURN_COUNTER, gameTurn);
            System.out.print(currentBoard.format());
            System.out.printf(Messages.TURN_PROMPT, currentPlayer);

            ParsedCommand parsedCommand = ParsedCommand.parseCommand(Utils.readLineFromStdIn());

            System.out.print(Messages.TURN_DELIMITER);

            switch (parsedCommand.command) {

                case TURN:
                    if (isCommandValid(parsedCommand)) {
                        storeBoard((TicTacToeBoard) currentBoard.copy());
                        currentBoard.put(parsedCommand.arg1, parsedCommand.arg2, currentPlayer);

                    }
                    else System.out.print(Messages.ERROR_ILLEGAL_PLAY + System.lineSeparator());
                    break;

                case REWIND:
                    if (isCommandValid(parsedCommand) && parsedCommand.arg1 < configuration.getHistorySize()) {
                        for (int num = 1; num < parsedCommand.arg1; num++){
                            history.removeLast();
                        }

                        currentBoard = (TicTacToeBoard) history.getLast();
                        history.removeLast();
                    }
                    else System.out.print(Messages.ERROR_REWIND + System.lineSeparator());
                    break;

                case QUIT:
                    System.out.printf(Messages.GAME_OVER, gameTurn - 1);
                    System.out.print(currentBoard.format());
                    isFinished = true;

                case INVALID:
                default:
                    System.out.print(Messages.ERROR_INVALID_COMMAND);
            }

            System.out.print(System.lineSeparator());

            if (currentBoard.hasWinner() != null) {

                System.out.printf(Messages.GAME_OVER, gameTurn);
                System.out.print(currentBoard.format());
                System.out.printf(Messages.GAME_WINNER, currentPlayer);
                isFinished = true;
            }


            playerIndex++;
            gameTurn++;
        }
    }

    /**
     * storeBoard takes care of managing the history. Adds the current board
     * to the history, controls historySize and additionally removes older boards.
     * @param currentBoard represents the current board to be added to the history
     */
    private void storeBoard(TicTacToeBoard currentBoard) {
        if (history.size() == configuration.getHistorySize()){
            history.removeFirst();
        }

        history.add(currentBoard);
    }

    /**
     * This method is the second most important one, parses commands.
     * Uses its own private class ParsedCommand which is basically for parsing
     * commands :q, <<x and turns.
     * @param parsedCommand is an object which stores arguments from input
     * @return when input commandis valid, else false
     */
    private boolean isCommandValid(ParsedCommand parsedCommand) {

        if (parsedCommand.command == ParsedCommand.Command.TURN) {
            int row = parsedCommand.arg1;
            int col = parsedCommand.arg2;

            //Both numbers must be in <0, n) where n is the size of the board
            if ((row < 0) || (row > configuration.getBoardSize()-1)) return false;
            if ((col < 0) || (col > configuration.getBoardSize()-1)) return false;

            //Coordinates are referencing an empty cell
            return currentBoard.getCell(row, col) == ' ';
        }

        if (parsedCommand.command == ParsedCommand.Command.REWIND) {
            int rewindTurns = parsedCommand.arg1;

            //N must be in <0, h) where h is the size of history
            return (rewindTurns >= 0) && (rewindTurns <= configuration.getHistorySize());
        }


        return false;
    }

    /**
     * Private static class for parsing commands.
     */
    private static class ParsedCommand {

        enum Command {
            TURN,
            REWIND,
            QUIT,
            INVALID
        }

        Integer arg1;
        Integer arg2;
        Command command;

        /**
         * Constructor creates an object ParsedCommand which is represented
         * by three parameters:
         * @param command - enum type Turn, rewind, quit or invalid
         * @param arg1 of type integer is first argument of a command
         * @param arg2 of type integer is second argument of a command
         */
        private ParsedCommand(Command command, Integer arg1, Integer arg2) {
            this.command = command;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        /**
         * Method for parsing a command
         * @param inputLine commands from input
         * @return parsed command
         */
        public static ParsedCommand parseCommand(String inputLine) {

            //check if it is TURN command
            Pattern turnCommandPattern = Pattern.compile("^(\\s+)?\\d+\\s\\d+(\\s+)?$");
            Matcher matcher = turnCommandPattern.matcher(inputLine);

            if (matcher.matches()) {
                //it is a TRUN command
                List<String> arguments = Utils.findPositiveIntegers(inputLine);

                if (arguments.size() != 2) {
                    return new ParsedCommand(Command.INVALID, null, null);
                }
                else {
                    return new ParsedCommand(Command.TURN,
                            Integer.valueOf(arguments.get(0)), Integer.valueOf(arguments.get(1)));
                }

            }

            //check if it is REWIND command
            Pattern rewindCommandPattern = Pattern.compile("^(\\s+)?<<\\d+(\\s+)?$");
            matcher = rewindCommandPattern.matcher(inputLine);

            if (matcher.matches()) {
                //it is a REWIND command
                List<String> arguments = Utils.findPositiveIntegers(inputLine);

                if (arguments.size() != 1) {
                    return new ParsedCommand(Command.INVALID, null, null);
                }
                else {
                    return new ParsedCommand(Command.REWIND,
                            Integer.valueOf(arguments.get(0)), null);

                }

            }

            //check if it is QUIT command
            Pattern quitCommandPattern = Pattern.compile("^(\\s+)?:q(\\s+)?$");
            matcher = quitCommandPattern.matcher(inputLine);

            if (matcher.matches()) {
                //it is a quit command
                return new ParsedCommand(Command.QUIT, null, null);
            }

            return new ParsedCommand(Command.INVALID, null, null);
        }

    }
}
