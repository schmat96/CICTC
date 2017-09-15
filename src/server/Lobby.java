package server;

import java.util.ArrayList;
import java.util.Iterator;



public class Lobby {
	
	private String name;
	private String passwort;
	ArrayList<Client> users = new ArrayList<Client>();
	
	public Lobby() {
		
	}
	
	public Lobby(String name, Client c, String string) {
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

}
