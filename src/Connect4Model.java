import java.util.Observable;

/**
 * Holds the board of Connect4.
 * @author Michael Dam, Aaron Valenzuela
 *
 * Internally uses a 2D array/grid of integers called board.
 * Board is int row tall and int col wide.
 * Use board[0][0] to access the top left, board[row-1][0] to access bottom left...
 * dropToken(token, col) drops a token from top to bottom into the 2D array.
 * VALID TOKENS:
 * 1 - YELLOW
 * 2 - RED
 * A 0 in the grid means that slot is empty.
 * Initializing the board fills the grid with 0.
 * 
 * When a token is dropped or the board is initialized, all Observers will be notified
 * and passed the row/col of the position that changed, and the token it changed to.
 * It will be passed 0 for color when the board is initialized.
 */

public class Connect4Model extends Observable {
	private int row; 		// # of rows in board
	private int col; 		// # of columns in board
	private int[][] board;
	
	public Connect4Model() {
		row = 6;
		col = 7;
		initializeBoard();
	}
	
	/**
	 * Clears the board, for starting new games.
	 */
	public void initializeBoard() {
		board = new int[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				board[i][j] = 0;
			}
		}
		// last parameter of Connect4MoveMessage tells the View it is a new game.
		Connect4MoveMessage newGame =  new Connect4MoveMessage(0,0,0);
		setChanged();
		notifyObservers(newGame);
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public int getCol() {
		return col;
	}
	
	/**
	 * Drops a token in a column.
	 * @param col an int telling which column to drop token in.
	 * @param token an int telling what type of token to use (1 = YELLOW, 2 = RED)
	 * @return false if dropToken failed (including bound errors, or simply a full column), true if dropToken succeeded
	 */
	public boolean dropToken(int token, int col) {
		if (col < 0 || col >= this.col) {
			System.err.println("Gave dropToken() a column out of bounds");
			return false;
		}
		if (token != 1 && token != 2) {
			System.err.println("Gave an invalid number for type of token");
			return false;
		}
		if (board[0][col] > 0) {
			// The column you tried to drop a token in is full!
			return false;
		}
		int i = 0;
		int row = 0;
		boolean dropped = false;
		while (i < this.row - 1 && !dropped) {
			if (board[i+1][col] > 0) {
				board[i][col] = token;
				dropped = true;
				row = i;
			}
			i++;
		}
		if (!dropped) {
			board[this.row - 1][col] = token;
			row = this.row - 1;
		}
		// NOTIFY OBSERVERS HERE, make sure you pass any info you need in VIEW through here.
		Connect4MoveMessage moveMsg = new Connect4MoveMessage(row, col, token);
		setChanged();
		notifyObservers(moveMsg);
		return true;
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
	
	/**
	 * Prints to console what the model looks like in the form of a grid of integers.
	 */
	public void printDebug() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Checks the token of given position with the 3 positions to the right.
	 * @param row int row of the position to be checked
	 * @param col int col of the position to be checked
	 * @return 0 if token differs, else return that token's value
	 */
	private int checkRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row][col + i]) { return 0; }
		}
		return token;
	}
	/**
	 * Checks the token of given position with the 3 positions below.
	 * @param row int row of the position to be checked
	 * @param col int col of the position to be checked
	 * @return 0 if token differs, else return that token's value
	 */
	private int checkDown(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row + i][col]) { return 0; }
		}
		return token;
	}
	/**
	 * Checks the token of given position with the 3 positions diagonally down right.
	 * @param row int row of the position to be checked
	 * @param col int col of the position to be checked
	 * @return 0 if token differs, else return that token's value
	 */
	private int checkDownRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row + i][col + i]) { return 0; }
		}
		return token;
	}
	/**
	 * Checks the token of given position with the 3 positions diagonally upper right.
	 * @param row int row of the position to be checked
	 * @param col int col of the position to be checked
	 * @return 0 if token differs, else return that token's value
	 */
	private int checkUpRight(int row, int col) {
		int token = board[row][col];
		if (token == 0) { return 0; }
		for (int i = 1; i < 4; i++) {
			if (token != board[row - i][col + i]) { return 0; }
		}
		return token;
	}
}