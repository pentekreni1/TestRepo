package game;

import model.Checker;
import model.Die;
import model.Move;
import states.GameState;

import java.util.List;
import java.util.Stack;

public interface IGameBoard {
    public List<Checker> getRedCheckers();

    public List<Checker> getBlackCheckers();

    public GameState getRedState();

    public GameState getRedBarState();

    public GameState getRedBearOffState();

    public GameState getBlackState();

    public GameState getBlackBarState();

    public GameState getBlackBearOffState();

    public GameState getState();

    public void setState(GameState state);

    public Stack<Checker>[] getPoint();

    public void setUp();

    public void createAndPlaceCheckers(int[] redSetupPoints, int[] noOfRedCheckers, int[] blackSetupPoints, int[] noOfBlackCheckers);

    public boolean move(Move move);

    public boolean move(int fromPoint, Die die);

    public void nextPlayer();
}
