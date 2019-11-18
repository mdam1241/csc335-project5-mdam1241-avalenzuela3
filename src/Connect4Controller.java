import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Controller {

	public Connect4Model model;
	public Connect4View view;

	// Constructor
	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}

	/**
	 * Calls the model's initiliazeBoardMethod() to clear all the slots
	 */
	public void newGame() {
		model.initializeBoard();
	}

	public void humanTurn(int col) {
		// TODO
	}

	public void computerTurn() {
		// TODO
	}

	/**
	 * This method is called by the View when a column has been clicked by the user. 
	 * The controller determines whether the column clicked is full or not and changes 
	 * the model if is not. If it is, it displays an Error message
	 * 
	 * @param moveObj Object containing a player's token color and the move they made.
	 */
	public void moveMade(Connect4MoveMessage moveObj) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("Column full, pick somewhere else!");
		boolean validMove = model.dropToken(moveObj.getColor(), moveObj.getColumn());
		if (!validMove) {
			alert.showAndWait();
		}
	}

}