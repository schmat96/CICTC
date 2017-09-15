package client;

import java.util.ArrayList;

public interface listForTabpane {
	
	ArrayList<String> conversation = new ArrayList<String>();
	String name = "";
	int id = 0;
	
	String getName();
	String getConversation();
	void addText(String e);
	
}
