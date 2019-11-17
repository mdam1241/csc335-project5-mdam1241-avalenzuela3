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
	GridPane grid = new GridPane();

	@Override
	public void update(Observable observable, Object moveMsg) {
        Connect4MoveMessage playerMove = (Connect4MoveMessage) moveMsg;
        ObservableList<Node> spots = grid.getChildren();
        int r = 0;
        int c = 0;
        for (Node node : spots) {
            if(grid.getRowIndex(node) == playerMove.getRow() && grid.getColumnIndex(node) == playerMove.getColumn())
            {
            	// below statement is for testing correct spot clicked
            	System.out.println("CIRCLE AT:" + playerMove.getRow() + ": " + playerMove.getColumn());
                break;
            }
        
	}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Connect 4");

		Menu menu = new Menu("File");
		MenuItem menuItem = new MenuItem("New Game");

		MenuBar menuBar = new MenuBar();

		menuBar.prefWidthProperty().bind(stage.widthProperty());

		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItem);

		VBox menuBox = new VBox(menuBar);

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

		grid.setVgap(8);
		grid.setHgap(8);

		grid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.setPadding(new Insets(10));

		// adding EventHandler to the entire grid
		grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double xCoord = event.getX();
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
		
				
				// Must now write code to send column # to Controller.
			}
		});
		
		// setting menu bar and connect4 board
		BorderPane gameBoard = new BorderPane();
		gameBoard.setCenter(grid);
		gameBoard.setTop(menuBox);
		
		Scene scene = new Scene(gameBoard, 350, 340);
		stage.setScene(scene);
		
		// show the running app:
		stage.show();
	}
}