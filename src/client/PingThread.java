package client;


import java.util.ArrayList;


@Deprecated
public class PingThread extends Thread {
	
	private static final long PING_MS = 10000;
	private static final long DISCONNECT_AFTER_MISSED_PING_INT = 4;
	private static long Ping_LocalTime;
	public boolean isRunning = true;
	

	private Client client = null;
	@SuppressWarnings("unused")
	private int pingsNotReceived;
	ArrayList<Long> ping = new ArrayList<Long>();
	

	public PingThread(Client c) {
		this.client = c;
		
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		
		while (isRunning) {
			
			long startTime = System.currentTimeMillis();
	  	    long elapsedTime = startTime - Ping_LocalTime;
	  	    if (elapsedTime > this.PING_MS) {
	  	    	ping.add(startTime);
	  	    	client.sendMessageToServer("PING " + startTime);
	  	    	Ping_LocalTime = System.currentTimeMillis();
	  	    }
	  	    
	  	    if (ping.size() > DISCONNECT_AFTER_MISSED_PING_INT) {
	  	    	client.stopAll();
	  	    }
			
			
		}
		
	}

	public void pingReceived(String string) {
		long received = Long.valueOf(string);
		Boolean removed = ping.remove(received);
		if (removed == true) {
			long pingTook = System.currentTimeMillis();
			pingTook = pingTook - received;
			System.out.println("PING: " + pingTook + "MS");
			String a = Long.toString(pingTook);
			client.updatePing(a);
		}
		
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

}
