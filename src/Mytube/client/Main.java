package Mytube.client;

import java.io.IOException;
import java.net.Socket;

import Mytube.vo.myLibrary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

	public Main() {
		// TODO Auto-generated constructor stub
		try {
			socket=new Socket("localhost", 18080);
			
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
			controller.setPrimaryStage(primaryStage);
			controller.setSocket(socket);
			controller.setTreecontrol(treecontrol);
			WebView web=(WebView) root.lookup("#web");
			web.getEngine().load("https://www.youtube.com/");
			controller.setWeb(web);
			
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("MyTube");
			primaryStage.getIcons().addAll(mytube);
			primaryStage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public class LoginUI
	{
		
	}
}
