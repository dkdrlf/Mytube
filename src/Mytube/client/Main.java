package Mytube.client;

import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class Main extends Application {
	private TreeViewController controller;

	public static void main(String[] args) {
		try {
			Socket socket=new Socket("localhost", 8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) {
		try {
			final Image img16 = new Image(getClass().getResourceAsStream("treeviewsample16.jpg"));
			final Image img32 = new Image(getClass().getResourceAsStream("treeviewsample32.jpg"));
			
			//GUI
			final Pane root = (Pane)FXMLLoader.load(this.getClass().getResource("fxml001.fxml"));
			this.controller =new TreeViewController((TreeView<TreeItemData>)root.lookup("#treeview"));
			stage.setScene(new Scene(root));
			stage.setTitle("MyTube");
			stage.getIcons().addAll(img16, img32);
			stage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
