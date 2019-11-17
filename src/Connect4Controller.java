
/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Controller {

	public Connect4Model model;
	public Connect4View view;

	// Constructor
	public Connect4Controller(Connect4Model model, Connect4View view) {
		this.model = model;
		this.view = view;
	}

	public void newGame() {
		model.initializeBoard();
	}
	public void humanTurn(int col) {
		// TODO
	}

	public void computerTurn() {
		// TODO
	}

}