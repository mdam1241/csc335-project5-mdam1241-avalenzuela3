import java.util.Observable;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Model extends Observable {
	private int row; 		// # of rows in board
	private int col; 		// # of columns in board
	private int[][] board;	// 0 means empty, 1 means yellow, 2 means red
	
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
	}
}