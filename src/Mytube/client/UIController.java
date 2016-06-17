package Mytube.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import Mytube.command.Command;
import Mytube.vo.myLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.java2d.cmm.CMSManager;

public class UIController implements Initializable, Runnable{

	private Stage primaryStage;	
	
	ComboBox<String> category;
	TextField title;
	TextField url;
	@FXML Button btn_store;
	@FXML Button btn_delete;
	Button btn_insert;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	TreeViewController treecontrol;
	Parent parent=null;
	Stage dialog;
	public TreeViewController getTreecontrol() {
		return treecontrol;
	}
	public void setTreecontrol(TreeViewController treecontrol) {
		this.treecontrol = treecontrol;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	public void store(ActionEvent e) throws IOException
	{
		
		dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);
		dialog.setTitle("Store");
		
	
			
		parent = FXMLLoader.load(getClass().getResource("store.fxml"));
		btn_insert=(Button)parent.lookup("#btn_insert");
		btn_insert.setOnAction(event->insert(event));
		title=(TextField)parent.lookup("#title");
		url=(TextField)parent.lookup("#url");
		category=(ComboBox<String>)parent.lookup("#category");
		

		ObservableList<String> list = FXCollections.observableArrayList("교육", "K-POP", "여행");
		ComboBox<String> combo=(ComboBox<String>)parent.lookup("#category");
		combo.setItems(list);

		Scene scene = new Scene(parent);
		
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		
	}
	public void delete(ActionEvent e)
	{
		System.out.println("델리트");
	
	}
	public void insert(ActionEvent e)
	{
		String t=title.getText();
		String u=url.getText();
		String c=this.category.getValue();
		int category=0;
		if(c.equals("교육"))
		{
			category=myLibrary.EDUCATION;
		}
		else if(c.equals("K-POP"))
		{
			category=myLibrary.KPOP;
		}
		else if(c.equals("여행"))
		{
			category=myLibrary.TRAVEL;
		}
		Command command=new Command(Command.SAVE);
		command.setCategory(category);
		command.setTitle(t);
		command.setUrl(u);
		sendData(command);
		
	}
	public void cancel(ActionEvent ee)
	{
		System.out.println("cancel");
	}
	public void ThreadStart(){
		Thread t1 = new Thread(this);
		t1.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Command cmd=(Command)ois.readObject();
				switch(cmd.getSatatus())
				{
					case Command.SAVE:
					{
						if(cmd.getCategory()==myLibrary.EDUCATION)
						{
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							treecontrol.getNode0().getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.KPOP)
						{
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							treecontrol.getNode1().getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.TRAVEL)
						{
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							treecontrol.getNode2().getChildren().add(ti);
						}
						dialog.close();
						break;
					}
					case Command.FIND:
					{
						break;
					}
					case Command.DELETE:
					{
						break;
					}
					
				}
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	public void sendData(Command result){
		try {
			oos.writeObject(result);
			oos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
