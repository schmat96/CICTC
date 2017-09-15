package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientListeningThread extends Thread {
	
	private Socket socket  = null;
	private BufferedReader is = null;
	private PrintWriter os = null;
	private Boolean isRunning = true;
	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	private Client client = null;
	

	public ClientListeningThread(Socket s1, Client c) {
		this.socket = s1;
		this.client = c;
		
	}
	
	public void run() {
		while(isRunning) {
			try {
				is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			}

			String s = "";
			String[] input = null;
			
			
				try {
					s = is.readLine();
				} catch (IOException e) {
					isRunning = false;
					e.printStackTrace();
				}
					try {
						input = s.split("\\s+");
					} finally {
						
					}
						
					switch (input[0]) {
						case "PING":
							client.getPt().pingReceived(input[1]);
							break;
						case "CHAT":
							String message = "";
							String name = "";
							switch (input[1]) {
								
								case "WHISPER":
									name = input[2];
									for (int i = 3;i<input.length;i++) {
										message = message + input[i] + " ";
									}
									client.receivedWhisperMessage(message, name);
									break;
								case "LOBBY":
									name = input[2];
									for (int i = 3;i<input.length;i++) {
										message = message + input[i] + " ";
									}
									client.receivedChatMessage(message, name); 
									
									break;
								default:
									for (int i = 2;i<input.length;i++) {
										message = message + input[2] + " ";
									}
									client.Systemmessage(message);
									
								}

						case "USERNAME":
							client.changeUsername(input[1]);
						case "LOBBY":
							switch (input[1]) {
							case "ADD":
								client.lobbyAdded(input[2]);
								break;
							case "PERMISSION":
									client.lobbyPermission(input[2], true);
									break;
							}
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
									client.Systemmessage(sysmessage);
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
									client.addUser(input[2]);
									} catch (Exception ArrayIndexOutOfBoundsException) {
										
									}
									
								case "REMOVEUSER":
									try {
									//client.removeUser(input[2]);
									} catch (Exception ArrayIndexOutOfBoundsException) {
										
									}
								default:
									
									break;
							}
						default:
							//System.out.println(s);
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
	
	
	

}
