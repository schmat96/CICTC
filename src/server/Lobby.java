package server;

import java.util.ArrayList;
import java.util.Iterator;



public class Lobby {
	
	private String name;
	ArrayList<Client> users = new ArrayList<Client>();
	
	public Lobby() {
		
	}
	
	public Lobby(String name, Client c) {
		this.name = name;
		this.users.add(c);
	}
	
	public void removeClient(Client c) {
		this.users.remove(c);
	}
	
	public void addClient(Client c) {
		this.users.add(c);
	}
	
	public String getName() {
		return name;
	}

}
