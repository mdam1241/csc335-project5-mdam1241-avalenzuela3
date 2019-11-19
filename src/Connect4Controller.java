import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4Controller {

	public Connect4Model model;
	public Connect4View view;
	
	// Networking
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	// Constructor
	public Connect4Controller(Connect4Model model, Connect4View view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * Calls the model's initiliazeBoardMethod() to clear all the slots
	 */
	public void newGame() {
		model.initializeBoard();
	}

	public void humanTurn(int col) {
		model.dropToken(1, col); // TODO: This ASSUMES the current controller represents YELLOW.
	}

	public void computerTurn() {
		boolean validMove = false;
		int randomCol = 0;
		while (!validMove) {
			randomCol = (int) Math.floor(Math.random() * model.getCol());
			if (model.getBoard()[0][randomCol] == 0) {
				validMove = true;
			}
		}
		model.dropToken(1,randomCol); // TODO: This ASSUMES the current controller represents YELLOW.
	}

	/**
	 * This method is called by the View when a column has been clicked by the user. 
	 * The controller determines whether the column clicked is full or not and changes 
	 * the model if is not. If it is, it displays an Error message
	 * 
	 * @param moveObj Object containing a player's token color and the move they made.
	 */
	public void moveMade(Connect4MoveMessage moveObj) {
		boolean validMove = model.dropToken(moveObj.getColor(), moveObj.getColumn());
		int winner = model.checkVictory();
		if (!validMove) {
			view.fullColumn();
		} else if (winner > 0) {
			view.displayWinner(winner, moveObj.getColor());
		}
	}
	/**
	 * Called by View when the Network Setup dialog box is closed.
	 * @param setup A custom stage that contains fields for user-given network settings.
	 */
	public void setupNetwork(NetworkSetupDialogBox setup) {
		if (!setup.isOk()) {
			return;
		}
		closeLooseConnections();
		if (setup.isServer()) {
			setupServer(setup);
		} else {
			setupClient(setup);
		}
	}
	private void setupServer(NetworkSetupDialogBox setup) {
		try {
			server = new ServerSocket(setup.getPort());
			connection = server.accept();
			output = new
				ObjectOutputStream(connection.getOutputStream());
			input = new 
				ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void setupClient(NetworkSetupDialogBox setup) {
		try {
			connection = new Socket(setup.getServer(), setup.getPort());
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * Any connections that the controller has will be closed by this method.
	 */
	private void closeLooseConnections() {
		try {
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
			if (server != null && !server.isClosed()) {
				server.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}