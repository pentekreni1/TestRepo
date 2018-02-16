package states;

import model.Move;

public interface GameState {

    /**
     * Test a move from point A to point B
     *
     * @param move
     * @return -1 for invalid move, 0 for hit, 1 for valid move
     */
    public int testMove(Move move);

    public int getColor();

    public String toString();

}
