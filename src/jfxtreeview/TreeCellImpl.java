package jfxtreeview;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

final class TreeCellImpl extends TreeCell<TreeItemData> {
	private static final Image GROUP_IMAGE = createImage("folder.gif");
	private static final Image ITEM_IMAGE = createImage("file.gif");
	
	private final TreeViewController controller;
	
	private TreeCellGraph graph;


	TreeCellImpl(TreeViewController controller){
		this.controller = controller;
	}

	@Override
	public void startEdit() {
		super.startEdit();
		
		if(this.graph == null){
			this.graph = new TreeCellGraph(this.controller, this, createImageView(getItem()));
		}
		
		this.graph.startEdit();
		
		setText(null);
		setGraphic(this.graph);
	}

	@Override
	public void cancelEdit(){
		super.cancelEdit();
		
		setText(getItem().getName());
		setGraphic(createImageView(getItem()));
	}

	@Override
	public void updateItem(TreeItemData data, boolean empty){
		super.updateItem(data, empty);
		if(empty){
			
			setText(null);
			setGraphic(null);
		}else if(isEditing()){
			
			setText(null);
			setGraphic(this.graph);
		} else {
			
			setText(data.getName());
			setGraphic(createImageView(data));
		}
	}

	private static Image createImage(String fname){
		return new Image(TreeCellImpl.class.getResourceAsStream(fname));
	}


	private static ImageView createImageView(TreeItemData data){
		final Image img = (data.getType() == TreeItemData.Type.GROUP) ?
			GROUP_IMAGE : ITEM_IMAGE;
		return new ImageView(img);
	}
}
