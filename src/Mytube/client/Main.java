package Mytube.client;

import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class Main extends Application {
private TreeViewController controller;
	
	public static void main(String[] args) {
		try {
			Socket socket=new Socket("localhost", 18080);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			final Image img16 = new Image(getClass().getResourceAsStream("treeviewsample16.jpg"));
			
			final Image img32 = new Image(getClass().getResourceAsStream("treeviewsample32.jpg"));
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml001.fxml"));
			Pane root =loader.load();
			this.controller =new TreeViewController((TreeView<TreeItemData>)root.lookup("#treeview"));
			UIController controller = loader.getController();
			controller.setPrimaryStage(primaryStage);
			
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("MyTube");
			primaryStage.getIcons().addAll(img16, img32);
			primaryStage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
