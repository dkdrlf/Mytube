package Mytube.vo;

import java.io.Serializable;

public class myLibrary implements Serializable{
	private int category;
	private String title;
	
	static final public int EDUCATION=10;
	static final public int KPOP=20;
	static final public int TRAVEL=30;
	
	public myLibrary(int category, String title){
		this.category = category;
		this.title = title;
	}
	
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "myLibrary [category=" + category + ", title=" + title + "]";
	}
	
	
}

