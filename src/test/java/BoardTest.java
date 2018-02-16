import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Stack;

import game.*;
import model.Checker;
import model.Die;
import model.Move;
import org.junit.Test;

import states.MoveValidationMethods;

public class BoardTest {
	GameBoard board = GameBoard.getInstance();
	Die mockDie = mock(Die.class);

	@Test
	public void testBoardSetUp() {

		board.setUp();

		assertEquals(2, board.getPoint()[1].size());
		assertEquals(5, board.getPoint()[6].size());
		assertEquals(3, board.getPoint()[8].size());
		assertEquals(5, board.getPoint()[12].size());
		assertEquals(5, board.getPoint()[13].size());
		assertEquals(3, board.getPoint()[17].size());
		assertEquals(5, board.getPoint()[19].size());
		assertEquals(2, board.getPoint()[24].size());
	}

	@Test
	public void testBoardMoveWithDieArgument() {
		board.setUp();

		// move red
		board.setState(board.getRedState());
		when(mockDie.getValue()).thenReturn(4);
		assertTrue(board.move(1, mockDie));
		assertEquals(1, board.getPoint()[5].size());
		assertEquals(1, board.getPoint()[1].size());
		// move black
		board.setState(board.getBlackState());
		when(mockDie.getValue()).thenReturn(3);
		assertTrue(board.move(24, mockDie));
		assertEquals(1, board.getPoint()[21].size());
		assertEquals(1, board.getPoint()[24].size());
		// invalid move
		when(mockDie.getValue()).thenReturn(1);
		assertFalse(board.move(13, mockDie));
		assertEquals(5, board.getPoint()[13].size());
		assertEquals(5, board.getPoint()[12].size());
		// out of bounds
		when(mockDie.getValue()).thenReturn(6);
		assertFalse(board.move(6, mockDie));
		// hit
		board.setState(board.getRedState());
		when(mockDie.getValue()).thenReturn(2);
		assertTrue(board.move(19, mockDie));
		assertEquals(1, board.getPoint()[21].size());
		assertTrue(board.getPoint()[21].peek().color == Constant.RED);
		assertEquals(1, board.getPoint()[Constant.BLACKBAR].size());
		assertTrue(board.getPoint()[Constant.BLACKBAR].peek().color == Constant.BLACK);
	}
	
	@Test
	public void testNextPlayer(){
		board.setUp();
		
		board.setState(board.getBlackState());
		assertTrue(board.move(new Move(24, 22)));
		board.nextPlayer();
		assertTrue(board.getState() == board.getRedState());
		assertFalse(board.move(new Move(24, 22)));
		assertTrue(board.move(new Move(19, 22)));
		assertTrue(board.getPoint()[Constant.BLACKBAR].peek().color == Constant.BLACK);
	}
	
	@Test
	public void testMoveOpponentChecker(){
		board.setUp();
		
		board.setState(board.getBlackState());
		assertTrue(board.move(new Move(6, 5)));
		assertFalse(board.move(new Move(1, 2)));
		board.setState(board.getRedState());
		assertTrue(board.move(new Move(1, 2)));
		assertFalse(board.move(new Move(6, 5)));
	}
	
	@Test
	public void testInvalidMoves(){
		board.setUp();
		
		board.setState(board.getBlackState());
		//move in wrong direction
		assertFalse(board.move(new Move(24, 25)));
		//move 10 steps
		assertFalse(board.move(new Move(24, 14)));
		//bear-off before all checkers have reached the home board
		assertFalse(board.move(new Move(6, 0)));
		
		board.setState(board.getRedState());
		//move in wrong direction
		assertFalse(board.move(new Move(19, 18)));
		//move 10 steps
		assertFalse(board.move(new Move(12, 22)));
		//bear-off before all checkers have reached the home board
		assertFalse(board.move(new Move(19, 25)));
	}
	
	@Test
	public void testBarState(){
		board.setUp();
		
		board.setState(board.getRedState());
		board.move(new Move(1, 3));
		board.nextPlayer();
		board.move(new Move(6, 3));
		
		board.nextPlayer();
		assertTrue(board.getState() == board.getRedBarState());
		// try move a checker on the board instead of checker from the bar
		assertFalse(board.move(new Move(1, 2)));
		// move checker to point held by opponent checkers
		assertFalse(board.move(new Move(Constant.REDBAR, 6)));
		// hit opponent 
		assertTrue(board.move(new Move(Constant.REDBAR, 3)));
		board.nextPlayer();
		assertTrue(board.getState() == board.getBlackBarState());
		// try move a checker on the board instead of checker from the bar
		assertFalse(board.move(new Move(13, 11)));
		// try to move to an invalid point
		assertFalse(board.move(new Move(Constant.BLACKBAR, 18)));
		// move to a valid point
		assertTrue(board.move(new Move(Constant.BLACKBAR, 21)));
	}
	
	@Test
	public void testBearOffState(){
		for(Stack<Checker> s: board.getPoint()){
			s.clear();
		}
		
		board.getPoint()[19].add(new Checker(Constant.RED));
		board.getPoint()[22].add(new Checker(Constant.RED));
		board.getPoint()[24].add(new Checker(Constant.RED));

		board.getPoint()[1].add(new Checker(Constant.BLACK));
		board.getPoint()[3].add(new Checker(Constant.BLACK));
		board.getPoint()[5].add(new Checker(Constant.BLACK));
		
		board.setState(board.getRedBearOffState());
		
		assertTrue(board.move(new Move(24, Constant.RED)));
		when(mockDie.getValue()).thenReturn(4);
		assertFalse(board.move(22, mockDie));
		assertTrue(board.move(new Move(19, 25)));
		assertTrue(board.move(22, mockDie));
		assertFalse(board.move(new Move(25, 22)));
		
		board.setState(board.getBlackBearOffState());
		assertFalse(board.move(new Move(3, -2)));
		assertTrue(board.move(new Move(5, 0)));
		assertTrue(board.move(new Move(3, -2)));
		assertTrue(board.move(new Move(1, -5)));
		assertFalse(board.move(new Move(0, 1)));
		
		assertEquals(3, board.getPoint()[0].size());
		assertEquals(3, board.getPoint()[25].size());
	}
	
	@Test
	public void testDetectBearOffState(){
		board.setUp();
		board.setState(board.getRedState());
		
		for (int i = 0; i < 5; i++) {
			board.move(new Move(12, 18));
			board.move(new Move(18, 23));
		}
		
		for(int i = 0; i < 2; i++){
			board.move(new Move(1, 7));
			board.move(new Move(7, 12));
			board.move(new Move(12, 18));
			board.move(new Move(18, 21));
			board.move(new Move(17, 20));
		}
		
		board.move(new Move(17, 20));
		board.move(new Move(23, 25));
		assertEquals(board.getRedBearOffState(), board.getState());
	}
	
	@Test
	public void testMoveOuterChecker(){
		int[] redSetupPoints = new int[] {22, 23, 24 };
		int[] noOfRedCheckers = new int[] {5, 5, 5 };
		int[] blackSetupPoints = new int[] { 5, 3, 2, 1, 0 };
		int[] noOfBlackCheckers = new int[] { 3, 1, 3, 3, 5 };
		board.createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
		
		Game g = new Game();
		board.setState(board.getBlackBearOffState());
		g.getDice().add(new Die(6));
		assertTrue(g.move(new Move(5, 0)));
	
	}
	
	@Test
	public void testGetValidMovesForChecker(){
		int[] redSetupPoints = new int[] {23, 24};
		int[] noOfRedCheckers = new int[] {1, 1};
		int[] blackSetupPoints = new int[] {2, 1};
		int[] noOfBlackCheckers = new int[] {1, 1 };
		board.createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
		board.setState(board.getRedBearOffState());
		
		Game g = new Game();
		g.getDice().add(new Die(6));
		List<Integer> results = MoveValidationMethods.getValidMovesForChecker(board.getPoint()[24].peek(), g.getDice());
		assertTrue(results.isEmpty());
		results = MoveValidationMethods.getValidMovesForChecker(board.getPoint()[23].peek(), g.getDice());
		assertEquals(new Integer(25), results.get(0));
		board.nextPlayer();
		assertTrue(g.getDice().get(0).getValue() == 6);
		results = MoveValidationMethods.getValidMovesForChecker(board.getPoint()[1].peek(), g.getDice());
		assertTrue(results.isEmpty());
		results = MoveValidationMethods.getValidMovesForChecker(board.getPoint()[2].peek(), g.getDice());
		assertEquals(new Integer(0), results.get(0));
	}
	
	@Test
	public void testBlackWin(){
		int[] redSetupPoints = new int[] {24};
		int[] noOfRedCheckers = new int[] {2};
		int[] blackSetupPoints = new int[] {1};
		int[] noOfBlackCheckers = new int[] {2};
		board.createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
		
		Game g = new Game();
		board.setState(board.getBlackBearOffState());
		g.getDice().add(new Die(1));
		g.getDice().add(new Die(1));
		assertFalse(g.checkWinner());
		g.move(new Move(1, 0));
		g.move(new Move(1, 0));
		board.setState(board.getBlackBearOffState());
		assertTrue(g.checkWinner() && board.getState().getColor() == Constant.BLACK);
	}
	
	@Test
	public void testRedWin(){
		int[] redSetupPoints = new int[] {24};
		int[] noOfRedCheckers = new int[] {2};
		int[] blackSetupPoints = new int[] {1};
		int[] noOfBlackCheckers = new int[] {2};
		board.createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
		
		Game g = new Game();
		board.setState(board.getRedBearOffState());
		g.getDice().add(new Die(1));
		g.getDice().add(new Die(1));
		assertFalse(g.checkWinner());
		g.move(new Move(24, 25));
		g.move(new Move(24, 25));
		board.setState(board.getRedBearOffState());
		assertTrue(g.checkWinner() && board.getState().getColor() == Constant.RED);
	}

}
