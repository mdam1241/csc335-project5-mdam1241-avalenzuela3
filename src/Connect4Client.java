import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.Scanner;
public class Connect4Client {

	public static void main(String[] args) {
		try {
			Socket server = new Socket("localhost", 4000);
			ObjectOutputStream output = new
				ObjectOutputStream(server.getOutputStream());
			ObjectInputStream input = new 
				ObjectInputStream(server.getInputStream());
			
			
			// Do some IO with the server
			
			server.close();     
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}

