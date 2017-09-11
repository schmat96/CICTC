package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class clientThread extends Thread {
	
	private Client client;
	private InputStream inp = null;
	private BufferedReader brinp = null;
	private DataOutputStream out = null;
	private ServerSocket serverSocket = null;
	private Boolean isRunning = true;
	
	private final static long coinGain_MS = 10000;
	private static final int SYSTEM_POPUP_COSTS = 20;
	private static final int COINS_MS = 300000;
	private Long Coins_sys_ms;
	

   

	Socket socket = null;
    private Server server = null;

	public clientThread(Socket s, Server server, database db) {
		   client = new Client(s.getInetAddress(), db);
		   this.socket = s;
		   this.server = server;
		   Coins_sys_ms = System.currentTimeMillis();

	}
	
	public void run() {
		while(isRunning) {
			
				
			
			
			 try {
		            inp = socket.getInputStream();
		            brinp = new BufferedReader(new InputStreamReader(inp));
		            out = new DataOutputStream(socket.getOutputStream());
		        } catch (IOException e) {
		            return;
		        }
		        String line;
		       
		            try {
		                line = brinp.readLine();
		                System.out.println(line);
		                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
		                	server.closeClientThread(this);
		                    socket.close();
		                    isRunning = false;
		                    return;
		                } else {

		                	String[] input = line.split("\\s+");
		                	switch (input[0]) {
		                		case "USERNAME":           			
		                			String username = this.server.checkUsername(input[1]);
		                			this.client.setUsername(username);
		                			this.sendMessage("USERNAME "+username);
		                			break;
		                		case "PING":
		                			server.messageToAll(line);
		                			break;
		                		case "LOBBY":
		                			switch (input[1].replaceAll(" ", "")) {
			                			case "ADD":   
			                				try {
		                					this.server.addLobby(input[2], this.client);
			                				} catch (DoubleLobbyNameExcpection e) {
			                					this.sendMessage("SYSTEM MESSAGE Diese Lobby ist schon vorhanden!");
			                					return;
			                				}
		                					break;
		                				case "JOIN":   
		                					this.server.addClientToLobby(input[2], this.client);
		                					break;
		                				case "LEAVE":   
		                					this.server.removeClientFromLobby(input[2], this.client);
		                					break;
		                				default:
		                					break;
		                			}
		                			break;
		                		case "CHAT":
		                			switch (input[1].replaceAll(" ", "")) {
		                				case "/popup":   
		                					checkForPopUp(input);
		                					break;
		                				case "/coins":   
		                					this.sendMessage("SYSTEM MESSAGE Du hast "+this.client.getCoins() + " Coins!");
		                					break;
		                				case "/w":   
		                					String st = "CHAT <p><font color=\"green\">" + this.client.getUsername()+" hat dir gefl�stert: :";
				                			for (int i = 3;i<input.length;i++) {
				                				st = st + " " + input[i];
				                			}
				                			st = st + "</font></p>";
				                			server.messageToSpecifiedExcpectSender(st, input[2]);
		                					break;
		                				case "/random":   
		                					if (input.length >= 2) {
		                						this.random(input[2]);
		                					} else {
		                						this.sendMessage("SYSTEM MESSAGE Du musst noch etwas wetten!");
		                						return;
		                					}
		                					
		                					break;
		                				default:
		                					String s = "CHAT " + this.client.getUsername()+" :";
				                			for (int i = 1;i<input.length;i++) {
				                				s = s + " " + input[i];
				                			}
				                			server.messageToAllExcpectSender(s, this.client.getUsername());
		                					break;
		                			}
		                			
		                			break;
		                		default:
		                			
		                	}
		                	
		                }
		            } catch (IOException e) {
		                this.server.closeClientThread(this);
		                return;
		            }
		       
		}
	}

	private void random(String string) {
		int bet = 1;
		try {
			bet = Integer.parseInt(string);
		} catch (Exception e) {
			this.sendMessage("SYSTEM MESSAGE Du kannst keine Strings verwetten!");
			return;
		}
		if (bet<=0) {
			this.sendMessage("SYSTEM MESSAGE Du kannst nicht negativ wetten.");
			return;
		} else if (bet > client.getCoins()) {
			this.sendMessage("SYSTEM MESSAGE Nicht genug Coins vorhanden. Deine Coins: " + client.getCoins());
			return;
		} else {
			if (Math.random()*10>9) {
				this.sendMessage("SYSTEM MESSAGE Fortuna war mit dir. Dein Einsatz wurde versechsfacht. Oder so.");
				this.client.earnedCoin(bet*6);
			} else {
				this.sendMessage("SYSTEM MESSAGE Vielleicht solltest du arbeiten anstatt mit deinem Gl�ck zu spielen. Du verlierst deinen Einsatz.");
				this.client.earnedCoin(-bet);
			}
		}
		
		
	}

	private void checkForPopUp(String[] input) {
		if (client.getCoins()>=SYSTEM_POPUP_COSTS) {
			client.earnedCoin(-SYSTEM_POPUP_COSTS);
			String s = "";
			for (int i = 2;i<input.length;i++) {
				s = s + " " + input[i];
			}
			server.messageToAllExcpectSender("SYSTEM POPUP "+s, this.client.getUsername());
		} else {
			sendMessage("SYSTEM MESSAGE Nicht genug coins! Du brauchst "+SYSTEM_POPUP_COSTS+" coins.");
		}
		
		
	}

	public void sendMessage(String string) {
		try {
			out.writeBytes(string + "\n\r");
			
			if ((int) (Math.random()*10)==1 && newCoin()) {
				client.earnedCoin(10);
				out.writeBytes("SYSTEM MESSAGE Gratulation. Du hast dir einen Coin verdient. Neuer Coin count: "+ client.getCoins() + "\n\r");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	  private boolean newCoin() {
		if (System.currentTimeMillis() > COINS_MS + Coins_sys_ms) {
			Coins_sys_ms = System.currentTimeMillis();
			return true;
		}
		
		return false;
		
	}

	public Boolean getIsRunning() {
			return isRunning;
		}

		public void setIsRunning(Boolean isRunning) {
			this.isRunning = isRunning;
		}
		
		public Client getClient() {
			return this.client;
		}
		
	
	

}
