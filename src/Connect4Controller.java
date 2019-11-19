import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;

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
	Connect4MoveMessage msg;
	
	// Player = 1 for YELLOW, 2 for RED. Host will be YELLOW while client will be RED.
	// Player 1, Yellow, the Host, will always go first.
	// Player 2, Red, the Client, will always go second.
	private int player;
	private boolean human; // False = Computer
	private boolean myTurn;

	// Constructor
	public Connect4Controller(Connect4Model model, Connect4View view) {
		this.model = model;
		this.view = view;
		player = 1; // TODO: If user clicks on screen before network setup, it will drop yellow tokens for testing.
		human = true;
	}

	/**
	 * Calls the model's initializeBoard() to clear all the slots
	 */
	private void newGame() {
		model.initializeBoard();
	}
	public boolean isHuman() {
		return human;
	}
	public boolean isMyTurn() {
		return myTurn;
	}
	/**
	 * This method is called by the View when a column has been clicked by the user. 
	 * The controller determines whether the column clicked is full or not and changes 
	 * the model if is not. If it is, it displays an Error message
	 * 
	 * @param col int column index that user clicked on.
	 */
	public void humanTurn(int col) {
		if (!checkWinner()) {
			model.dropToken(player, col);
			try { output.writeObject(new Connect4MoveMessage(0, col, player)); }
			catch (IOException e) { e.printStackTrace(); }
			if (!checkWinner()) {
				theirTurn();
			}
		}
	}

	/**
	 * This method should be called when it is this controller's turn and controller is set to Computer
	 */
	public void computerTurn() {
		if (!checkWinner()) { // Checks if there is a winner before doing a move
			boolean validMove = false;
			int randomCol = 0;
			while (!validMove) {
				randomCol = (int) Math.floor(Math.random() * model.getCol());
				if (model.getBoard()[0][randomCol] == 0) {
					validMove = true;
				}
			}
			model.dropToken(player,randomCol);
			try { output.writeObject(new Connect4MoveMessage(0, randomCol, player)); }
			catch (IOException e) { e.printStackTrace(); }
			if (!checkWinner()) { // Checks if there is a winner after doing a move.
				theirTurn();
			}
		}
	}
	/**
	 * This method should be called when it is the other controller's turn.
	 */
	private void theirTurn() {
		myTurn = false;
		theirTurn their = new theirTurn();
		Platform.runLater(their);
	}
	/**
	 * Checks if the model returns victory. Identifies whose victory, then tells view to alert player.
	 * Needs to be called when client receives move from other player, and when client does a move.
	 * @return true when there is a winner, false when there isn't.
	 */
	private boolean checkWinner() {
		int winner = 0;
		winner = model.checkVictory();
		if (winner == player) {
			view.displayWinner();
			closeLooseConnections();
			// TODO: Tell view to consume any mouse clicks on the board until new network is set up.
			return true;
		} else if (winner > 0) {
			// TODO: view.displayLoser();
			closeLooseConnections();
			// TODO: Tell view to consume any mouse clicks on the board until new network is set up.
			return true;
		}
		return false;
	}
	/**
	 * Called by View when the Network Setup dialog box is closed.
	 * @param setup A custom stage that contains fields for user-given network settings.
	 */
	public void setupNetwork(NetworkSetupDialogBox setup) {
		if (!setup.isOk()) {
			return;
		}
		newGame();
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
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			player = 1;
			human = setup.isHuman();
			myTurn = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void setupClient(NetworkSetupDialogBox setup) {
		try {
			connection = new Socket(setup.getServer(), setup.getPort());
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			player = 2;
			human = setup.isHuman();
			myTurn = false;
			theirTurn();
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
	private class theirTurn implements Runnable {
		public void run() {
			try {
				msg = (Connect4MoveMessage) input.readObject();
				model.dropToken(msg.getColor(), msg.getColumn());
				myTurn = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
