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
					resultCmd.setResult(dao.insertTube(cmd.getCategory(), cmd.getTitle(), cmd.getUrl(), cmd.getUser()));
					resultCmd.setCategory(cmd.getCategory());
					resultCmd.setTitle(cmd.getTitle());
					sendData(resultCmd);
					break;
				case Command.FIND:
					resultCmd = new Command(Command.FIND);
					al = dao.searchTube(cmd.getContents(), cmd.getUser());
					System.out.println("파인드2"+al);
					resultCmd.setAlist(al);
					sendData(resultCmd);
					break;
				case Command.DELETE:
					resultCmd = new Command(Command.DELETE);
					resultCmd.setResult(dao.deleteTube(cmd.getTitle(), cmd.getUser()));
					resultCmd.setTitle(cmd.getTitle());
					sendData(resultCmd);
					break;
				case Command.SHOWALLTUBE:
					resultCmd = new Command(Command.SHOWALLTUBE);
					resultCmd.setAlist(dao.showAllTube(cmd.getUser()));
					sendData(resultCmd);
					break;
					//영상띄우기
				case Command.SHOWTUBE:
					resultCmd = new Command(Command.SHOWTUBE);
					resultCmd.setUrl(dao.showTube(cmd.getTitle(), cmd.getUser()));
					sendData(resultCmd);
					break;
				
				case Command.JOIN:
					resultCmd =new Command(Command.JOIN);
					resultCmd.setResult(dao.join(cmd.getUser()));
					sendData(resultCmd);
					break;
					
				case Command.LOGIN:
					System.out.println("서버로그인");
					resultCmd=new Command(Command.LOGIN);
					resultCmd.setUser(cmd.getUser());
					resultCmd.setResult(dao.login(cmd.getUser()));
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
