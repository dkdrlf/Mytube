package jfxtreeview;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class TreeViewController {
	
	/**
	 * 트리뷰 셋팅 
	 * @param treeview = fxml에서 생성한 트리뷰
	 */
	public TreeViewController(TreeView<TreeItemData> treeview) {
		//트리뷰 폴더 생성
		final TreeItem<TreeItemData> rootNode = new TreeItem<>(new TreeItemData("Root", TreeItemData.Type.GROUP));
		final TreeItem<TreeItemData> node0 = new TreeItem<>(new TreeItemData("교육", TreeItemData.Type.GROUP));
		final TreeItem<TreeItemData> node1 = new TreeItem<>(new TreeItemData("K-POP", TreeItemData.Type.GROUP));
		final TreeItem<TreeItemData> node2 = new TreeItem<>(new TreeItemData("여행", TreeItemData.Type.GROUP));
		rootNode.setExpanded(true); //ture 폴더가 펼친형태 근대 안먹힘 
		rootNode.getChildren().add(node0);
		rootNode.getChildren().add(node1);
		rootNode.getChildren().add(node2);
		
		//트리뷰 폴더에 항목생성
		for(int i=0; i<10; i++){
			node0.getChildren().add(new TreeItem<>(new TreeItemData("Node0"+i)));
			node1.getChildren().add(new TreeItem<>(new TreeItemData("Node1"+i)));
			node2.getChildren().add(new TreeItem<>(new TreeItemData("Node2"+i)));
		}
		
		treeview.setRoot(rootNode);
		treeview.setCellFactory(new TreeViewCellFactory());
	}

	protected TreeItemData treeItemDataRenamed(TreeItem<TreeItemData> treeItem, String name){
		return new TreeItemData(name, treeItem.getValue().getType());
	}

	final class TreeViewCellFactory implements Callback<TreeView<TreeItemData>,TreeCell<TreeItemData>> {
		@Override
		public TreeCell<TreeItemData> call(TreeView<TreeItemData> treeview){
			return new TreeCellImpl(TreeViewController.this);
		}
	}
}
