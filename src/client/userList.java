package client;

import java.util.ArrayList;
import java.util.Iterator;

public class userList implements listForTabpane {
	
	private ArrayList<String> conversation = new ArrayList<String>();
	private String name = "";
	private int id = 0;

	public userList(String name, int id) {
		this.id = id;
		this.name = name;
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

}
