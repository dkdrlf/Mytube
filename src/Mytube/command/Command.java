package Mytube.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Mytube.vo.myLibrary;

public class Command implements Serializable{

	private String title;
	private String url;
	private String category;
	private int satatus;
	ArrayList<myLibrary> alist=new ArrayList<>();
	
	static final public int FIND=10;
	static final public int delete=20;
	static final public int store=30;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getSatatus() {
		return satatus;
	}
	public void setSatatus(int satatus) {
		this.satatus = satatus;
	}
	public ArrayList<myLibrary> getAlist() {
		return alist;
	}
	public void setAlist(ArrayList<myLibrary> alist) {
		this.alist = alist;
	}
	
	
	
	
	
}
