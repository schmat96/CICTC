package client;

import java.util.ArrayList;
import java.util.Iterator;

public class lobbyList implements listForTabpane {

	private ArrayList<String> conversation = new ArrayList<String>();
	private String name = "";
	private int id = 0;
	private Boolean permission = true;
	private int newMessageCount = 0;
	
	
	
	public lobbyList(String name, int id) {
		this.name = name;
		this.id = id;
		if (name.equalsIgnoreCase("System")) {
			permission = true;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getConversation() {
		String hue = "";
		Iterator<String> userIterator = conversation.iterator();
		while (userIterator.hasNext()) {
			hue = hue + userIterator.next() + "<br>";
		}
		return hue;
	}

	@Override
	public void addText(String e) {
		this.conversation.add(e);
		
	}
	
	public Boolean getPermission() {
		return permission;
	}

	public void setPermission(Boolean perm) {
		this.permission = perm;
		
	}

	public int getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public int getNewMessages() {
		
		return newMessageCount;
	}

	public void AddNewMessageCount(int i) {
		this.newMessageCount = this.newMessageCount + i;
		
	}

	

	

}
