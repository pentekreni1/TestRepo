package states;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import game.*;
import model.Checker;
import model.Die;
import model.Move;

public class MoveValidationMethods {
	private static IGameBoard board = GameBoard.getInstance();
	private static Stack<Checker>[] points = board.getPoint();

	public static void setGameBoard(IGameBoard iGameBoard){
		board = iGameBoard;
		points = board.getPoint();
	}

	public static boolean basicMoveTests(Move move, int stateColor) {
		int fromPoint = move.getFromPosition();
		int toPoint = move.getToPosition();
		return	ownChecker(fromPoint, stateColor) &&
				dieValue(fromPoint, toPoint) && 
				inBounds(toPoint) && 
				direction(fromPoint, toPoint, stateColor);
	}

	public static boolean dieValue(int fromPoint, int toPoint) {
		int value = Math.abs(toPoint - fromPoint);
		return value > 0 && value < 7;
	}

	public static boolean inBounds(int toPoint) {
		return toPoint > Constant.BLACK && toPoint < Constant.RED;
	}

	public static boolean ownChecker(int fromPoint, int stateColor) {
		return !points[fromPoint].empty() && 
				points[fromPoint].peek().color == stateColor;
	}

	public static boolean direction(int fromPoint, int toPoint, int stateColor) {
		int steps = (fromPoint == Constant.REDBAR) ? toPoint : (toPoint - fromPoint);
		return stateColor == Constant.RED && steps > 0 || stateColor == Constant.BLACK && steps < 0;
	}

	public static boolean emptyOrOwnPoint(int toPoint, int stateColor) {
		return points[toPoint].isEmpty() || points[toPoint].peek().color == stateColor;
	}

	public static boolean hit(int toPoint, int stateColor) {
		return points[toPoint].size() == 1 && points[toPoint].peek().color != stateColor;
	}

	public static boolean checkerFromBar(int fromPoint, int toPoint, int stateColor) {
		return (stateColor == Constant.RED && 
				fromPoint == Constant.REDBAR && 
				toPoint <= 6 && toPoint >= 1 
				) || (
				stateColor == Constant.BLACK && 
				fromPoint == Constant.BLACKBAR && 
				toPoint >= 19 && toPoint <= 24 );
	}

	public static boolean inBearOffRange(int toPoint, int stateColor) {
		return (stateColor == Constant.RED && 
				toPoint >= 20 && 
				toPoint <= 25
				) || ( 
				stateColor == Constant.BLACK && 
				toPoint >= 0 && 
				toPoint <= 5);
	}
	
	public static boolean availableMovesInBearOff(List<Die> dice, int fromPos){
		boolean toReturn = false;
		for (Die d : dice) {
			int toPos = (board.getState().getColor() == Constant.BLACK) ? fromPos - d.getValue()
					: fromPos + d.getValue();
			if (MoveValidationMethods.forcedMoves(new Move(fromPos, toPos), board.getState().getColor())) {
				toReturn = true;
			}
		}
		return toReturn;
	}

	public static boolean forcedMoves(Move move, int stateColor) {
		int steps = Math.abs(move.getToPosition() - move.getFromPosition());
		boolean toReturn = false;
		if (stateColor == Constant.RED) {
			for (int i = 25 - steps; i > 18; i--) {
				if (!points[i].empty() && points[i].peek().color == Constant.RED) {
					toReturn = true;
				}
			}
		} else {
			for (int i = steps; i < 7; i++) {
				if (!points[i].empty() && points[i].peek().color == Constant.BLACK) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	public static int positionOfOuterMostChecker(int stateColor) {
		int toReturn;
		if (stateColor == Constant.RED) {
			toReturn = 25;
			for (Checker c : board.getRedCheckers()) {
				if (c.getPosition() < toReturn) {
					toReturn = c.getPosition();
				}
			}
		} else {
			toReturn = 0;
			for (Checker c : board.getBlackCheckers()) {
				if (c.getPosition() > toReturn) {
					toReturn = c.getPosition();
				}
			}
		}
		return toReturn;
	}

	public static boolean possibleMoves(List<Die> dice) {
		if (board.getState() == board.getBlackBarState()) {
			for (Die d : dice) {
				if (board.getState().testMove(new Move(Constant.BLACKBAR, 25 - d.getValue())) != -1) {
					return true;
				}
			}
		} else if (board.getState() == board.getRedBarState()) {
			for (Die d : dice) {
				if (board.getState().testMove(new Move(Constant.REDBAR, d.getValue())) != -1) {
					return true;
				}
			}
		} else if (board.getState() == board.getBlackState()) {
			for (Checker c : board.getBlackCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(new Move(c.getPosition(), c.getPosition() - d.getValue())) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getRedState()) {
			for (Checker c : board.getRedCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(new Move(c.getPosition(), d.getValue() + c.getPosition())) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getBlackBearOffState()) {
			for (Checker c : board.getBlackCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(new Move(c.getPosition(), c.getPosition() - d.getValue())) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getRedBearOffState()) {
			for (Checker c : board.getRedCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(new Move(c.getPosition(), c.getPosition() + d.getValue())) != -1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static List<Integer> getValidMovesForChecker(Checker c, List<Die> dice) {
		List<Integer> toReturn = new ArrayList<Integer>();
		GameState state = board.getState();
		int toPoint;

		for (Die d : dice) {
			if (state == board.getBlackBarState() || state == board.getRedBarState()) {
				toPoint = (state.getColor() == Constant.BLACK) ? (Constant.RED - d.getValue()) : d.getValue();
			}else{
				toPoint = (state.getColor() == Constant.BLACK) ? (c.getPosition() - d.getValue())
						: (c.getPosition() + d.getValue());
			}
			if (state.testMove(new Move(c.getPosition(), toPoint)) != -1 && toPoint<=Constant.RED && toPoint>=Constant.BLACK) {
				toReturn.add(new Integer(toPoint));
			}else if((state == board.getBlackBearOffState() || state == board.getRedBearOffState()) 
					&& !availableMovesInBearOff(dice, c.getPosition()) 
					&& positionOfOuterMostChecker(state.getColor()) == c.getPosition()){
				toPoint = state.getColor() == Constant.BLACK ? Constant.BLACK : Constant.RED;
				toReturn.add(new Integer(toPoint));
			}
		}
		return toReturn;
	}

}
