package server;

import java.net.InetAddress;

import client.ClientThread;

public class Client {

	private String username = "";
	private String ip = "";
	
	


	private database db;
	private int id;
	public boolean clientAlreadyOpen;
	
	public String getIp() {
		return ip;
	}
	
	public Client(InetAddress inetAddress, database db) {
		
		this.db = db;
		String ip = inetAddress.toString().substring(1);
		
		if (db.clientWithIPAlreadyExists(ip)) {
			System.out.println("client already exists in database!");
			clientAlreadyOpen = true;
			
		} else {
			System.out.println("creating new client in database");
			db.createNewClient(ip);
			clientAlreadyOpen = false;
		}
		id = db.getIDfromDBdependingOnIP(ip);
	}


	public void Client() {
		
	}
	

	public void setUsername(String string) {
		db.setUsername(id, string);
		this.username = string;
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getCoins() {
		return db.getCoins(id);
	}


	public void earnedCoin(int i) {
		int coins = db.clientearnedCoin(i, id);
	}


}
