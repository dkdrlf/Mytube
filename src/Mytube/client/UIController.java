package Mytube.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import Mytube.command.Command;
import Mytube.vo.myLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
	@FXML TextField tf_search;
	@FXML Button btx_exit;
	Button btn_insert;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	TreeViewController treecontrol;
	Parent parent=null;
	Stage dialog;
	TreeItem<String> node0;
	TreeItem<String> node1;
	TreeItem<String> node2;
	TreeView<String > t;
	Popup popup;
	WebView web;
	
	public void setWeb(WebView web) {
		this.web = web;
	}
	public TreeViewController getTreecontrol() {
		return treecontrol;
	}
	public void setTreecontrol(TreeViewController treecontrol) {
		this.treecontrol = treecontrol;
		this.node0=this.treecontrol.getNode0();
		this.node1=this.treecontrol.getNode1();
		this.node2=this.treecontrol.getNode2();
		t=treecontrol.getTreeview();
		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
		    handleMouseClicked(event);
		};
 
		t.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
		
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			ThreadStart();
			Command c=new Command(Command.SHOWALLTUBE);
			sendData(c);
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
	public void imageShowall(ActionEvent e)
	{
		treecontrol.getRootNode().getChildren().clear();
		
		node0 = new TreeItem<String>("교육"); 
		node1 = new TreeItem<String>("K-POP");
		node2 = new TreeItem<String>("여행");
		treecontrol.getRootNode().getChildren().add(node0);
		treecontrol.getRootNode().getChildren().add(node1);
		treecontrol.getRootNode().getChildren().add(node2);
		
		Command c=new Command(Command.SHOWALLTUBE);
		sendData(c);
	}
	
	//트리마우스
	private void handleMouseClicked(MouseEvent event) {
	  
		if(event.getClickCount()==2){ //더블클릭 처리 !!
		    Node node = event.getPickResult().getIntersectedNode();
		    // Accept clicks only on node cells, and not on empty spaces of the TreeView
		    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
		    	if((String) ((TreeItem)t.getSelectionModel().getSelectedItem()).getValue()!="교육" &&
		    			(String)((TreeItem)t.getSelectionModel().getSelectedItem()).getValue()!="K-POP" &&
		    					(String)((TreeItem)t.getSelectionModel().getSelectedItem()).getValue()!="여행"){
		        String address = (String) ((TreeItem)t.getSelectionModel().getSelectedItem()).getValue();
		        //setfile(address);
		       mytube();
		        //System.out.println("Node click: " + name);
		     //   Command c = new Command(Command.SHOWTUBE);
		      //  c.setTitle(name);
		       // sendData(c);
		    	}
		    }
		}
	}
	public void mytube()
	{
			System.out.println("마이튜브");
			web.getEngine().load("file:///d:/temp.html");
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
		popup.hide();
		String s=t.getSelectionModel().getSelectedItem().getValue();
		Command c=new Command(Command.DELETE);
		c.setTitle(s);
		try {
			oos.writeObject(c);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void delete_dialog(ActionEvent e)
	{
		popup=new Popup();
		try {
			parent = FXMLLoader.load(getClass().getResource("delete_dialog.fxml"));
			ImageView imageView = (ImageView) parent.lookup("#imgMessage");
			imageView.setImage(new Image(getClass().getResource("dialog-warning.png").toString()));
			Button yesButton=(Button)parent.lookup("#yes");
			yesButton.setOnAction(event->delete(event));
			Button noButton=(Button)parent.lookup("#no");
			noButton.setOnAction(event->popup.hide());
			String s=t.getSelectionModel().getSelectedItem().getValue();
			Label message=(Label)parent.lookup("#delete_label");
			message.setText(s+"를 정말로 삭제하시겠습니까?");
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		popup.getContent().add(parent);
		popup.setAutoHide(true);	
		popup.show(primaryStage);
	}
	public void insert(ActionEvent e)
	{
		String t=title.getText();
		String u=url.getText();
		String c=this.category.getValue();
		System.out.println(c);
		int category=0;
		if(c.equals("교육"))
		{
			System.out.println("교육비교");
			category=myLibrary.EDUCATION;
		}
		else if(c.equals("K-POP"))
		{System.out.println("케이팝비교");
			category=myLibrary.KPOP;
		}
		else if(c.equals("여행"))
		{
			category=myLibrary.TRAVEL;
		}
		System.out.println(this.category.getItems());
		Command command=new Command(Command.SAVE);
		command.setCategory(category);
		command.setTitle(t);
		command.setUrl(u);
		sendData(command);
		dialog.close();
	}
	public void search(ActionEvent e){
		System.out.println("search:"+tf_search.getText());
		Command c=new Command(Command.FIND);
		c.setContents(tf_search.getText());
		try {
			oos.writeObject(c);
			oos.reset();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
				System.out.println(cmd.getCategory());
				switch(cmd.getSatatus())
				{
					case Command.SAVE:
					{
						if(cmd.getCategory()==myLibrary.EDUCATION)
						{
							System.out.println("education");
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							node0.getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.KPOP)
						{	System.out.println("kop");
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							node1.getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.TRAVEL)
						{	System.out.println("travel");
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							node2.getChildren().add(ti);
							
						}
						break;
					}
					case Command.FIND:
					{
						treecontrol.getRootNode().getChildren().clear();
						
						node0 = new TreeItem<String>("교육"); 
						node1 = new TreeItem<String>("K-POP");
						node2 = new TreeItem<String>("여행");
						treecontrol.getRootNode().getChildren().add(node0);
						treecontrol.getRootNode().getChildren().add(node1);
						treecontrol.getRootNode().getChildren().add(node2);
						ArrayList<myLibrary>al=new ArrayList<>();
						al=cmd.getAlist();
						for(myLibrary m:al)
						{
							if(m.getCategory()==myLibrary.EDUCATION)
							{
								System.out.println("education");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node0.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.KPOP)
							{	System.out.println("kop");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node1.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.TRAVEL)
							{	System.out.println("travel");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node2.getChildren().add(ti);
							}
						}
						break;
					}
					case Command.DELETE:
					{
						treecontrol.getRootNode().getChildren().clear();
						
						node0 = new TreeItem<String>("교육"); 
						node1 = new TreeItem<String>("K-POP");
						node2 = new TreeItem<String>("여행");
						treecontrol.getRootNode().getChildren().add(node0);
						treecontrol.getRootNode().getChildren().add(node1);
						treecontrol.getRootNode().getChildren().add(node2);
						
						Command c=new Command(Command.SHOWALLTUBE);
						oos.writeObject(c);
						oos.reset();
						
						break;
					}
					
					case Command.SHOWALLTUBE:
					{
						System.out.println("딜리트쇼올 드러옴");
						ArrayList<myLibrary>al=new ArrayList<>();
						al=cmd.getAlist();
						System.out.println(al.toString());
						for(myLibrary m:al)
						{
							if(m.getCategory()==myLibrary.EDUCATION)
							{
								System.out.println("education");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node0.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.KPOP)
							{	System.out.println("kop");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node1.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.TRAVEL)
							{	System.out.println("travel");
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node2.getChildren().add(ti);
							}
						}
						break;
					}
					case Command.SHOWTUBE:
					{
						String address = cmd.getUrl(); //url;
						setfile(address);
						mytube();
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
	  public void setfile(String address){
	    	File file = new File("C:/Users/user/workspace1");
	    	try {
				FileWriter fw = new FileWriter(file);
				String str = "<html><body><iframe width='560' height='315' src='"+address+"' frameborder='0' allowfullscreen></iframe></body></html>";
				System.out.println("파일셋");
				fw.write(str);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }

}
