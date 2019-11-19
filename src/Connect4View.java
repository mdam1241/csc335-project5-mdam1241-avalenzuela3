import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class represents the view of the Connect4 board. It uses a GridPane to
 * display the board with Circle objects contained in VBoxes to display each
 * slot of the game board. The entire GridPane has an onClickEvent handler that
 * sends to the controller what column has been clicked in order to drop a token
 * in it and sends the controller the information. The View receives the message
 * from the model that was just changed and updates the View to add a token to
 * that column, or it displays an error message if the column is full in its
 * update function.
 * 
 * 
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4View extends Application implements Observer {
	private GridPane grid;
	private Connect4Model model;
	private Connect4Controller controller;
	private NetworkSetupDialogBox setup;

	@Override
	public void init() {
		grid = new GridPane();
		model = new Connect4Model();
		model.addObserver(this);
		controller = new Connect4Controller(model, this);
	}
	
	/**
	 * For debugging and unit-testing purposes.
	 * @return The controller that this view uses.
	 */
	public Connect4Controller debugGetController() {
		return controller;
	}

	/**
	 * This method is called when a change has been made in the Model in cases where
	 * a player makes a move, a winner has been determined, or a new game has been
	 * requested to start. It iterates through an enhanced for loop of the
	 * GridPane's children that represent the Connect 4 board in order to change the
	 * correct slot.
	 */
	@Override
	public void update(Observable observable, Object moveMsg) {
		Connect4MoveMessage playerMove = (Connect4MoveMessage) moveMsg;
		ObservableList<Node> slots = grid.getChildren();

		if (playerMove.getColor() == 0) { // New game requested
			this.newGame(slots);
		} else {
			for (Node node : slots) {
				if (GridPane.getRowIndex(node) == playerMove.getRow() + 1
						&& GridPane.getColumnIndex(node) == playerMove.getColumn()) {
					// below statement is for testing correct spot chosen after user clicks a column
					// System.out.println("CIRCLE AT:" + playerMove.getRow() + ": " + playerMove.getColumn());

					Circle currentSpot = (Circle) ((VBox) node).getChildren().get(0); // Current Circle Object
					if (playerMove.getColor() == Connect4MoveMessage.YELLOW) {
						currentSpot.setFill(javafx.scene.paint.Color.YELLOW);
						break;
					} else {
						currentSpot.setFill(javafx.scene.paint.Color.RED);
						break;
					}
				}
      }
		}
		}
	}
	
	/**
	 * Displays an Alert box that notifies the user the column they chose is full.
	 * Must be called by the controller.
	 */
	public void fullColumn() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("Column full, pick somewhere else!");
		alert.showAndWait();
	}

	/**
	 * Displays an Alert box that notifies the user they've won. Still needs code to
	 * determine who is the winner.
	 * 
	 * @param winnerNum integer representing the winner.
	 * @param color
	 */

	public void displayWinner() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("Message");
		alert.setContentText("You won!");
		alert.showAndWait();
	}

	/**
	 * Iterates through all the children of the GridPane that are Circle objects and
	 * changes the color of them to white to indicate a new game
	 *
	 * @param slots ObservableList of Nodes that represent the circle slots in the
	 *              board.
	 */
	private void newGame(ObservableList<Node> slots) {
		for (Node node : slots) {
			Circle currentSpot = (Circle) ((VBox) node).getChildren().get(0);
			currentSpot.setFill(javafx.scene.paint.Color.WHITE);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Connect 4");
		MenuBar menuBar = createMenuBar(stage);
		VBox menuBox = new VBox(menuBar);

		createConnect4Slots();
		setBoardConstraintsAndColor();
		addOnClickEventToGrid();

		// setting menu bar and connect4 board
		BorderPane gameBoard = new BorderPane();
		gameBoard.setCenter(grid);
		gameBoard.setTop(menuBox);

		Scene scene = new Scene(gameBoard, 350, 340);
		stage.setScene(scene);
		// show the running app:
		stage.show();
	}

	/**
	 * Sets the constraints on the Connect 4 game board
	 */
	private void setBoardConstraintsAndColor() {
		grid.setVgap(8);
		grid.setHgap(8);
		grid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.setPadding(new Insets(10));
	}

	/**
	 * Adds an onClickEvent to the entire grid and calculates what column and row
	 * the user just clicked on. Sends a message to the Controller using a
	 * Connect4MoveMessage object to indicate a player has made a move.
	 */
	private void addOnClickEventToGrid() {
		// adding EventHandler to the entire grid
		grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double xCoord = event.getX();
				int column = calculateColumnClicked(xCoord);
				if (model.getBoard()[0][column] != 0) {
					fullColumn();
				} else if (controller.isHuman() && controller.isMyTurn()) {
					controller.humanTurn(column);
				}
			}

			/**
			 * Calculates the column number that was clicked by the user and returns it.
			 * 
			 * @param xCoord x-coordinate where the user clicked
			 * @return column number that was clicked
			 */
			private int calculateColumnClicked(double xCoord) {
				int column;
				double firstColumn = 52.0;
				if (xCoord < firstColumn)
					column = 0;
				else if (xCoord < firstColumn + 50)
					column = 1;
				else if (xCoord < firstColumn + 100)
					column = 2;
				else if (xCoord < firstColumn + 150)
					column = 3;
				else if (xCoord < firstColumn + 200)
					column = 4;
				else if (xCoord < firstColumn + 250)
					column = 5;
				else
					column = 6;
				return column;
			}
		});
	}

	/**
	 * Creates the circles that represent the slots on the Connect4 game board. Uses
	 * a loop to create a 7 X 6 board of available slots. Each slot is represented
	 * by a VBox containing a Circle object.
	 * 
	 */
	private void createConnect4Slots() {
		int row = 0;
		int col = 0;
		for (int i = 0; i < 42; i++) {
			if (i % 7 == 0) {
				row++;
				col = 0;
			}

			Circle circle = new Circle(20, Color.WHITE);
			VBox box = new VBox(circle);

			box.setCenterShape(true);
			grid.add(box, col, row);
			GridPane.setRowIndex(box, row);
			GridPane.setColumnIndex(box, col);
			col++;
		}
	}

	/**
	 * Creates the menu bar at the top of the window. It contains a File Menu with a
	 * New Game MenuItem.
	 * 
	 * @param stage used to present the board
	 * @return a MenuBar item containing the correct Menu and MenuItem
	 */
	private MenuBar createMenuBar(Stage stage) {
		Menu menu = new Menu("File");
		MenuItem menuItem = new MenuItem("New Game");
		setup = new NetworkSetupDialogBox();
		menuItem.setOnAction((event) -> {
			setup.show();
		});
		setup.setOnHidden((event)-> {

			controller.setupNetwork(setup);
		});
		MenuBar menuBar = new MenuBar();

		menuBar.prefWidthProperty().bind(stage.widthProperty());
		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItem);
		return menuBar;
	}

}