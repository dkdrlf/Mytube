package Mytube.client;

public class TreeItemData {
	private final String name;	
	private final Type type;	


	public TreeItemData(String name) {
		this(name, Type.ITEM);
	}


	public TreeItemData(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	
	public String getName(){
		return this.name;
	}

	
	public Type getType(){
		return this.type;
	}

	
	@Override
	public String toString(){
		return this.getName();
	}

	
	public enum Type {
		/**グループです。*/
		GROUP,
		/**アイテムです。*/
		ITEM,
	}
}
