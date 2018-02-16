package states;

import game.Constant;
import model.Move;

import static states.MoveValidationMethods.*;

public class BlackBarState implements GameState {

    public int testMove(Move move) {
        if (checkerFromBar(move.getFromPosition(), move.getToPosition(), getColor())) {
            if (emptyOrOwnPoint(move.getToPosition(), getColor())) {
                return 1;
            } else if (hit(move.getToPosition(), getColor())) {
                return 0;
            } else
                return -1;
        } else
            return -1;
    }

    public int getColor() {
        return Constant.BLACK;
    }

    @Override
    public String toString() {
        return "Black Bar State";
    }

}
