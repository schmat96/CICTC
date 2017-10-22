package server;

import java.util.ArrayList;



public class Lobby {
	
	private int id;
	private String name;
	private String passwort;
	ArrayList<Client> users = new ArrayList<Client>();
	
	public Lobby() {
		
	}
	
	public Lobby(String name, Client c, String string, int id) {
		this.id = id;
		this.name = name;
		this.users.add(c);
		this.passwort = string;
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
	
	public String getPasswort() {
		return passwort;
	}

	public int getID() {
		return this.id;
	}

}
