import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NetworkSetupDialogBox extends Stage{

	/**
	 * Attempting to figure out the dialog box for this class. The code below
	 * is NOT working.
	 */
	public static void main(String[] args) {
		GridPane grid = new GridPane();
		Dialog dialog = new Dialog();
		Label  create = new Label("Create:");
		Label  play   = new Label("Play as:");
		RadioButton server = new RadioButton("Server");
		RadioButton client = new RadioButton("Client");
		RadioButton human = new RadioButton("Human");
		RadioButton computer = new RadioButton("Computer");
		
		dialog.getDialogPane().getChildren().addAll(create, play, server, client, human, computer);
		
		dialog.initModality(Modality.APPLICATION_MODAL);
		
	}
}
