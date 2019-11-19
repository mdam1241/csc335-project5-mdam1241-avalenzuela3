import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;

/**
 * @author Michael Dam, Aaron Valenzuela
 * Takes input from Connect4View GUI and controls a Connect4Model board.
 * The only public methods are:
 * humanTurn() - to be called by View when user clicks on GUI.
 * setupNetwork() - to be called by View when user closes Network dialog box.
 * and some getters so that view can determine when to register clicks from user.
 * 
 * When a network is set up, the controller hosting the server will go first as YELLOW.
 * After the host drops the token, the client will go second as RED.
 * Human controllers click on the GUI to drop a token into the board.
 * Computer controllers randomly choose a column to drop a token into.
 * After every turn, checks the board if there is a victory.
 * Closes networks, input, and tells view to open a window when a victory is achieved
 * until a new network is made.
 */

public class Connect4Controller {

	public Connect4Model model;
	public Connect4View view;
	
	// Networking
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Connect4MoveMessage msg;
	
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
		player = 1;
		human = true;
		myTurn = false;
	}

	/**
	 * Calls the model's initializeBoard() to clear all the slots
	 */
	private void newGame() {
		model.initializeBoard();
	}
	/**
	 * @return If the user controlling this is human.
	 */
	public boolean isHuman() {
		return human;
	}
	/**
	 * @return If it is this controller's turn to drop a token.
	 */
	public boolean isMyTurn() {
		return myTurn;
	}
	
	/**
	 * Checks if it is the controller's turn.
	 * If it is a computer, calls computerTurn();
	 * Otherwise, waits for GUI to call humanTurn();
	 */
	private void checkTurn() {
		if (myTurn && !human) {
			computerTurn();
		}
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
	private void computerTurn() {
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
		Runnable their = new Runnable() {
			public void run() {
				try {
					msg = (Connect4MoveMessage) input.readObject();
					Platform.runLater(new Runnable() {
						public void run() {
							model.dropToken(msg.getColor(), msg.getColumn());
							myTurn = true;
							checkTurn();
						}
					});
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(their).start();
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
			myTurn = false;
			return true;
		} else if (winner > 0) {
			// Optional: view.displayLoser();
			closeLooseConnections();
			myTurn = false;
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
	/**
	 * This method should be called when user wants to set up a server.
	 * @param setup A custom stage that contains fields for user-given network settings.
	 */
	private void setupServer(NetworkSetupDialogBox setup) {
		Runnable task = new Runnable() {
			public void run() {
				try {
					server = new ServerSocket(setup.getPort());
					connection = server.accept();
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								output = new ObjectOutputStream(connection.getOutputStream());
								input = new ObjectInputStream(connection.getInputStream());
								player = 1;
								human = setup.isHuman();
								myTurn = true;
								checkTurn();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(task).start();
	}
	/**
	 * This method should be called when user wants to set up a client.
	 * @param setup A custom stage that contains fields for user-given network settings.
	 */
	private void setupClient(NetworkSetupDialogBox setup) {
		Runnable task = new Runnable() {
			public void run() {
				try {
					connection = new Socket(setup.getServer(), setup.getPort());
					Platform.runLater(new Runnable() {
						public void run() {
							try {
								output = new ObjectOutputStream(connection.getOutputStream());
								input = new ObjectInputStream(connection.getInputStream());
							} catch (IOException e) {
								e.printStackTrace();
							}
							player = 2;
							human = setup.isHuman();
							myTurn = false;
							theirTurn();
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		};
		new Thread(task).start();
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
