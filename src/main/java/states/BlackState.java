package states;

import static states.MoveValidationMethods.basicMoveTests;
import static states.MoveValidationMethods.emptyOrOwnPoint;
import static states.MoveValidationMethods.hit;

import game.Constant;
import model.Move;

public class BlackState implements GameState {

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
        return Constant.BLACK;
    }

    @Override
    public String toString() {
        return "Black State";
    }
}
