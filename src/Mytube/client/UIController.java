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
import Mytube.vo.User;
import Mytube.vo.myLibrary;
import javafx.application.Platform;
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
import javafx.scene.layout.Pane;
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
	ObjectOutputStream oos;
	ObjectInputStream ois;
	TreeViewController treecontrol;
	Parent parent=null;
	Pane pane=null;
	Stage dialog;
	TreeItem<String> node0;
	TreeItem<String> node1;
	TreeItem<String> node2;
	TreeView<String > t;
	Popup popup;
	WebView web;
	User user;
	
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	public void setWeb(WebView web) {
		this.web = web;
	}
	public TreeViewController getTreecontrol() {
		return treecontrol;
	}
	public void setUser(User user)
	{
		this.user=user;
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
	
	public void setSocket(ObjectOutputStream oos,ObjectInputStream ois) {
			this.ois=ois;
			this.oos=oos;
			ThreadStart();
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
		web.getEngine().load("https://www.youtube.com/");
		Command c=new Command(Command.SHOWALLTUBE);
		c.setUser(user);
		sendData(c);
		web.getEngine().load("https://www.youtube.com/");
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
	              String title = (String) ((TreeItem)t.getSelectionModel().getSelectedItem()).getValue();
	              
	              //System.out.println("Node click: " + name);
	              //mytube();
	              Command c = new Command(Command.SHOWTUBE);
	              c.setUser(user);
	              c.setTitle(title);
	              sendData(c);
	             }
	          }
	      }
	}
	public void mytube()
	{
			web.getEngine().load("file:///D:/temp.html");
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
		url.setText(web.getEngine().getDocument().getBaseURI());
		
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
		c.setUser(user);
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
		System.out.println(this.category.getItems());
		Command command=new Command(Command.SAVE);
		command.setCategory(category);
		command.setTitle(t);
		command.setUrl(u);
		command.setUser(user);
		sendData(command);
		dialog.close();
	}
	public void search(ActionEvent e){
		Command c=new Command(Command.FIND);
		c.setContents(tf_search.getText());
		System.out.println("서치겟텍스트"+tf_search.getText());
		c.setUser(user);
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
				switch(cmd.getSatatus())
				{
					case Command.SAVE:
					{
						if(cmd.getCategory()==myLibrary.EDUCATION)
						{
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							node0.getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.KPOP)
						{	
							TreeItem<String> ti = new TreeItem<String>(cmd.getTitle());
							node1.getChildren().add(ti);
						}
						else if(cmd.getCategory()==myLibrary.TRAVEL)
						{	
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
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node0.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.KPOP)
							{	
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node1.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.TRAVEL)
							{	
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
						c.setUser(user);
						oos.writeObject(c);
						oos.reset();
						
						break;
					}
					
					case Command.SHOWALLTUBE:
					{
						ArrayList<myLibrary>al=new ArrayList<>();
						al=cmd.getAlist();
						for(myLibrary m:al)
						{
							if(m.getCategory()==myLibrary.EDUCATION)
							{
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node0.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.KPOP)
							{	
								TreeItem<String> ti = new TreeItem<String>(m.getTitle());
								node1.getChildren().add(ti);
							}
							else if(m.getCategory()==myLibrary.TRAVEL)
							{	
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
						Platform.runLater(()->{
							mytube();
						});
						break;
					}
					case Command.JOIN:
					{	
						Boolean r=cmd.getResult();
						popup=new Popup();
						try {
							parent = FXMLLoader.load(getClass().getResource("join.fxml"));
							Label label = (Label) parent.lookup("#lb_join");
							if(r)
							{
								label.setText("가입에 성공하셨습니다!");
							}
							else
							{
								label.setText("아이디가 중복됩니다");
							}
							Button button=(Button)parent.lookup("#btn_join");
							button.setOnAction(event->popup.hide());
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						popup.getContent().add(parent);
						popup.setAutoHide(true);
						Platform.runLater(()->{
						popup.show(primaryStage);
						});
						break;
					}
					case Command.LOGIN:
					{
						Boolean r=cmd.getResult();
						popup=new Popup();
						try {
							parent = FXMLLoader.load(getClass().getResource("join.fxml"));
							Label label = (Label) parent.lookup("#lb_join");
							if(r)
							{
								user=cmd.getUser();
								final Image mytube = new Image(getClass().getResourceAsStream("mytb.png"));
								Platform.runLater(()->{
								primaryStage.setScene(new Scene(pane));
								primaryStage.setTitle("MyTube");
								primaryStage.getIcons().addAll(mytube);
								primaryStage.show();
								Command c=new Command(Command.SHOWALLTUBE);
								c.setUser(user);
								sendData(c);
								});
							}
							else
							{
								label.setText("정보가 일치하지 않습니다");
								popup.getContent().add(parent);
								popup.setAutoHide(true);
								Platform.runLater(()->{
								popup.show(primaryStage);
								});
							}
							Button button=(Button)parent.lookup("#btn_join");
							button.setOnAction(event->popup.hide());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
	  public void setfile(String address){
		  
          File file = new File("d:/temp.html");
          System.out.println("어드레스"+address);
          String[] temp=new String[5];
          temp=address.split("v=");
          System.out.println("템프출력"+temp.length);
          try {
        	 System.out.println("파일롸이터");
            FileWriter fw = new FileWriter(file);
            String str = "<!DOCTYPE html><html><head><meta charset='utf-8'><title></title></head><body><iframe width='560' height='315' src='https://www.youtube.com/embed/"+temp[1]+"?version=3&arnp;hl=ko_KR&arnp;rel=0&amp;autoplay=1;showinfo=0' frameborder='0' allowfullscreen></iframe></body></html>";
            fw.write(str);
            fw.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
       }
}
