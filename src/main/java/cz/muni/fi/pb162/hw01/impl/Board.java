package cz.muni.fi.pb162.hw01.impl;

/**
 * Game board
 */
public interface Board {


    Character hasWinner();

    /**
     * Places a symbol on given coordinates
     *
     * @param x row
     * @param y column
     * @param symbol symbol
     */
    void put(int x, int y, char symbol);

    /**
     * Returns board's cell on given coordinates
     * @param x row
     * @param y column
     * @return character in cell or null
     */
    Character getCell(int x, int y);

    /**
     * @return size of the board
     */
    int size();

    /**
     * @return true if the board is completely filled by symbols
     */
    boolean isFull();

    /**
     * @param x row
     * @param y column
     * @return true if cell is empty
     */
    boolean isEmpty(int x, int y);

    /**
     * Creates a deep copy of this board
     * @return copy of this board
     */
    Board copy();

    /**
     * Returns formatted current board and
     * prints it on output
     * @return formatted board
     */
    String format();
}