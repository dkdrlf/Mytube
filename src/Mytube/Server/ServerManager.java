package Mytube.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Mytube.command.Command;

public class ServerManager implements Runnable{
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Command resultCmd = new Command();
	public ServerManager(Socket client){
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			serverStart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		try {
			Command cmd = (Command)ois.readObject();			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void serverStart(){
		Thread t1 = new Thread(this);
		t1.start();
	}
	public void sendData(){
		try {
			oos.writeObject(resultCmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
