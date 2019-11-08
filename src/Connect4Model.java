import java.util.Observable;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Model extends Observable {
	public int row; // # of rows in board
	public int col; // # of columns in board
	
	public Connect4Model() {
		row = 6;
		col = 7;
	}
}