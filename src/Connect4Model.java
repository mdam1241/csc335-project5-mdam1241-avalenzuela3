import java.util.Observable;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Model extends Observable {
	private int row; 		// # of rows in board
	private int col; 		// # of columns in board
	private int[][] board;	// board[0][0] gives the token at the very top left of the board.
							// board[row][col] gives the token at the bottom right.
							// For token types: 0 = empty, 1 = yellow, 2 = red
	
	public Connect4Model() {
		row = 6;
		col = 7;
		board = new int[row][col];
	}
	
	/**
	 * Clears the old board, for starting new games.
	 */
	public void clearBoard() {
		board = new int[row][col];
		// NOTIFY OBSERVERS HERE, make sure you pass any info you need in VIEW through here.
		notifyObservers();
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * Drops a token in a column.
	 * @param col an int telling which column to drop token in.
	 * @param chip an int telling what type of token to use (1 = YELLOW, 2 = RED)
	 */
	public void dropToken(int token, int col) {
		if (col < 0 || col > this.col) {
			System.err.print("Gave dropRed() a column out of bounds");
			return;
		}
		if (token != 1 || token != 2) {
			System.err.print("Gave an invalid number for type of token");
			return;
		}
		int i = 0;
		boolean dropped = false;
		while (i < row - 1 && !dropped) {
			if (board[i+1][col] > 0) {
				board[i][col] = token;
				dropped = true;
			}
			i++;
		}
		if (dropped = false) {
			board[row][col] = token;
		}
		// NOTIFY OBSERVERS HERE, make sure you pass any info you need in VIEW through here.
		notifyObservers();
	}
	
	/**
	 * Checks the current state of the board for any victories.
	 * @return 0 for none, 1 for yellow, 2 for red victory
	 */
	public int checkVictory() {
		for (int i = 0; i < row - 3; i++) {
			for (int j = 0; j < col - 3; j++) {
				// Check top left corner of board
				if (checkRight(i,j) != 0) { return checkRight(i,j); }
				if (checkDown(i,j) != 0) { return checkDown(i,j); }
				if (checkDownRight(i,j) != 0) { return checkDownRight(i,j); }
			}
		}
		for (int i = 3; i < row; i++) {
			for (int j = 0; j < col - 3; j++) {
				// Check bottom left corner of board
				if (checkUpRight(i,j) != 0) { return checkUpRight(i,j); }
				if (checkRight(i,j) != 0) { return checkRight(i,j); }
			}
		}
		for (int i = 0; i < row - 3; i++) {
			for (int j = 3; j < col; j++) {
				// Check top right corner of board
				if (checkDown(i,j) != 0) { return checkDown(i,j); }
			}
		}
		return 0;
	}
	
	public void printDebug() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
	}
	
	private int checkRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row][col + i]) { return 0; }
		}
		return token;
	}
	private int checkDown(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row + i][col]) { return 0; }
		}
		return token;
	}
	private int checkDownRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row + i][col + i]) { return 0; }
		}
		return token;
	}
	private int checkUpRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row - i][col + i]) { return 0; }
		}
		return token;
	}
}