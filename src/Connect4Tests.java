import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.application.Application;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Tests {
	
	@Test
	void testTopRightBoardVictoryDown() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[i][6] = 1;
		}
		board.printDebug();
		assertTrue(board.checkVictory() == 1);
	}
	

	@Test
	void testTopLeftBoardVictoryDown() {
		Connect4Model board = new Connect4Model();
		for (int i = 0; i < 4; i++) {
			board.getBoard()[i][1] = 1;
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
		assertTrue(board.getCol() == 7);
		assertFalse(board.dropToken(0,0)); // Tried to drop an "empty" token
		assertFalse(board.dropToken(1, -1)); // Tried to drop a token out of bounds
		assertFalse(board.dropToken(2, 7)); // Tried to drop a token out of bounds
		board.dropToken(1, 0);
		board.printDebug();
		board.dropToken(1, 0);
		board.printDebug();
		board.dropToken(1,1);
		board.dropToken(2, 6);
		assertFalse(board.checkVictory() > 0);
		board.dropToken(1, 0);
		board.dropToken(2, 0);
		board.dropToken(1, 0);
		assertFalse(board.checkVictory() > 0);
		assertTrue(board.dropToken(1,0)); // Fill the last slot in column 1, dropToken is true
		assertFalse(board.dropToken(1,0)); // Cannot fit another token in column 1, dropToken is false
		board.dropToken(1, 2);
		board.dropToken(1, 3);
		assertTrue(board.checkVictory() == 1);
		board.printDebug();
	}
	@Test
	void testController() {
		Connect4Controller controller = new Connect4Controller(new Connect4Model(), new Connect4View());
		assertTrue(controller.isHuman()); // Default values before network is set up.
		assertFalse(controller.isMyTurn());
		// Controller is too dependent on input from classes running on JavaFX threads to unit test.
		// Must run 2 Applications, connect network, and play game to test controller.
	}
	@Test
	void testMessage() {
		Connect4MoveMessage msg = new Connect4MoveMessage(1,1,1);
		assertTrue(msg.getColor() == 1);
		assertTrue(msg.getColumn() == 1);
		assertTrue(msg.getRow() == 1);
	}
}