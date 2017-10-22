package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Client {
	
	public boolean isRunning = true;
	private Socket s1 = null;
	private PrintWriter os;
	private ClientListeningThread CLT;
	private WindowThread WT = null;
	private String username = "";
	private BufferedImage ScreenShot;
	private ServerSocket serverSocket;


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
		  	
		  	
		  	//WT.start();
		  	
		  	if (username == "") {
		  		username = "default";
		  	}
		  	
		  	this.sendMessageToServer("USERNAME " + username);
		  	
		  	WT = new WindowThread(this);
		  	
		  	
	       
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

	public void stopAll() {
		this.isRunning = false;
		this.CLT.setIsRunning(false);
		
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

	public void lobbyAdded(String string, int id) {
		if (WT != null) {
			WT.addLobby(string, id);
			}		
	}

	public void receivedWhisperMessage(String message, int id) {
		if (WT != null) {
			WT.receivedWhisperMessage(message, id);
			}
	}
	
	public void receivedChatMessage(String message, int id) {
		if (WT != null) {
			WT.receivedChatMessage(message, id);
			}
	}
	
	public void addUser(String s, int id) {
		if (WT != null) {
			WT.addUser(s, id);
		}
	}

	public void removeUser(int id) {
		if (WT != null) {
			WT.removeUser(id);
		}
		
	}

	public void lobbyPermission(int id, Boolean perm) {
		if (WT != null) {
			WT.setPermission(id, perm);
		}
		
	}

	public void setScreenShot(BufferedImage screenShotImage2, String ID) {
		if (ScreenShot==null) {
		this.ScreenShot = screenShotImage2;
		this.sendMessageToServer("SYSTEM SCREENSHOT "+ ID);
		} else {
			WT.receivedChatMessage("U can only send 1 ScreenShot at a Time!", 0000);
		}
		
	}

	public void sendScreenShot(String input, String port) {
		if (ScreenShot!=null) {
		Socket screenShotSocket = null;
		while(screenShotSocket==null) {
		
		try {
			screenShotSocket=new Socket(input, Integer.parseInt(port));
		} catch (UnknownHostException e1) {
			screenShotSocket = null;
			e1.printStackTrace();
		} catch (IOException e1) {
			screenShotSocket = null;
			e1.printStackTrace();
		}
	}
		System.out.println("Sending ScreenShot");
		try {
			OutputStream outputStream = screenShotSocket.getOutputStream();
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(ScreenShot, "JPG", byteArrayOutputStream);
	        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
	        
	        outputStream.write(size);
	        outputStream.write(byteArrayOutputStream.toByteArray());
	        //outputStream.write(b);
	        outputStream.flush();
	        
	        System.out.println("Flushed: " + System.currentTimeMillis());
		} catch (IOException e) {
			System.out.println("failed");
			e.printStackTrace();
		}
		this.ScreenShot = null;
		try {
			screenShotSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	public void openScreenShotListener(String string) {
		int port = Integer.parseInt(string);
		
		
		try {
            serverSocket = new ServerSocket(port);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		new screenShotListener(serverSocket);
		
		
		
	}										


	
	

}
