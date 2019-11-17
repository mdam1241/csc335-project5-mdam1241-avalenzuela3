import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Tests {
	
	@Test
	void testBoardVictoryDown() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[i][0] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}

	@Test
	void testBoardVictoryRight() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[0][i] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}
	
	@Test
	void testBoardVictoryDownRight() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[i][i] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}
	
	@Test
	void testBoardVictoryUpRight() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[5 - i][i] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}
	
	@Test
	void testDrop() {
		Connect4Model board = new Connect4Model();
		board.dropToken(1, 0);
		board.dropToken(1, 0);
		board.dropToken(1,1);
		board.dropToken(2, 6);
		board.printDebug();
		assertFalse(board.checkVictory() > 0);
		board.dropToken(1, 0);
		board.dropToken(2, 0);
		board.dropToken(1, 0);
		board.printDebug();
		assertFalse(board.checkVictory() > 0);
	}
}