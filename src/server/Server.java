package server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.*;

public class Server {
	
	private JTextField JTEXTFIELD_Input = new JTextField();
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	private JPanel JPANEL_Main = new JPanel();
	
	private static database db = new database();
	
	ArrayList<clientThread> users = new ArrayList<clientThread>();
	
	ArrayList<Lobby> lobbies = new ArrayList<Lobby>();

	public Server(int port) {
		

		
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
				
				Iterator<clientThread> tobecheckedagainst = users.iterator();
				
				boolean canbeadded = true;
				while(tobecheckedagainst.hasNext() ) {
					clientThread checker = tobecheckedagainst.next();
					if( clientThread.getIP().equals(checker.getIP()) ) {
						clientThread.sendMessage("SYSTEM CLOSE");
						canbeadded = false;
					}
					
					
				}
				if(canbeadded) {
					users.add(clientThread);
				} else {
					clientThread.stop();
				}
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
			System.out.println("Sending to Client");
		}
		
	}


	public void closeClientThread(clientThread clientThread) {
		clientThread finalUser = null;
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


	public void messageToAllExcpectSender(String s, String username) {
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user.getClient().getUsername().equals(username)) {
				user.sendMessage("SYSTEM SENDED");
			} else {
				user.sendMessage(s);
			}
			
			System.out.println("Sending to Client: " + s);
		}
		
	}


	public void messageToSpecified(String message, String to) {
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			if (user.getClient().getUsername().equals(to)) {
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
		Lobby lobby = new Lobby(string, client, passwort);
		this.lobbies.add(lobby);
		this.messageToAll("LOBBY ADD "+ string);
		}
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


	public void removeClientFromLobby(String string, Client client) {
		this.messageToAllExcpectSender("SYSTEM REMOVEUSER "+ string, string);
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			if (lobby.getName().equals(string)) {
				lobby.removeClient(client);
				return;
			}
		}
		
		
	}


	public void sendInfos(Client client) {
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			this.messageToSpecified("LOBBY ADD "+ lobby.getName(), client.getUsername());
		}
		
		Iterator<clientThread> userIterator = users.iterator();
		while (userIterator.hasNext()) {
			clientThread user = userIterator.next();
			this.messageToSpecified("SYSTEM ADDUSER " + user.getClient().getUsername(), client.getUsername());
		}
	}


	public Boolean LobbyPermission(String string, String string2, String name) {
		Iterator<Lobby> lobbyIterator = this.lobbies.iterator();
		while (lobbyIterator.hasNext()) {
			Lobby lobby = lobbyIterator.next();
			if (lobby.getName().equalsIgnoreCase(string)) {
				if (lobby.getPasswort().equalsIgnoreCase(string2)) {
					return true;
				} else {
					return false;
				}
			}

			
		}
		return false;
		
	}
}
