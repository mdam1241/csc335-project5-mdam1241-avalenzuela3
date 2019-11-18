
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * A custom stage acting as a dialog box for network setup.
 * @author Michael Dam, Aaron Valenzuela
 */
public class NetworkSetupDialogBox extends Stage {

	private boolean confirmSettings; // whether or not user clicked "OK" or "Cancel"
	private boolean createServer; // false = createClient
	private boolean playAsHuman; // false = playAsComputer
	private String server;
	private int port;
	
	/**
	 * Constructs the scene, and sets the scene for this stage.
	 * If something doesn't look right, modify here.
	 */
	public NetworkSetupDialogBox() {
		this.confirmSettings = false;
		this.createServer = true;
		this.playAsHuman = true;
		this.server = "localhost";
		this.port = 4000;
		this.initModality(Modality.APPLICATION_MODAL);
		
		HBox box0 = new HBox(10);
		HBox box1 = new HBox(10);
		HBox box2 = new HBox(10);
		HBox box3 = new HBox(10);
		
		Label  create = new Label("Create:");
		Label  play   = new Label("Play as:");
		Label serverLabel = new Label("Server");
		Label portLabel = new Label("Port");
		RadioButton serverButton = new RadioButton("Server");
		RadioButton clientButton = new RadioButton("Client");
		serverButton.setSelected(true);
		serverButton.setOnAction((event) -> {
			clientButton.setSelected(false); // Stops user from arming both Server and Client buttons
		});
		clientButton.setOnAction((event) -> {
			serverButton.setSelected(false);
		});
		RadioButton humanButton = new RadioButton("Human");
		RadioButton computerButton = new RadioButton("Computer");
		humanButton.setSelected(true);
		humanButton.setOnAction((event) -> {
			computerButton.setSelected(false); // Stops user from arming both Human and Computer buttons
		});
		computerButton.setOnAction((event) -> {
			humanButton.setSelected(false);
		});
		TextField serverText = new TextField("localhost");
		serverText.setPrefWidth(125);
		TextField portText = new TextField();
		portText.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		portText.setText("4000");
		portText.setPrefWidth(125);
		Button ok = new Button("OK");
		ok.setOnAction((event) -> {
			// User pressed OK, store all settings in this class.
			this.confirmSettings = true;
			this.createServer = serverButton.isSelected() && !clientButton.isSelected();
			this.playAsHuman = humanButton.isSelected() && !computerButton.isSelected();
			this.server = serverText.getText();
			this.port = Integer.parseInt(portText.getText());
			// Now, set all values to default.
			defaultSetup(serverButton, clientButton, humanButton, computerButton,
							serverText, portText);
			this.close();
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction((event) -> {
			// User pressed cancel. Set all values to default.
			this.confirmSettings = false;
			this.createServer = true;
			this.playAsHuman = true;
			this.server = "localhost";
			this.port = 4000;
			defaultSetup(serverButton, clientButton, humanButton, computerButton,
							serverText, portText);
			this.close();
		});
		
		box0.getChildren().addAll(create,serverButton,clientButton);
		box1.getChildren().addAll(play,humanButton,computerButton);
		box2.getChildren().addAll(serverLabel,serverText,portLabel,portText);
		box3.getChildren().addAll(ok,cancel);
		
		GridPane pane = new GridPane();
		pane.addColumn(0, box0,box1,box2,box3);
		pane.setVgap(20);
		pane.setPadding(new Insets(16));
		
		Scene scene = new Scene(pane,375,175);
		this.setScene(scene);
		this.setTitle("Network Setup");
		// Have the caller use this.show() to show Network Setup window.
	}
	
	/**
	 * @return True means the user clicked "OK", false means "Cancel"
	 */
	public boolean isOk() {
		return confirmSettings;
	}
	/**
	 * @return True means "Server" is selected, false means "Client"
	 */
	public boolean isServer() {
		return createServer; // 
	}
	/**
	 * @return True means "Human" is selected, false means "Computer"
	 */
	public boolean isHuman() {
		return playAsHuman;
	}
	/**
	 * @return String that is inside server TextField
	 */
	public String getServer() {
		return server;
	}
	/**
	 * @return String that is inside port TextField
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Takes all the inputs of Network Setup and turns them to default values.
	 */
	private void defaultSetup(RadioButton serverButton, RadioButton clientButton,
								RadioButton humanButton, RadioButton computerButton,
								TextField serverText, TextField portText) {
		serverButton.setSelected(true);
		clientButton.setSelected(false);
		humanButton.setSelected(true);
		computerButton.setSelected(false);
		serverText.setText("localhost");
		portText.setText("4000");
	}
}