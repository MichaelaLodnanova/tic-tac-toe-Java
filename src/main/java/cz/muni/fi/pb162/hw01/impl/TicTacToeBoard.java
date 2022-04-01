package cz.muni.fi.pb162.hw01.impl;

import java.util.Arrays;

/**
 * Class representing an object - 2D play board which represents the main
 * play ground
 * @author Michaela Lodnanova
 */
public class TicTacToeBoard implements Board {

    Configuration configuration;

    private char[][] board;

    /**
     * Constructor creates 2D board of type TicTacToeBoard. Board is
     * represented by an object of type Configuration which
     * gives a board input parameters.
     * Also, constructor sets all cells in the board on UNASSIGNED
     * @param configuration is an object which stores all input parameters
     *                      (commands)
     */
    public TicTacToeBoard(Configuration configuration) {

        this.configuration = configuration;

        board = new char[configuration.getBoardSize()][configuration.getBoardSize()];

        for (int i = 0; i < configuration.getBoardSize(); i++) {
            for (int j = 0; j < configuration.getBoardSize(); j++) {

                board[i][j] = Configuration.UNASSIGNED;


            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    /**
     * hasWinner method uses methods for checking if a game has
     * already a winner
     * @return player's character if we found a winner, else return null
     */
    @Override
    public Character hasWinner() {

        for (char player : configuration.getPlayers()) {
            if (checkHorizontal(player)) return player;
            if (checkVertical(player)) return player;
            if (checkDiagonal(player)) return player;
        }

        return null;
    }

    @Override
    public void put(int x, int y, char symbol) {
        board[x][y] = symbol;
    }

    @Override
    public Character getCell(int x, int y) {
        return (board[x][y] == Configuration.UNASSIGNED) ? ' ' : board[x][y];
    }

    @Override
    public int size() {
        return configuration.getBoardSize();
    }

    @Override
    public boolean isFull() {
        int fullBoard = (int) Math.pow(configuration.getBoardSize(), 2);

        int assignedCells = 0;
        for (int row = 0; row < configuration.getBoardSize(); row++) {
            for (int col = 0; col < configuration.getBoardSize(); col++) {
                if (board[row][col] != Configuration.UNASSIGNED) {
                    assignedCells++;
                }
            }
        }

        return assignedCells == fullBoard;
    }

    @Override
    public boolean isEmpty(int x, int y) {
        return board[x][y] == Configuration.UNASSIGNED;
    }

    @Override
    public Board copy() {
        TicTacToeBoard copiedBoard = new TicTacToeBoard(configuration);
        copiedBoard.setBoard(Arrays.stream(board).map(char[]::clone).toArray(char[][]::new));
        return copiedBoard;
    }

    @Override
    public String format() {
        StringBuilder returnString = new StringBuilder();
        StringBuilder lineSeparator = new StringBuilder();

        lineSeparator.append("--".repeat(Math.max(0, configuration.getBoardSize())));
        lineSeparator.append("-\n");

        for (int i = 0; i < configuration.getBoardSize(); i++) {

            returnString.append(lineSeparator);

            for (int j = 0; j < configuration.getBoardSize(); j++) {
                returnString.append('|');
                returnString.append(getCell(i, j));

            }
            returnString.append('|');
            returnString.append('\n');
        }

        returnString.append(lineSeparator);

        return returnString.toString();
    }

    /**
     * Method for checking winner on horizontal line in the game board
     * @param player represents a character of a player
     * @return true if we have a winner on horizontals -> rows
     */
    private boolean checkHorizontal(char player) {

        //check every row
        for (int i = 0; i < configuration.getBoardSize(); i++) {
            //reset counter for the current row
            int counter = 0;

            //check each cell in the row
            for (int j = 0; j < configuration.getBoardSize(); j++) {
                //found player's symbol
                if (board[i][j] == (player)) {
                    counter++;
                    if (counter >= configuration.getWinSize())
                        return true;
                } else {
                    //player's sequence discontinued in the row, resetting the counter
                    counter = 0;
                }
            }
        }

        //winning sequence not found
        return false;
    }

    /**
     * Method for checking winner on vertical line in the game board
     * @param player represents a character of a player
     * @return true if we have a winner on verticals -> columns
     */
    private boolean checkVertical(char player) {

        //check every column
        for (int j = 0; j < configuration.getBoardSize(); j++) {
            //reset counter for the current column
            int counter = 0;

            //check each cell in the column
            for (int i = 0; i < configuration.getBoardSize(); i++) {
                //found player's symbol
                if (board[i][j] == (player)) {
                    counter++;
                    if (counter >= configuration.getWinSize())
                        return true;
                } else {
                    //player's sequence discountinued in the column, resetting the counter
                    counter = 0;
                }
            }
        }

        //winning sequence not found
        return false;
    }

    /**
     * Method for checking winner on diagonals in the game board
     * @param player represents a character of a player
     * @return true if we have a winner on horizontals
     */
    private boolean checkDiagonal(char player) {
        int win_size = configuration.getWinSize();
        int board_size = configuration.getBoardSize();
        int off_line = board_size - win_size;

        for (int i = 0; i <= off_line; i++) {
            for (int j = 0; j < board_size; j++) {
                if (board[i][j] == player) {
                    boolean won = true;
                    if (j + win_size - 1 < board_size) {
                        for (int x = 1; x < win_size; x++) {
                            if (board[i + x][j + x] != player) {
                                won = false;
                                break;
                            }
                        }

                        if (won) {
                            return true;
                        }
                    }

                    if (j - win_size + 1 >= 0) {
                        for (int x = 1; x < win_size; x++) {
                            if (board[i + x][j - x] != player) {
                                won = false;
                                break;
                            }
                        }

                        if (won) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}
