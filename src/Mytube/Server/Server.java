package Mytube.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket server;
	
	
	public Server(){
		ServerSocket server;
		try {
			server = new ServerSocket(8080);
			while(true){
				Socket client = server.accept();
				new ServerManager(client);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server();
	}

}
