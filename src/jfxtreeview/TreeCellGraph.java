package jfxtreeview;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


final class TreeCellGraph extends GridPane {
	private final TreeViewController controller;
	private final TreeCellImpl cell;
	private final TextField textField = new TextField();
	private TextFocusHandler focusHandler;


	TreeCellGraph(TreeViewController controller, TreeCellImpl cell, ImageView imageView){
		this.controller = controller;
		this.cell = cell;
		this.textField.setOnKeyPressed(new TextFieldKeyPressedHandler());
		add(imageView, 0, 0);
		add(this.textField, 1, 0);
	}


	void startEdit(){
		final TextField textField = this.textField;
		this.focusHandler = new TextFocusHandler();
		textField.focusedProperty().addListener(this.focusHandler);
		textField.setText(this.cell.getItem().getName());
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				textField.selectAll();
				textField.requestFocus();
			}
		});
	}


	private void endingEditByKeyboard(){
		
		final TextField textField = this.textField;
		final String currentText = textField.getText();
		if(currentText.isEmpty()){
			return;
		}
	
		final TreeCellImpl cell = this.cell;
		final String prevText = cell.getItem().getName();
		if(currentText.equals(prevText) == true){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
	
		final TreeItem<TreeItemData> treeItem = cell.getTreeItem();
		final Iterator<TreeItem<TreeItemData>> children = treeItem.getParent().getChildren().iterator();
		while(children.hasNext()){
			final TreeItem<TreeItemData> child = children.next();
		
			if(treeItem != child && currentText.equals(child.getValue().getName())){
				return;
			}
		}
		
		final TreeItemData newTreeItemData = this.controller.treeItemDataRenamed(treeItem, currentText);
		if(newTreeItemData == null){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		removeFocusHandler();
		cell.commitEdit(newTreeItemData);
	}

	private void endingEditByLostingFocus(){
		final TextField textField = this.textField;
		final String currentText = textField.getText();
		if(currentText.isEmpty()){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		final TreeCellImpl cell = this.cell;
		final String prevText = cell.getItem().getName();
		if(currentText.equals(prevText) == true){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		final TreeItem<TreeItemData> treeItem = cell.getTreeItem();
		final Iterator<TreeItem<TreeItemData>> children = treeItem.getParent().getChildren().iterator();
		while(children.hasNext()){
			final TreeItem<TreeItemData> child = children.next();
			if(treeItem != child && currentText.equals(child.getValue().getName())){
				removeFocusHandler();
				cell.cancelEdit();
				return;
			}
		}
		final TreeItemData newTreeItemData = this.controller.treeItemDataRenamed(treeItem, currentText);
		if(newTreeItemData == null){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		removeFocusHandler();
		cell.commitEdit(newTreeItemData);
	}

	private void removeFocusHandler(){
		this.textField.focusedProperty().removeListener(this.focusHandler);
		this.focusHandler = null;
	}

	final class TextFieldKeyPressedHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			switch(e.getCode()){
			case ENTER:
				TreeCellGraph.this.endingEditByKeyboard();
				break;
			case ESCAPE:
				TreeCellGraph.this.cell.cancelEdit();
				break;
			default:
			}
		}
	}

	final class TextFocusHandler implements ChangeListener<Boolean>{
		@Override
		public void changed(ObservableValue<? extends Boolean> o, Boolean b1, Boolean b2) {
			if(b2==false){
				TreeCellGraph.this.endingEditByLostingFocus();
			}
		}
	}
}
