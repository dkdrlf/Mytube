package Mytube.client;
 
import java.net.Socket;
 
import Mytube.vo.myLibrary;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
 
public class TreeViewController {
	
	private TreeItem<String> rootNode;
	private TreeItem<String> node0;
	private TreeItem<String> node1;
	private TreeItem<String> node2;
	
	private TreeView<String> treeview;
	/**
	 * 트리뷰 셋팅 
	 * @param treeview = fxml에서 생성한 트리뷰
	 */
	public TreeViewController(TreeView<String> treeview) {
		this.treeview = treeview;
		//트리뷰 폴더 생성
		rootNode = new TreeItem<>();
		node0 = new TreeItem<String>("교육"); 
		node1 = new TreeItem<String>("K-POP");
		node2 = new TreeItem<String>("여행");
		rootNode.setExpanded(true); //ture 폴더가 펼친형태 근대 안먹힘 
		rootNode.getChildren().add(node0);
		rootNode.getChildren().add(node1);
		rootNode.getChildren().add(node2);
		this.treeview.setRoot(rootNode);
	}
	
	public TreeView<String> getTreeview() {
		return treeview;
	}

	public TreeItem<String> getRootNode() {
		return rootNode;
	}
	public TreeItem<String> getNode0() {
		return node0;
	}
	public TreeItem<String> getNode1() {
		return node1;
	}
	public TreeItem<String> getNode2() {
		return node2;
	}
}
	
	