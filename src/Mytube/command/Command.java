package Mytube.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Mytube.vo.myLibrary;

public class Command implements Serializable{

	private String title;
	private String url;
	private int category;
	private int satatus;
	private String contents;
	private boolean result;
	
	ArrayList<myLibrary> alist=new ArrayList<>();
	
	static final public int FIND=20;
	static final public int DELETE=30;
	static final public int SAVE=10;
	static final public int SHOWALLTUBE=40;
	
	public Command(int status){
		this.satatus = status;
	}
	
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
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
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
	
	public String getContents(){
		return contents;
	}
	public void setContents(String contents){
		this.contents = contents;
	}
	public boolean getResult(){
		return result;
	}
	public void setResult(boolean result){
		this.result = result;
	}
}
