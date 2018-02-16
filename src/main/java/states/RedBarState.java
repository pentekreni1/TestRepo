package states;
import static states.MoveValidationMethods.*;

import game.Constant;
import model.Move;

public class RedBarState implements GameState {

	public int testMove(Move move) {
		if(checkerFromBar(move.getFromPosition(), move.getToPosition(), getColor())){
			if(emptyOrOwnPoint(move.getToPosition(), getColor())){
				return 1;
			}
			else if(hit(move.getToPosition(), getColor())){
				return 0;
			}else
				return -1;
		}else
		return -1;
	}

	public int getColor() {
		return Constant.RED;
	}
	
	@Override
	public String toString(){
		return "Red Bar State";
	}
}
