package game;

import model.Checker;
import model.Die;
import model.Move;
import model.ScoredMove;
import states.MoveValidationMethods;

import java.util.*;

public class Game implements GameSubject {

    private ArrayList<Observer> observers = new ArrayList<Observer>();
    private ArrayList<Die> dice = new ArrayList<Die>();
    private int player = Constant.BLACK;
    private boolean rolled = false;
    private IGameBoard board = GameBoard.getInstance();

    public Game(IGameBoard iGameBoard){
        board = iGameBoard;
    }

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(0);
    }

    public ArrayList<Die> getDice() {
        return dice;
    }

    public int getPlayer() {
        return player;
    }

    public boolean roll() {
        if (!rolled) {
            dice.clear();
            dice.add(new Die());
            dice.add(new Die());
            dice.get(0).roll();
            dice.get(1).roll();
            if (dice.get(0).getValue() == dice.get(1).getValue()) {
                dice.add(new Die(dice.get(0)));
                dice.add(new Die(dice.get(0)));
            }
            rolled = true;
            notifyDiceStatus();
            checkPossibleMoves();
            return true;
        }
        return false;
    }

    public void checkPossibleMoves() {
        if (!MoveValidationMethods.possibleMoves(dice)) {
            notifyNoMoves();
            board.nextPlayer();
            rolled = false;
        }
    }

    public Move getBestMove() {
        List<Checker> currentCheckers = new ArrayList<>();
        List<Move> legalMoves = new ArrayList<>();
        ScoredMove[] scoredMoves;
        int bestMoveIndex = 0;

        if (board.getState().getColor() == Constant.BLACK) {
            currentCheckers = board.getBlackCheckers();
        } else {
            currentCheckers = board.getRedCheckers();
        }

        currentCheckers = removeDuplicates(currentCheckers);

        currentCheckers.forEach(checker -> {
            List<Integer> validToPositionsForCurrentChecker = MoveValidationMethods.getValidMovesForChecker(checker, dice);
            validToPositionsForCurrentChecker.forEach(toPos -> legalMoves.add(new Move(checker.getPosition(), toPos)));
        });

        scoredMoves = new ScoredMove[legalMoves.size()];
        for (int i = 0; i < legalMoves.size(); i++) {
            scoredMoves[i] = new ScoredMove(legalMoves.get(i), board);
        }
        int max = 0;
        for (int i = 0; i < legalMoves.size(); i++) {
            int score = scoredMoves[i].getScore();
            if (score > max) {
                bestMoveIndex = i;
                max = score;
            }
        }

        return scoredMoves[bestMoveIndex].getMove();
    }

    public boolean move(Move move) {
        int steps;
        int fromPosition = move.getFromPosition();
        int toPosition = move.getToPosition();
        if (fromPosition == Constant.REDBAR || fromPosition == Constant.BLACKBAR) {
            steps = (fromPosition == Constant.REDBAR) ? toPosition : 25 - toPosition;
        } else {
            steps = Math.abs(fromPosition - toPosition);
        }
        for (Die d : dice) {
            if (d.getValue() == steps) {
                if (board.move(move)) {
                    dice.remove(d);
                    notifyDiceStatus();
                    checkWinner();
                    if (!dice.isEmpty()) {
                        checkPossibleMoves();
                    } else {
                        rolled = false;
                        board.nextPlayer();
                    }
                    return true;
                }
            }
        }

        if (board.getState() == board.getBlackBearOffState() || board.getState() == board.getRedBearOffState()) {
            boolean availableMovesInBearOff = false;
            for (Die d : dice) {
                int toPoint = (board.getState().getColor() == Constant.BLACK) ? fromPosition - d.getValue()
                        : fromPosition + d.getValue();
                if (MoveValidationMethods.forcedMoves(new Move(toPoint, fromPosition), board.getState().getColor())) {
                    availableMovesInBearOff = true;
                }
            }
            if (!availableMovesInBearOff) {
                dice.sort(new Comparator<Die>() {

                    public int compare(Die o1, Die o2) {
                        return o1.getValue() - o2.getValue();
                    }
                });
                int outermostChecker = MoveValidationMethods.positionOfOuterMostChecker(board.getState().getColor());
                for (int i = 0; i < dice.size(); i++) {
                    if (steps < dice.get(i).getValue() && fromPosition == outermostChecker && board.move(new Move(fromPosition, toPosition))) {
                        dice.remove(dice.get(i));
                        notifyDiceStatus();
                        checkWinner();
                        if (!dice.isEmpty()) {
                            checkPossibleMoves();
                        } else {
                            board.nextPlayer();
                            rolled = false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void removeDice() {
        dice.clear();
        notifyDiceStatus();
        rolled = false;
    }

    public boolean checkWinner() {
        List<Checker> testList = board.getState().getColor() == Constant.BLACK ? board.getBlackCheckers() : board.getRedCheckers();
        int home = board.getState().getColor() == Constant.BLACK ? Constant.BLACK : Constant.RED;
        for (Checker c : testList) {
            if (c.getPosition() != home) {
                return false;
            }
        }
        notifyWinner();
        return true;
    }

    public void notifyDiceStatus() {
        for (Observer o : observers) {
            o.drawDice(dice);
            o.countBearOff();
        }
    }

    private List<Checker> removeDuplicates(List<Checker> currentCheckers) {
        Set<Checker> hs = new HashSet<>();
        hs.addAll(currentCheckers);
        currentCheckers.clear();
        currentCheckers.addAll(hs);
        return currentCheckers;
    }

    private void notifyNoMoves() {
        for (Observer o : observers) {
            o.notifyNoMoves();
        }
    }

    private void notifyWinner() {
        for (Observer o : observers) {
            o.notifyWinner(board.getState().getColor());
        }
    }
}