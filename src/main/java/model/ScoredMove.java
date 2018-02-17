package model;

import game.Constant;
import game.GameBoard;
import game.IGameBoard;
import model.Move;

public class ScoredMove {

    private int score;
    private Move move;
    private IGameBoard board;

    public static final int BEAROFF_WEIGHT = 150;
    public static final int CAPTURE_WEIGHT = 5;
    public static final int SINGLE_CHECKER_WEIGHT = -8;
    public static final int ENTER_HOME_WEIGHT = 20;
    public static final int POSITION_PONDERED_WEIGHT = 2;

    public ScoredMove(Move move, IGameBoard board) {
        this.move = move;
        this.board = board;
        getMoveScore();
    }

    public int getScore() {
        return this.score;
    }

    public Move getMove() {
        return move;
    }

    private void getMoveScore() {
        int toPosition = this.move.getToPosition();
        int color = board.getState().getColor();

        if (color == Constant.RED && toPosition == 25) {
            score += BEAROFF_WEIGHT;
        }

        if (color == Constant.RED && toPosition >= 19) {
            score += ENTER_HOME_WEIGHT;
        }

        if (color == Constant.RED) {
            score += toPosition*POSITION_PONDERED_WEIGHT;
        }
    }
}