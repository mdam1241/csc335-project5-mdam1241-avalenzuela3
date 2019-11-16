import java.util.Observable;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Model extends Observable {
	public int row; 		// # of rows in board
	public int col; 		// # of columns in board
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
}