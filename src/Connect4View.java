import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4View extends Application implements Observer {
	public GridPane grid;
	public Connect4Model model;
	public Connect4Controller controller;

	/* There shouldn't be a reason to make a constructor that asks for other models/controllers
	 * since Connect4View is the first object to be initialized, which will then construct a new model/controller in init().
	public Connect4View(Connect4Model model, Connect4Controller controller) {
		this.controller = controller;
		this.model = model;
		this.model.addObserver(this);
	}
	*/
	@Override
	public void init() {
		grid = new GridPane();
		model = new Connect4Model();
		controller = new Connect4Controller(model, this);
	}

	@Override
	public void update(Observable observable, Object moveMsg) {
		Connect4MoveMessage playerMove = (Connect4MoveMessage) moveMsg;
		ObservableList<Node> slots = grid.getChildren();
		
		if (playerMove.getColor() == 0) {
			this.newGame(slots);
		}
		else {
			
		for (Node node : slots) {
			if (GridPane.getRowIndex(node) == playerMove.getRow() && GridPane.getColumnIndex(node) == playerMove.getColumn()) {
				// below statement is for testing correct spot chosen after user clicks a column
				System.out.println("CIRCLE AT:" + playerMove.getRow() + ": " + playerMove.getColumn());
				Circle currentSpot = (Circle) ((VBox) node).getChildren().get(0);
				if (playerMove.getColor() == Connect4MoveMessage.YELLOW)
					currentSpot.setFill(javafx.scene.paint.Color.YELLOW);
				else
					currentSpot.setFill(javafx.scene.paint.Color.RED);
				break;
			}
		}
		}
		
		
	}
	
	
	/**
	 * Iterates through all the children of the GridPane that are Circle objects and changes the color of them to white 
	 * to indicate a new game
	 *
	 * @param slots ObservableList of Nodes that represent the circle slots in the board.
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

	private void setBoardConstraintsAndColor() {
		grid.setVgap(8);
		grid.setHgap(8);
		grid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.setPadding(new Insets(10));
	}

	private void addOnClickEventToGrid() {
		// adding EventHandler to the entire grid
		grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double xCoord = event.getX();
				int column = calculateColumnClicked(xCoord);
				
				// Must now write code to send column # to Controller.
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
			col++;
		}
	}

	private MenuBar createMenuBar(Stage stage) {
		Menu menu = new Menu("File");
		MenuItem menuItem = new MenuItem("New Game");

		MenuBar menuBar = new MenuBar();

		menuBar.prefWidthProperty().bind(stage.widthProperty());

		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItem);
		return menuBar;
	}

}