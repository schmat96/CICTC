package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class clientThread extends Thread {
	
	private Client client;
	private InputStream inp = null;
	private BufferedReader brinp = null;
	private DataOutputStream out = null;

	private Boolean isRunning = true;
	
	private long lastTimeSend;

	
	private static final int SYSTEM_POPUP_COSTS = 20;
	private static final int COINS_MS = 300000;
	private Long Coins_sys_ms;
	public boolean terminateClient;
	private String IP;

	
	Socket socket = null;
	Socket screenShotSocket = null;
    private Server server = null;
    
    public String getIP() {
		return IP;
	}
    
	public clientThread(Socket s, Server server, database db) {
			isRunning = false;
		   client = new Client(s.getInetAddress(), db);
		   terminateClient = client.clientAlreadyOpen;
		   IP = s.getInetAddress().toString().substring(1);
		   this.socket = s;
		   lastTimeSend = System.currentTimeMillis();
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
		        	server.closeClientThread(this);
		        }
		        String line;
		       
		            try {
		                line = brinp.readLine();
		            } catch (IOException e) {
		            		server.closeClientThread(this);
		                return;
		            }
		                
		                	
		                	System.out.println("Received: "+line);
		                	
		                
		                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
		                	server.closeClientThread(this);
		                    try {
								socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                    isRunning = false;
		                    return;
		                } else {
		                	String[] input = line.split("\\s+");
		                	switch (input[0]) {
		                		case "SYSTEM":
		                			switch (input[1]) {
			                			case "GETINFOS":
			                				server.sendInfos(this.client);
			                				break;
			                			case "SCREENSHOT":
			                				this.server.sendReadyScreenshot(this.getClient().getID(), Integer.parseInt(input[2]), Integer.parseInt(input[3]));
				                			break;
		                			}
	                			break;
		                		case "USERNAME":           			
		                			String username = this.server.checkUsername(input[1]);
		                			this.client.setUsername(username);
		                			this.sendMessage("USERNAME "+username);
		                			server.messageToAllExcpectSender("SYSTEM ADDUSER " + this.getClient().getUsername()+ " " +this.getClient().getID(), this.getClient().getID());
		                			break;
		                		case "PING":
		                			server.messageToAll(line);
		                			break;
		                		case "LOBBY":
		                			switch (input[1].replaceAll(" ", "")) {
			                			case "ADD": 
			                				try {
			                					this.server.addLobby(input[2], this.client, input[3]);
		                		
			                				} catch (DoubleLobbyNameExcpection e) {
			                					this.sendMessage("SYSTEM MESSAGE Diese Lobby ist schon vorhanden!");
			                					return;
			                				} catch (permissionExcpection e) {
			                					this.sendMessage("SYSTEM MESSAGE Du hast keine Rechte um dies zu machen! Frage den Admin um eine Lobby zu erstellen");
			                					return;
			                				}
			                				
		                					break;
		                				case "JOIN":   
		                					this.server.addClientToLobby(input[2], this.client, "");
		                					break;
		                				case "LEAVE":   
		                					//this.server.removeClientFromLobby(this);
		                					break;
		                				case "PERMISSION" :
		                					if (this.server.LobbyPermission(Integer.parseInt(input[2]), input[3], this.getName())) {
		                						this.sendMessage("LOBBY PERMISSION " + input[2]);
		                					} else {
			                					this.sendMessage("SYSTEM MESSAGE Das Passwort war leider falsch.");
		                					}
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
		                				case "WHISPER":   
		                					int id = Integer.parseInt(input[2]);
		                					String st = "CHAT WHISPER "+ this.getClient().getID()+ " " + this.client.getUsername() + " ";
		                					String msg = "CHAT WHISPER  "+ id+ " You:";
				                			for (int i = 3;i<input.length;i++) {
				                				st = st + " " + input[i];
				                				msg = msg + " " + input[i];
				                			}
				                			server.messageToSpecified(st, id);
				                			this.sendMessage(msg);
		                					break;
		                				case "/random":   
		                					try {
		                						this.random(input[2]);
		                					} catch (Exception ArrayIndexOutOfBoundsException) {
		                						this.sendMessage("SYSTEM MESSAGE Du musst noch etwas wetten!");
		                						return;
		                					}		
		                					break;
		                				case "LOBBY":   
		                					String mes = "CHAT LOBBY " + input[2] + " " +this.client.getUsername()+": ";
		                					String mes2 = "CHAT LOBBY " + input[2] + " You: ";
		                					String zw = "";
		                					for (int i = 3;i<input.length;i++) {
		                						zw = zw + " " + input[i];
				                			}
		                					mes = mes + zw;
		                					mes2 = mes2 + zw;
				                			server.messageToAllExcpectSender(mes, this.client.getID());
				                			this.sendMessage(mes2);
		                					break;
		                				default:
		                					String s = "CHAT LOBBY System " + this.client.getUsername()+" :";
				                			for (int i = 1;i<input.length;i++) {
				                				s = s + " " + input[i];
				                			}
				                			server.messageToAllExcpectSender(s, this.client.getID());
		                					break;
		                			}
		                			
		                			break;
		            
		                		default:
		                	}	
		                	
		                	
		                }
		            
		       
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
            while (inp.read()!=-1) {
        		System.out.println("there was something in the pipee");
        }
            return;
            
        } catch (IOException e) {
			System.out.println("What there was an unexcpected Error");
			e.printStackTrace();
			return;
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
				this.sendMessage("SYSTEM MESSAGE Vielleicht solltest du arbeiten anstatt mit deinem Glück zu spielen. Du verlierst deinen Einsatz.");
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
			server.messageToAllExcpectSender("SYSTEM POPUP "+s, this.client.getID());
		} else {
			sendMessage("SYSTEM MESSAGE Nicht genug coins! Du brauchst "+SYSTEM_POPUP_COSTS+" coins.");
		}
		
		
	}

	public void sendMessage(String string) {
		try {
			Boolean letServerKnow = true;
    			while(50 > System.currentTimeMillis()-this.lastTimeSend) {
    				if (letServerKnow) {
    					System.out.println("Waiting cause ya server cant handle it");
    					letServerKnow = false;
    				}
    				
    			}
    			lastTimeSend = System.currentTimeMillis();
			out.writeBytes(string + "\n\r");
			System.out.println("Sending to :"+this.getClient().getID()+" "+string);
			
			if ((int) (Math.random()*10)==1 && newCoin()) {
				client.earnedCoin(10);
				out.writeBytes("SYSTEM MESSAGE Gratulation. Du hast dir einen Coin verdient. Neuer Coin count: "+ client.getCoins() + "\n\r");
			}
			
		} catch (IOException e) {
			this.server.closeClientThread(this);
			
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
