package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	public boolean isRunning = true;
	private Socket s1 = null;
	private PrintWriter os;
	private PingThread pt;
	private ClientListeningThread CLT;
	private WindowThread WT = null;
	private String username = "";


	public Client(int port, String ip) {
		  	try {
				s1=new Socket(ip, port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  	
		  	CLT = new ClientListeningThread(s1, this);
		  	CLT.start();
		  	
		  	username = System.getProperty("user.name");
		  	
		  
		  	try {
				os = new PrintWriter(s1.getOutputStream());
			} catch (IOException e1) {
				isRunning = false;
				e1.printStackTrace();
			}
		  	
		  	/*
		  	pt = new PingThread(this);
		  	pt.start();
		  	*/
		  	
		  	WT = new WindowThread(this);
		  	//WT.start();
		  	
		  	if (username == "") {
		  		username = "default";
		  	}
		  	
		  	this.sendMessageToServer("USERNAME " + username);
		  	
		  	//this.start();
		  	
		  	
	       
	}
	
	/*
	public void run() {
		
		while (isRunning) {
	  		
	  		try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	  	    
	  	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			 
			
			
			String inputString = "";
			try {
				inputString = br.readLine();
				br.
				
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			}
			if (inputString!="") {
				if (inputString.equalsIgnoreCase("QUIT")) {
					isRunning = false;
				}
				this.sendMessageToServer("CHAT " + inputString);
				
			}
			
	  	}
		
	}
	*/

	public void sendMessageToServer(String string) {
		os.println(string);
		os.flush();
		
	}
	
	public PingThread getPt() {
		return pt;
	}

	public void setPt(PingThread pt) {
		this.pt = pt;
	}

	public void stopAll() {
		this.isRunning = false;
		this.pt.setRunning(false);
		this.CLT.setIsRunning(false);
		
	}

	public void receivedChatMessage(String message) {
		if (WT != null) {
		WT.receivedChatMessage(message);
		}
		System.out.println(message);
	}

	public void updatePing(String pingTook) {
		if (WT != null) {
		WT.updatePing(pingTook);
		}
	}

	public void changeUsername(String string) {
		username = string;
		
	}
	
	public String getUsername() {
		return this.username;
	}

	public void popUpMessage(String shutdownMes) {
		if (WT != null) {
			WT.popUpMessage(shutdownMes);
			}
		
	}

	public void Systemmessage(String sysmessage) {
		if (WT != null) {
			WT.receivedSystemMessage(sysmessage);
			}
		
	}

	public void lobbyAdded(String string) {
		if (WT != null) {
			WT.lobbyAdded(string);
			}
		
	}

}
