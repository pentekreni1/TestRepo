package model;

import game.Constant;
import game.IGameBoard;

public class ScoredMove {

    private int score = 1;
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

        if (color == Constant.BLACK) {
            score += (25 - toPosition) * POSITION_PONDERED_WEIGHT;

            if (toPosition == 0) {
                score += BEAROFF_WEIGHT;
            }

            if (toPosition <= 6) {
                score += ENTER_HOME_WEIGHT;
            }
        }
    }
}