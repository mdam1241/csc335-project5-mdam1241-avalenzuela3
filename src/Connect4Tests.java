import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Tests {
	
	@Test
	void testBoard() {
		Connect4Model board = new Connect4Model();
		board.printDebug();
		board.dropToken(1, 0);
		board.dropToken(1, 0);
		board.dropToken(1,1);
		board.dropToken(2, 6);
		board.printDebug();
		assertFalse(board.checkVictory() > 0);
		board.dropToken(1, 0);
		board.dropToken(2, 0);
		board.dropToken(1, 0);
		assertFalse(board.checkVictory() > 0);
	}
}