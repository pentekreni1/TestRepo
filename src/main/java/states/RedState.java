package states;

import game.Constant;
import model.Move;

import static states.MoveValidationMethods.*;

public class RedState implements GameState {

    public int testMove(Move move) {
        if (basicMoveTests(move, getColor())) {
            if (emptyOrOwnPoint(move.getToPosition(), getColor())) {
                return 1;
            } else if (hit(move.getToPosition(), getColor())) {
                return 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int getColor() {
        return Constant.RED;
    }

    @Override
    public String toString() {
        return "Red State";
    }
}
