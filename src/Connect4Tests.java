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
		assertFalse(board.checkVictory() == 1);
		for (int i = 0; i < 4; i++) {
			board.getBoard()[0][i] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}
	
	@Test
	void testDrop() {
		Connect4Model board = new Connect4Model();
		board.printDebug();
		board.dropToken(1, 0);
		board.printDebug();
		board.dropToken(1, 0);
		board.printDebug();
		board.dropToken(1,1);
		board.printDebug();
		board.dropToken(2, 6);
		board.printDebug();
		assertFalse(board.checkVictory() > 0);
		board.dropToken(1, 0);
		board.printDebug();
		board.dropToken(2, 0);
		board.printDebug();
		board.dropToken(1, 0);
		board.printDebug();
		assertFalse(board.checkVictory() > 0);
	}
}