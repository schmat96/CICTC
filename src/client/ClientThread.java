package client;

public class ClientThread extends Thread {

	private Client client = null;
	private boolean isRunning = true;
	
	public boolean isRunning() {
		return isRunning;
	}


	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	public ClientThread(Client c) {
		this.client = c;
	}
	
	
	public void run() {
		while (isRunning) {
			
		}
	}
	
}
