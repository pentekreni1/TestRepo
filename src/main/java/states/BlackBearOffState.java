package states;

import game.Constant;
import model.Move;

import static states.MoveValidationMethods.*;

public class BlackBearOffState implements GameState {

    public int testMove(Move move) {
        int toPosition = move.getToPosition();
        int fromPosition = move.getFromPosition();
        if (ownChecker(move.getFromPosition(), getColor()) && direction(fromPosition, toPosition, getColor())) {
            if (inBearOffRange(toPosition, getColor())) {
                if (emptyOrOwnPoint(toPosition, getColor())) {
                    return 1;
                } else if (hit(toPosition, getColor())) {
                    return 0;
                } else
                    return -1;
            } else if (toPosition < Constant.BLACK && !forcedMoves(new Move(toPosition, fromPosition), getColor())) {
                return 2;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public int getColor() {
        return Constant.BLACK;
    }

    @Override
    public String toString() {
        return "Black Bear-Off State";
    }
}
