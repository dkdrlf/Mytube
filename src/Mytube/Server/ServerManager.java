package Mytube.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Mytube.command.Command;
import Mytube.database.database;
import Mytube.vo.myLibrary;

public class ServerManager implements Runnable{
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Command resultCmd;
	private database dao;
	public ServerManager(Socket client){
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			dao = new database();
			serverStart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		try {
			ArrayList<myLibrary> al;
			Command cmd = (Command)ois.readObject();			
			switch(cmd.getSatatus()){
				case Command.SAVE:
					resultCmd = new Command(Command.SAVE);
					oos.writeObject(cmd);
					oos.reset();
					
					resultCmd.setResult(dao.insertTube(cmd.getCategory(), cmd.getTitle(), cmd.getUrl()));
					resultCmd.setCategory(cmd.getCategory());
					resultCmd.setTitle(cmd.getTitle());
					sendData(resultCmd);
					break;
				case Command.FIND:
					resultCmd = new Command(Command.FIND);
					al = dao.searchTube(cmd.getContents());
					resultCmd.setAlist(al);
					sendData(resultCmd);
					break;
				case Command.DELETE :
					resultCmd = new Command(Command.DELETE);
					resultCmd.setResult(dao.deleteTube(cmd.getTitle()));
					sendData(resultCmd);
					break;
					
			}
			
			
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
	public void sendData(Command resultCmd){
		try {
			oos.writeObject(resultCmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
