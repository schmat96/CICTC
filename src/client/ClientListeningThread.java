package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientListeningThread extends Thread {
	
	private Socket socket  = null;
	private BufferedReader is = null;
	private Boolean isRunning = true;
	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	private Client client = null;
	private InputStream inp;
	

	public ClientListeningThread(Socket s1, Client c) {
		this.socket = s1;
		this.client = c;
		
	}
	
	public void run() {
		while(isRunning) {
			String line = "";
			try {
	
				 inp = socket.getInputStream();
		            
				is=new BufferedReader(new InputStreamReader(inp));
				 line = is.readLine();
				
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			}

			String s = "";
			String[] input = null;
			
			
				
					s = line;
					System.out.println(s);
			
					try {
						input = s.split("\\s+");
					} finally {
						
					}
					
					String comparison = input[0].toUpperCase();
					comparison = comparison.replaceAll("\\s+", "");
						
					switch (comparison) {
						case "PING":
							
							break;
						case "CHAT":
							String message = "";
							switch (input[1]) {							
								case "WHISPER":
									int id = Integer.parseInt(input[2]);
									for (int i = 3;i<input.length;i++) {
										message = message + input[i] + " ";
									}
									client.receivedWhisperMessage("<font color=\"blue\">" + message + "</font>", id);
									break;
								case "LOBBY":
									int idLobby = Integer.parseInt(input[2]);
									for (int i = 3;i<input.length;i++) {
										message = message + input[i] + " ";
									}
									client.receivedChatMessage(message, idLobby); 
									
									break;
								default:
									for (int i = 2;i<input.length;i++) {
										message = message + input[2] + " ";
									}
									client.receivedChatMessage(message, 0000);
									
								}
								break;

						case "USERNAME":
							client.changeUsername(input[1]);
							break;
						case "LOBBY":
							switch (input[1]) {
							case "ADD":
								try {
								for (int i = 2;i<input.length;i=i+2) {
								int idLobby = Integer.parseInt(input[i+1]);
								System.out.println("Adding new Lobby with ID: " + idLobby);
								client.lobbyAdded(input[i], idLobby);
								}
								} catch (Exception ArrayIndexOutOfBoundsException) {
									
								}
								break;
							case "PERMISSION":
									client.lobbyPermission(Integer.parseInt(input[2]), true);
									break;
							}
							break;
						case "SYSTEM":
							switch (input[1]) {
								case "CLOSE":
									System.exit(0);
									break;
								case "SHUTDOWN":
									String shutdownMes = "";
									for (int i = 2;i<input.length;i++) {
										shutdownMes = shutdownMes + input[i] + " ";
									}
									client.popUpMessage(shutdownMes);
									break;
								case "MESSAGE":
									String sysmessage = "";
									for (int i = 2;i<input.length;i++) {
										sysmessage = sysmessage + input[i] + " ";
									}
									client.receivedChatMessage(sysmessage, 0000);
									break;
								case "POPUP":
									String popmessage = "";
									for (int i = 2;i<input.length;i++) {
										popmessage = popmessage + input[i] + " ";
									}
									client.popUpMessage(popmessage);
									break;
								case "ADDUSER":
									try {
										for (int i = 2;i<input.length;i=i+2) {
											int idAddUser = Integer.parseInt(input[i+1]);
											client.addUser(input[i], idAddUser);
										}
										
									} catch (Exception ArrayIndexOutOfBoundsException) {
										
									}
									break;
								case "REMOVEUSER":
									try {
									client.removeUser(Integer.parseInt(input[2]));
									} catch (Exception ArrayIndexOutOfBoundsException) {
										
									}
									break;
								case "SCREENSHOTSEND":

											client.sendScreenShot(input[2], input[3]);
										
											
									break;
								case "SCREENSHOTOPEN":
									client.openScreenShotListener(input[2], Integer.parseInt(input[3]));
										break;

								default:
									System.out.println("cant resolve message second level: " + s);
									break;
							}
							break;
						default:
							System.out.println("cant resolve message first level: " + s);
							break;
					}
					/*
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Could not sleep");
				e.printStackTrace();
			}
			*/
		}
	}
	
	@SuppressWarnings("unused")
	private void receivedScreenShot(InputStream inp2) {
		System.out.println("Reading: " + System.currentTimeMillis());

        byte[] sizeAr = new byte[4];
        try {
        	   
			inp2.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

	        byte[] imageAr = new byte[size];
	        inp2.read(imageAr);

	        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
	        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
	        JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(image)));
            frame.pack();
            frame.setVisible(true);
            while (inp2.read()!=-1) {
            		System.out.println("there was something in the pipe");
            }
            return;
            
        } catch (IOException e) {
			System.out.println("What there was an unexcpected Error");
			e.printStackTrace();
			return;
		}
        

        
		
	}
	
	
	

}
