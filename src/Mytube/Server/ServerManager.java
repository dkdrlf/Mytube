package Mytube.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Mytube.command.Command;
import Mytube.database.database;
import Mytube.vo.myLibrary;

public class ServerManager implements Runnable {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private database dao;
	Command resultCmd;

	public ServerManager(Socket client) {
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
		while (true) {
			try {
				ArrayList<myLibrary> al;
				System.out.println("세이브 들어옴1");
				Command cmd = (Command) ois.readObject();
				System.out.println("세이브 들어옴2");
				switch (cmd.getSatatus()) {
				case Command.SAVE:
					System.out.println("세이브 들어옴3");
					resultCmd = new Command(Command.SAVE);
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
				case Command.DELETE:
					resultCmd = new Command(Command.DELETE);
					resultCmd.setResult(dao.deleteTube(cmd.getTitle()));
					resultCmd.setTitle(cmd.getTitle());
					sendData(resultCmd);
					break;
				case Command.SHOWALLTUBE:
					resultCmd = new Command(Command.SHOWALLTUBE);
					resultCmd.setAlist(dao.showAllTube());
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
	}

	public void serverStart() {
		Thread t1 = new Thread(this);
		t1.start();
	}

	public void sendData(Command resultCmd) {
		try {
			oos.writeObject(resultCmd);
			oos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
