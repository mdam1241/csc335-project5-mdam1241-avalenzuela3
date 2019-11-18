
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A custom stage acting as a dialog box for network setup.
 * @author Michael Dam, Aaron Valenzuela
 */
public class NetworkSetupDialogBox extends Stage {

	private boolean confirmSettings; // whether or not user clicked "OK" or "Cancel"
	private boolean createServer; // false = createClient
	private boolean playAsHuman; // false = playAsComputer
	private String server;
	private String port;
	
	/**
	 * Constructs the scene, and sets the scene for this stage.
	 * If something doesn't look right, modify here.
	 */
	public NetworkSetupDialogBox() {
		this.initModality(Modality.APPLICATION_MODAL);
		HBox box0 = new HBox();
		HBox box1 = new HBox();
		HBox box2 = new HBox();
		HBox box3 = new HBox();
		
		Label  create = new Label("Create:");
		Label  play   = new Label("Play as:");
		Label serverLabel = new Label("Server");
		Label portLabel = new Label("Port");
		RadioButton serverButton = new RadioButton("Server");
		RadioButton clientButton = new RadioButton("Client");
		serverButton.setOnAction((event) -> {
			clientButton.disarm(); // Stops user from arming both Server and Client buttons
		});
		clientButton.setOnAction((event) -> {
			serverButton.disarm();
		});
		RadioButton humanButton = new RadioButton("Human");
		RadioButton computerButton = new RadioButton("Computer");
		humanButton.setOnAction((event) -> {
			computerButton.disarm(); // Stops user from arming both Human and Computer buttons
		});
		computerButton.setOnAction((event) -> {
			humanButton.disarm();
		});
		TextField serverText = new TextField("localhost");
		serverText.setPrefWidth(100); // Change this if it doesn't look right.
		TextField portText = new TextField("4000");
		portText.setPrefWidth(100); // Change this if it doesn't look right.
		Button ok = new Button("OK");
		ok.setOnAction((event) -> {
			// User pressed OK, store all settings in this class.
			this.confirmSettings = true;
			this.createServer = serverButton.isArmed() && !clientButton.isArmed();
			this.playAsHuman = humanButton.isArmed() && !computerButton.isArmed();
			this.server = serverText.getText();
			this.port = portText.getText();
			// Now, set all values to default.
			serverButton.arm();
			clientButton.disarm();
			humanButton.arm();
			computerButton.disarm();
			serverText.setText("localhost");
			portText.setText("4000");
			this.close();
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction((event) -> {
			// User pressed cancel. Set all values to default.
			serverButton.arm();
			clientButton.disarm();
			humanButton.arm();
			computerButton.disarm();
			serverText.setText("localhost");
			portText.setText("4000");
			this.close();
		});
		
		box0.getChildren().addAll(create,serverButton,clientButton);
		box1.getChildren().addAll(play,humanButton,computerButton);
		box2.getChildren().addAll(serverLabel,serverText,portLabel,portText);
		box3.getChildren().addAll(ok,cancel);
		
		GridPane pane = new GridPane();
		pane.addColumn(0, box0, box1, box2, box3);
		
		Scene scene = new Scene(pane,350,400);
		this.setScene(scene);
		this.setTitle("Network Setup");
		// Have the caller use this.show() to show Network Setup window.
	}
	
	public boolean isArmed() {
		return confirmSettings;
	}
	public boolean isServer() {
		return createServer;
	}
	public boolean isHuman() {
		return playAsHuman;
	}
	public String getServer() {
		return server;
	}
	public String getPort() {
		return port;
	}
}