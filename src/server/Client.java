package server;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;


public class Client {

	private String username = "testname";
	private String ip = "localhost";
	
	


	private database db;
	private int id;
	public boolean clientAlreadyOpen;
	
	public String getIp() {
		return ip;
	}
	
	public Client(InetAddress inetAddress, database db) {
		
		this.db = db;
		String ip = inetAddress.toString().substring(1);
		
		this.username = inetAddress.toString().substring(1);
		
		if (db.clientWithIPAlreadyExists(ip)) {
			System.out.println("client already exists in database!");
			clientAlreadyOpen = true;
			
		} else {
			System.out.println("creating new client in database");
			db.createNewClient(ip);
			clientAlreadyOpen = false;
		}
		id = db.getIDfromDBdependingOnIP(ip)+ ThreadLocalRandom.current().nextInt(10)*10;
		id = id + ThreadLocalRandom.current().nextInt(10)*100;
		id = id + ThreadLocalRandom.current().nextInt(10)*1000;
	}


	public Client() {
		
	}
	

	public void setUsername(String string) {
		db.setUsername(id, string);
		this.username = string;
		
	}
	
	public String getUsername() {
		if (this.username.equalsIgnoreCase("")) {
			if (this.ip.equalsIgnoreCase("")) {
				return "noname";
			} else {
				return this.ip;
			}
		}
		return this.username;
	}
	
	public int getCoins() {
		return db.getCoins(id);
	}


	public void earnedCoin(int i) {
		@SuppressWarnings("unused")
		int coins = db.clientearnedCoin(i, id);
	}

	public int getID() {
		return this.id;
	}


}
