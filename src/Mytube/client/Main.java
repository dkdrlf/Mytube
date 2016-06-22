package Mytube.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Mytube.command.Command;
import Mytube.vo.User;
import Mytube.vo.myLibrary;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public final class Main extends Application {
private TreeViewController treecontrol;
private WebView web;
Socket socket;
TextField tf_id;
TextField tf_password;
Pane root;
ObjectOutputStream oos;
ObjectInputStream ois;
UIController controller;
	public Main() {
		// TODO Auto-generated constructor stub
		try {
			socket=new Socket("localhost", 18080);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			
			//final Image mytube = new Image(getClass().getResourceAsStream("mytb.png"));
			final Image mytube = new Image(getClass().getResourceAsStream("mytb.png"));
			//final Image img32 = new Image(getClass().getResourceAsStream("treeviewsample32.jpg"));
			final Image showall= new Image(getClass().getResourceAsStream("showall.PNG"));
			final Image mytubeLogo=new Image(getClass().getResourceAsStream("mytube_logo.jpg"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml001.fxml"));
			Pane root =loader.load();
			
			this.treecontrol =new TreeViewController((TreeView<String>)root.lookup("#treeview"));
			ImageView logo=(ImageView)root.lookup("#mytube_logo");
			logo.setImage(mytubeLogo);
			ImageView iv=(ImageView) root.lookup("#image_showall");
			iv.setImage(showall);
			UIController controller = loader.getController();
			controller.setPane(root);
			controller.setPrimaryStage(primaryStage);
			controller.setSocket(oos,ois);
			controller.setTreecontrol(treecontrol);
			WebView web=(WebView) root.lookup("#web");
			web.getEngine().load("https://www.youtube.com/");
			//System.out.println(web.getEngine().getLocation());
			controller.setWeb(web);
			mytubeStart(primaryStage);
			/*
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("MyTube");
			primaryStage.getIcons().addAll(mytube);
			primaryStage.show();
			*/
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void mytubeStart(Stage primaryStage)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_UI.fxml"));
		try {
			root =loader.load();
			Button join=(Button)root.lookup("#btn_join");
			Button login=(Button)root.lookup("#btn_login");
			login.setOnAction(event->login(event));
			join.setOnAction(event->join(event));                                                                                                                        
			
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("MyTube");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void join(ActionEvent event)
	{
		
		tf_id=(TextField) root.lookup("#tf_id");
		tf_password=(TextField) root.lookup("#tf_password");
		String id=tf_id.getText();
		String password=tf_password.getText();
		User u=new User(id,password);
		Command c=new Command(Command.JOIN);
		c.setUser(u);
		try {
			oos.writeObject(c);
			oos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void login(ActionEvent event)
	{
		tf_id=(TextField) root.lookup("#tf_id");
		tf_password=(TextField) root.lookup("#tf_password");
		String id=tf_id.getText();
		String password=tf_password.getText();
		User u=new User(id,password);
		Command c=new Command(Command.LOGIN);
		c.setUser(u);
		try {
			oos.writeObject(c);
			oos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
