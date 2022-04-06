package cz.muni.fi.pb162.hw01.impl;


/**
 * Class configuration represents an object which stores
 * all input parameters.
 * @author Michaela Lodnanova
 */
public class Configuration implements GameConfiguration {

    public static final char UNASSIGNED = 1;

    private final int boardSize;
    private final int winSize;
    private final int historySize;
    private final char[] players;


    /**
     * Constructor creates an object Configuration which stores input.
     * @param boardSize The size of square matrix board
     * @param winSize The number of the same symbols in a line to win
     * @param historySize The number of moves to be stored for rewind
     * @param players The players characters
     */
    public Configuration(int boardSize, int winSize, int historySize, String players) {
        super();
        this.boardSize = boardSize;
        this.winSize = winSize;
        this.historySize = historySize;
        this.players = players.toCharArray();
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getWinSize() {
        return winSize;
    }

    public int getHistorySize() {
        return historySize;
    }

    public char[] getPlayers() {
        return players;
    }

    @Override
    public boolean isValid() {
        // winSize cannot be bigger than boardSize
        if (winSize > boardSize || winSize < 3){
            return false;
        }
        // validation of parameters
        if (boardSize < 3 || historySize < 0 || historySize > boardSize * boardSize){
            return false;
        }
        // players number must be > 1 + number of players should not be bigger than board size
        if (players.length <= 1 || players.length > boardSize){
            return false;
        }
        // player cannot be space ' '
        for (char player : players) {
            if (player == ' ') {
                return false;
            }
        }
        return true;
    }
}
