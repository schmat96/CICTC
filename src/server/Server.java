package server;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Server {
	
	private JTextField JTEXTFIELD_Input = new JTextField();
	private ServerSocket serverSocket = null;

	private Socket socket = null;

	
	private ScreenShotListenerThread  sslt = null;
	
	private JPanel JPANEL_Main = new JPanel();
	
	private static database db = new database();
	
	ArrayList<clientThread> users = new ArrayList<clientThread>();
	
	ArrayList<Lobby> lobbies = new ArrayList<Lobby>();
	
	ArrayList<Integer> randomPool = new ArrayList<Integer>();
	
	

	public Server(int port) {
		
		randomPool.add(0000);
		
		try {
            serverSocket = new ServerSocket(port);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		JFrame frame = new JFrame("CICTC Version");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTEXTFIELD_Input.setText("Eine normale Nachricht zum nicht verr�ckt werden");
		JTEXTFIELD_Input.setPreferredSize(JTEXTFIELD_Input.getPreferredSize());
		JTEXTFIELD_Input.setText("");
		JButton jb = new JButton("Close all Clients");
		jb.setPreferredSize(jb.getPreferredSize());
		
		 jb.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	messageToAll("SYSTEM CLOSE");
	        	
	        }
	      });
		 JButton message = new JButton("Message Clients");
		 message.setPreferredSize(message.getPreferredSize());
	
			message.addActionListener(new ActionListener()
		      {
		        public void actionPerformed(ActionEvent e)
		        {
		        	messageToAll("SYSTEM MESSAGE " + JTEXTFIELD_Input.getText());
		        	JTEXTFIELD_Input.setText("");
		        	
		        }
		      });
			
			JPANEL_Main.add(jb);
			JPANEL_Main.add(message);
			JPANEL_Main.add(JTEXTFIELD_Input);
			
			frame.add(JPANEL_Main);
			
			
		frame.setSize(new Dimension(500, 1000));  
		frame.setLocation(2000, 100);
		frame.setVisible(true);
		frame.pack();
		frame.setFocusable(true);
		frame.setVisible(true);
		
		

		while (true) {
			
	        
	        try {
				socket = serverSocket.accept();
				clientThread clientThread = new clientThread(socket, this, db);
				
				//if(clientThread.terminateClient == false) { //if the client doesn't already exist start new client thread
				clientThread.start();
				clientThread.setIsRunning(true);
				users.add(clientThread);
				//this.sendInfos(clientThread.getClient());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		}

	}


	public void messageToAll(String string) {
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			user.sendMessage(string);
		}
		
	}


	public void closeClientThread(clientThread clientThread) {
		clientThread finalUser = null;
		this.messageToAllExcpectSender("SYSTEM REMOVEUSER "+clientThread.getClient().getID(),clientThread.getClient().getID());
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user == clientThread) {
				finalUser = user;
			}
		}
		finalUser.setIsRunning(false);		
		
		Boolean removed = users.remove(clientThread);
		if (removed == true) {
			System.out.println("Client succesfully removed.");
		}
		
	}


	public String checkUsername(String string) {
		String username = string;
		while (true) {
		boolean doubleUsername = false;
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			Client c = user.getClient();
			if (c.getUsername().equals(username)) {
				doubleUsername = true;
			}
		}
		if (doubleUsername) {
			username = username+"1";
		} else {
			return username;
		}
		}
	}


	public void messageToAllExcpectSender(String s, int ID) {
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user.getClient().getID() == ID) {
				
			} else {
				user.sendMessage(s);
			}
			
			
		}
		
	}


	public void messageToSpecified(String message, int id) {
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user.getClient().getID() == id) {
				user.sendMessage(message);
		}
		}
		
	}


	public void addLobby(String string, Client client, String passwort) throws DoubleLobbyNameExcpection, permissionExcpection {
		int permission = db.getUserPermission(client);
		if (permission != 0) {
			throw new permissionExcpection();
		} else {
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			if (lobby.getName().equals(string)) {
				throw new DoubleLobbyNameExcpection();
				
			}
		}
		int id = idGenerator();
		Lobby lobby = new Lobby(string, client, passwort, id);
		this.lobbies.add(lobby);
		this.messageToAll("LOBBY ADD "+ string + " " + id);
		}
	}


	private int idGenerator() {
		
		Boolean needAnother = true;
		int id = 0;
		
		while(needAnother) {
			needAnother = false;	
			
			for (int i = 1; i<10000;i=i*10) {
				id = id + ThreadLocalRandom.current().nextInt(10)*i;
			}
			
			Iterator<Integer> randomPoolIt = this.randomPool.iterator();
			while (randomPoolIt.hasNext()) {
				if (randomPoolIt.next() == id) {
					needAnother = true;
				}
				
			}
		}
		
		
		return id;
	}


	public void addClientToLobby(String string, Client client, String passwort) {
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			if (lobby.getName().equals(string)) {
				if (lobby.getPasswort().equals(passwort)) {
				lobby.addClient(client);
				return;
				} else {
					
				}
			}
		}
		
	}


	public void sendInfos(Client client) {
		String lobbiesToSend = "";
		
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			lobbiesToSend = lobbiesToSend + lobby.getName() + " " + lobby.getID() + " ";
		}
		
		if (lobbiesToSend.equalsIgnoreCase("")) {
			
		} else {
			this.messageToSpecified("LOBBY ADD "+ lobbiesToSend, client.getID());
		}
		
		String messageToSend = "";
		
		Iterator<clientThread> userIterator = this.users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			messageToSend = messageToSend + user.getClient().getUsername() + " " +user.getClient().getID() + " ";
		}
		this.messageToSpecified("SYSTEM ADDUSER " + messageToSend, client.getID());
	}


	public Boolean LobbyPermission(int ID, String string2, String name) {
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			if (lobby.getID() == ID) {
				if (lobby.getPasswort().equalsIgnoreCase(string2)) {
					return true;
				} else {
					return false;
				}
			}

			
		}
		return false;
		
	}


	public void setScreenShotListenerThread(boolean b) {
		if (sslt != null) {
			if (sslt.getIsRunning()==false) {
		sslt.setIsRunning(true);
		sslt.run();
			}
		}
		
		
	}


	public void sendReadyScreenshot(int ID, int toWhom) {
		String ip = "";
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user.getClient().getID()==toWhom) {
				 ip = user.getIP();
			}
		}
		
		
		this.messageToSpecified("SYSTEM SCREENSHOTOPEN 16005", toWhom);
		this.messageToSpecified("SYSTEM SCREENSHOTSEND "+ip+" 16005", ID);
		
	}
}
