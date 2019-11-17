import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * @author Michael Dam, Aaron Valenzuela
 *
 */

public class Connect4View extends Application implements Observer {

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Connect 4");
		
				
		GridPane grid = new GridPane();
		Menu     menu = new Menu("File");
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
				row ++;
				col = 0;
			}
			
			Circle circle = new Circle(20, Color.WHITE);
			VBox box = new VBox(circle);
			
			box.setCenterShape(true);
			
	        box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					System.out.println("CLICKED" + event.getX());
				}
			});

			grid.add(box, col, row);
			col++;
	}
		
	grid.setVgap(8);
	grid.setHgap(8);
		
	grid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	grid.setPadding(new Insets(10));

	BorderPane border = new BorderPane();
	border.setCenter(grid);
	border.setTop(menuBox);
	Scene scene = new Scene(border, 370, 310); 
	stage.setScene(scene);
	// show the running app:
	stage.show();
}
}