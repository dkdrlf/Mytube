package Mytube.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIController implements Initializable {

	private Stage primaryStage;	
	
	ComboBox<String> category;
	TextField title;
	TextField url;
	@FXML Button btn_store;
	@FXML Button btn_delete;
	Button btn_insert;
	
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
		
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(primaryStage);
		dialog.setTitle("Store");
		
		Parent parent=null;
			
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
		System.out.println("인설트들어옴");
		String t=title.getText();
		String u=url.getText();
		String c=category.getValue();
		System.out.println("CATEGORY:"+c+"\n"+"TITLE:"+t+"\n"+"URL:"+u);
	}
	public void cancel(ActionEvent ee)
	{
		System.out.println("cancel");
	}
	

}
