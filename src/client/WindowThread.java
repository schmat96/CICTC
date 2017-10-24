					package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class WindowThread {
	
	@SuppressWarnings("unused")
	private BufferedImage screenShotImage = null;
		

	private ArrayList<userList> userList = new ArrayList<userList>();
	DefaultListModel<String> userListModel = new DefaultListModel<>();
	private ArrayList<lobbyList> lobbyList = new ArrayList<lobbyList>();
	DefaultListModel<String> lobbyListModel = new DefaultListModel<>();

	private Client client;
	
	private static final Color RECEIVED_NEW_MESSAGE_BACKGROUND = new Color(204,255,153);
	
	private static final String VERSION = "2.00";
	
	private static final int NUMBER_ROWS = 15;
	private static int lastSelectedUser = 0;
	private static int lastSelectedLobby = 0;
	
	private JPanel JPANEL_Ping = new JPanel();
	private JPanel JPANEL_Input = new JPanel();
	private JPanel JPANEL_Show = new JPanel();
	private JPanel JPANEL_Lobby = new JPanel();
	
	private JPanel JPANEL_MAIN = new JPanel();
	
	private JEditorPane JEDITORPANE_Chat = new JEditorPane();
	
	private JTextField JTEXTFIELD_Input = new JTextField();
	private JLabel JLabel_PingText = new JLabel();
	
	@SuppressWarnings("rawtypes")
	private JList JList_lobbyList;
	String[] dataUsers;
	@SuppressWarnings("rawtypes")
	private JList JList_userList;
	
	private JScrollPane editorScrollPane;
	
	private JTabbedPane tabpane;
	
	JFrame frame = null;
	private screenShotTaker screenShotWindow;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WindowThread(Client c) {
		
		//JPANEL_MAIN.setLayout(new GridLayout(10,1,10,1));
		
		this.client = c;
		
		frame = new JFrame("CICTC Version" + VERSION);
		frame.setSize(new Dimension(500,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setLocation(2000, 100);
		
		//frame.setLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		JPANEL_MAIN.setLayout(new GridBagLayout());
		
		//JPANEL_Show.setLayout(new GridLayout(1,1));
		//JPANEL_Input.setLayout(new GridLayout(1,1));
		JPANEL_Lobby.setLayout(new BoxLayout(JPANEL_Lobby, BoxLayout.PAGE_AXIS));
    
	   
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPANEL_Input.setBorder(blackline);
		JPANEL_Show.setBorder(blackline);
		JPANEL_Lobby.setBorder(blackline);
		
		constraints.fill = GridBagConstraints.BOTH;
		    
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		    constraints.gridheight = 8;
		    constraints.gridwidth = 9;
		    constraints.gridy = 0;
		    constraints.gridx = 1;
		    constraints.anchor = GridBagConstraints.NORTHWEST;
		    JPANEL_MAIN.add(JPANEL_Show, constraints);     

		    constraints.weightx = 1;
			constraints.weighty = 1;
		    constraints.gridheight = 1;
		    constraints.gridwidth = 10;
		    constraints.gridy = 8;
		    constraints.gridx = 0;
		    JPANEL_MAIN.add(JPANEL_Input, constraints);
		    
		    constraints.weightx = 0;
			constraints.weighty = 1;
		    constraints.gridheight = 8;
		    constraints.gridwidth = 1;
		    constraints.gridy = 0;
		    constraints.gridx = 0;
		    JPANEL_MAIN.add(JPANEL_Lobby, constraints);   
		    
		
		
		JTEXTFIELD_Input.setText("Eine normale Nachricht zum nicht verr�ckt werden");
		JTEXTFIELD_Input.setPreferredSize(JTEXTFIELD_Input.getPreferredSize());
		JTEXTFIELD_Input.setText("");
	     
		JEDITORPANE_Chat.setContentType("text/html");
		

		String s = "";
		for (int i = 0;i<NUMBER_ROWS;i++) {
			s = s + "<br>sasdadsdas dsasdasdasdas sdaadsdsadsddasdsaaddsa";
		}
		
		JEDITORPANE_Chat.setText(s);
		JList_lobbyList = new JList(lobbyListModel); 
		JEDITORPANE_Chat.setPreferredSize(JEDITORPANE_Chat.getPreferredSize());
		JList_lobbyList.setPreferredSize(new Dimension(100, JEDITORPANE_Chat.getPreferredSize().height-100));
		JEDITORPANE_Chat.setText("");
		
		JList_lobbyList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		        	
		            // Double-click detected
		            int index = list.locationToIndex(evt.getPoint());
		            lastSelectedLobby = index;
		            reloadChatText();
		            changeShowView(list.getSelectedValue());
		        } else if (evt.getClickCount() == 3) {

		        }
		    }
		});
		
		JList_userList = new JList(userListModel);
		JList_userList.setPreferredSize(new Dimension(100, JEDITORPANE_Chat.getPreferredSize().height-100));
		
		JList_userList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		            int index = list.locationToIndex(evt.getPoint());
		            lastSelectedUser = index;
		            reloadChatText();
		            changeShowView(list.getSelectedValue());
		        } 
		    }
		});
		
		JList_lobbyList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JList_lobbyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		JList_lobbyList.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(JList_lobbyList);
		
		tabpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT );
		tabpane.addTab("Lobby", JList_lobbyList);
		tabpane.addTab("Users", JList_userList);
		
		this.addLobby("System", 0000);
		lobbyList l = new lobbyList("System", 0000);
		this.lobbyList.add(l);
		
	      
	      
		  editorScrollPane = new JScrollPane(JEDITORPANE_Chat);
		  editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		  //editorScrollPane.setPreferredSize(JPANEL_Show.getPreferredSize());
		  
		  JButton jb = new JButton("send");
	      jb.setPreferredSize(new Dimension(80,50));
	      jb.setToolTipText("useful commands: /coins ; /popup [message] ; /w [name] ; /random [bet]");
	      jb.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	sendMessage(); 	
	        }
	      });
	      
	      JButton addLobbyButton = new JButton("Update");
	      addLobbyButton.setMinimumSize(new Dimension(80,50));
	      addLobbyButton.setToolTipText("Wush Update");
	      addLobbyButton.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	onLoad();
	        }
	      });
	      
	      JButton openScreenShot = new JButton("ScreenShot");
	      openScreenShot.setMinimumSize(new Dimension(80,50));
	      openScreenShot.setToolTipText("make a Screenshot and send it!");
	      openScreenShot.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	openScreenShot();
	        }
	      });
	      
	      JPANEL_Input.add(JTEXTFIELD_Input);
	      JPANEL_Input.add(jb);
	      JPANEL_Input.setPreferredSize(JPANEL_Input.getPreferredSize());
	      JPANEL_Ping.add(JLabel_PingText);
	      JPANEL_Lobby.add(listScroller);
	      JPANEL_Lobby.add(tabpane);
	      JPANEL_Lobby.add(openScreenShot);
	      JPANEL_Lobby.add(addLobbyButton);
	      JPANEL_Show.add(editorScrollPane);
	      
	     
		    
	      	
	    
	    frame.add(JPANEL_MAIN);
	    
	    
	    
	    

	    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher(this));
	    //frame.setPreferredSize(new Dimension(500, 600));    
	    frame.setVisible(true);
		frame.pack();
		frame.setFocusable(true);
		frame.setVisible(true);
		
		onLoad();
	}

	private void openScreenShot() {
		if (screenShotWindow == null) {
			this.screenShotWindow = new screenShotTaker(this);
		}
		
	}

	private void onLoad() {
		client.sendMessageToServer("SYSTEM GETINFOS");
	}

	protected void changeShowView(Object selectedValue) {
		
		
	}

	protected void addLobby() {
		    JPanel panel = new JPanel(new BorderLayout(5, 5));

		    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
		    label.add(new JLabel("Name", SwingConstants.RIGHT));
		    label.add(new JLabel("Passwort", SwingConstants.RIGHT));
		    panel.add(label, BorderLayout.WEST);

		    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
		    JTextField username = new JTextField();
		    controls.add(username);
		    JPasswordField password = new JPasswordField();
		    controls.add(password);
		    panel.add(controls, BorderLayout.CENTER);

		    JOptionPane.showMessageDialog(frame, panel, "Add Lobby", JOptionPane.QUESTION_MESSAGE);
		
		client.sendMessageToServer("LOBBY ADD "+ username.getText() + " " + new String(password.getPassword()));
		
		
	}
	
	protected void sendMessage() {
		
		String textfield = JTEXTFIELD_Input.getText();
		if (textfield.equals("")) {
		} else {
			if (textfield.equals("clean")) {
				this.receivedChatMessage("View cleaned up!", 0000);
				JTEXTFIELD_Input.setText("");
			} else {
				if (checkForHtml(textfield)) {
				
				if (textfield.startsWith("/")) {
					switch(textfield.split("\\s")[0]) {
						case ("/coins"):
							client.sendMessageToServer("CHAT /coins ");
							break;
						case ("/popup"):
							client.sendMessageToServer("CHAT " + textfield);
							break;
						case ("/random"):
							client.sendMessageToServer("CHAT " + textfield);
							break;
						case ("/w"):
							this.sendWhisperMessage(textfield);
							textfield = "/w "+textfield.split("\\s")[1];
							break;
						case ("/users"):
							Iterator<userList> userIterator2 = userList.iterator();
							while (userIterator2.hasNext()) {
								userList user = userIterator2.next();
								this.receivedChatMessage(user.getID() + ":"+user.getName(), 0000);
							}
							
							
							break;
						case ("/lobby"):
							addLobby();
							break;
						default:
							this.receivedChatMessage("Ich kenne dieses Kommando nicht!", 0000);
							break;
							
						}
					} else {
				
					
				if (tabpane.getSelectedIndex()==1) {
					int ID = currentSelectedItemChat();
					if (ID == 0000) {
						this.receivedWhisperMessage("System: Bitte wähle zuerst an wen die Nachricht gehen soll!", ID);
					} else {
					client.sendMessageToServer("CHAT WHISPER " + ID +" "+JTEXTFIELD_Input.getText());
					//this.receivedWhisperMessage("You: " + textfield, ID);
					}
				} else {
					int ID = currentSelectedItemLobby();
					if (ID == 0000) {
						this.receivedWhisperMessage("System: Bitte wähle zuerst an wen die Nachricht gehen soll!", ID);
					} else {
					client.sendMessageToServer("CHAT LOBBY " + ID  +" "+JTEXTFIELD_Input.getText());
					//this.receivedChatMessage("You: " + textfield, ID);
					}
				}
	        	JTEXTFIELD_Input.setText("");
					}
				} else {
					this.receivedChatMessage("<font color=\"red\">GRRRRRR! HTML Tags werden hier nicht gedulded!</font>", 0000);
					JTEXTFIELD_Input.setText("");
				}
			}
        	
        	}
		
		
	}

	private void sendWhisperMessage(String string) {
		String message = "";
		String[] hue = string.split("\\s");
		for (int i = 2;i<hue.length;i++) {
			message = message + " " + hue[i];
		}
		
		
		int id = 0;
		try {
			id = Integer.parseInt(hue[1]);
		} catch (NumberFormatException e) {
			id = 0;
		}
		if (id == 0) {
			Iterator<userList> userIterator2 = userList.iterator();
			while (userIterator2.hasNext()) {
				userList user = userIterator2.next();
				if (user.getName().equals(hue[1])) {
					this.client.sendMessageToServer("CHAT WHISPER " + user.getID() + " " + message);
				}
			}
		} else {
			this.client.sendMessageToServer("CHAT WHISPER " + id + " " + message);
		}
		
	}

	private int currentSelectedItemLobby() {
		Iterator<lobbyList> userIterator = lobbyList.iterator();
		while (userIterator.hasNext()) {
			lobbyList lobby = userIterator.next();
			if (lobby.getName().equalsIgnoreCase(this.currentSelected())) {
				return lobby.getID();
			}
			
		}
		return 0000;
	}

	private int currentSelectedItemChat() {
		Iterator<userList> userIterator = userList.iterator();
		while (userIterator.hasNext()) {
			userList lobby = userIterator.next();
			if (lobby.getName().equalsIgnoreCase(this.currentSelected())) {
				return lobby.getID();
			}
			
		}
		return 0000;
	}

	private String currentSelected() {
		if (tabpane.getSelectedIndex()==1) {
			return this.userListModel.getElementAt(lastSelectedUser);
		} else {
			return this.lobbyListModel.getElementAt(lastSelectedLobby);
		}
		
	}

	public void receivedChatMessage(String message, int ID) {
		Boolean found = false;
		Iterator<lobbyList> userIterator = lobbyList.iterator();
		while (userIterator.hasNext()) {
			lobbyList lobby = userIterator.next();
			if (lobby.getID() == ID) {
				found = true;
				lobby.addText(message);
				lobby.AddNewMessageCount(1);
			}
		}
		
		if (found == false) {
			System.out.println("was not able to resolve lobby");
			getLobbySaveMessageInTrace(message, ID);
			
		}
		//ADD CALL ONLY IF TAB IS ON THAT LOBBY
		reloadChatText();
	}

	public void receivedWhisperMessage(String message, int ID) {
		Boolean found = false;
		Iterator<userList> userIterator = userList.iterator();
		while (userIterator.hasNext()) {
			userList user = userIterator.next();
			if (user.getID() == ID) {
				user.addText(message);
				for (int i = 0; i<userListModel.getSize();i++) {
					if (userListModel.get(i).equalsIgnoreCase(user.getName())) {
						int indexBackUp = JList_userList.getSelectedIndex();
						JList_userList.setSelectedIndex(i);
						JList_userList.setSelectionBackground(RECEIVED_NEW_MESSAGE_BACKGROUND);
						JList_userList.setSelectedIndex(indexBackUp);
					}
				}
				
				found = true;
			}
		}
		
		if (found == false) {
			System.out.println("was not able to resolve lobby");
			getLobbySaveMessageInTrace(message, ID);
			
		}
		//ADD CALL ONLY IF TAB IS ON THAT LOBBY
		reloadChatText();
	}

	private void getLobbySaveMessageInTrace(String message, int iD) {
		System.out.println("Had to save message in Trace!");
		
	}

	private void reloadChatText() {
		String hue = "";
		if (tabpane.getSelectedIndex()==1) {
			String s = "";
			try {
			s = this.userListModel.getElementAt(lastSelectedUser);
			} catch (Exception e) {
				return;
			}
			Iterator<userList> userIterator = userList.iterator();
			while (userIterator.hasNext()) {
				userList user = userIterator.next();
				if (user.getName().equalsIgnoreCase(s)) {
					hue = user.getConversation();
				}
			}
		} else {
			String s = "";
			try {
			s = this.lobbyListModel.getElementAt(lastSelectedLobby);
			} catch (Exception e) {
				return;
			}
			Iterator<lobbyList> userIterator = lobbyList.iterator();
			while (userIterator.hasNext()) {
				lobbyList lobby = userIterator.next();
				if (lobby.getName().equalsIgnoreCase(s)) {
					if (lobby.getPermission()) {
					hue = lobby.getConversation();
					} else {
						askServerForPermission(lobby);
					}
				}
			}
		}
		
		JEDITORPANE_Chat.setText( hue );
		this.JEDITORPANE_Chat.validate();
		scrollDown();
	}

	private void askServerForPermission(client.lobbyList lobby) {
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Password", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.WEST);
	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    JPasswordField password = new JPasswordField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);
	    JOptionPane.showMessageDialog(frame, panel, "login", JOptionPane.QUESTION_MESSAGE);
	    client.sendMessageToServer("LOBBY PERMISSION " + lobby.getID()+" " + new String(password.getPassword()));
	}

	public static Boolean checkForHtml(String html) {
		if (html.matches("\\<.*?>")) {
			return false;
		} else {
			return true;
		}
	}


	public void updatePing(String pingTook) {
		//JLabel_PingText.setText("Ping: "+ pingTook +"MS");
		
	}
	
	    private class MyDispatcher implements KeyEventDispatcher {
	    	
	    	private WindowThread wt;
	    	
	    	public MyDispatcher(WindowThread windowThread) {
	    		this.wt = windowThread;
			}
	    	
	    	
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	            if (e.getID() == KeyEvent.KEY_PRESSED) {
	            	 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		                	wt.sendMessage();
		                }
	            }
	            return false;
	        }
	    }

		private void scrollDown() {
			this.JPANEL_Show.validate();
			JScrollBar vertical = this.editorScrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		}

		public void popUpMessage(String message) {
			JOptionPane.showMessageDialog(frame, message);	
		}
		
		public void addUser(String name, int id) {
			Boolean adding = true;
			Iterator<userList> userIterator = userList.iterator();
			while (userIterator.hasNext()) {
				userList user = userIterator.next();
				if (user.getID()==id) {
					adding = false;
				}
			}
			
			if (adding) {
				userListModel.addElement(name);
				userList e = new userList(name, id);
				this.userList.add(e);
			}
					

			}
			
		
		public void addLobby(String name, int id) {
			Boolean adding = true;
			Iterator<lobbyList> userIterator = lobbyList.iterator();
			while (userIterator.hasNext()) {
				lobbyList user = userIterator.next();
				if (user.getID() == id) {
					adding = false;
				}
			}
			if (adding) {
				lobbyListModel.addElement(name);
				lobbyList e = new lobbyList(name, id);
				this.lobbyList.add(e);
			} else {
				System.out.println("nothing to add");
			}
		}
		
		public void removeLobby(String name, int id) {
			lobbyListModel.removeElement(name);
			Iterator<lobbyList> userIterator = lobbyList.iterator();
			while (userIterator.hasNext()) {
				lobbyList user = userIterator.next();
				if (user.getName().equalsIgnoreCase(name)) {
					lobbyList.remove(user);
				}
			}
		}
		
		public void removeUser(int id) {
			
			Iterator<userList> userIterator = userList.iterator();
			while (userIterator.hasNext()) {
				userList user = userIterator.next();
				if (user.getID()==id) {
					userListModel.removeElement(user.getName());
					userList.remove(user);
				}
			}
		}

		public void setPermission(int i, Boolean perm) {
			Iterator<lobbyList> userIterator = lobbyList.iterator();
			while (userIterator.hasNext()) {
				lobbyList user = userIterator.next();
				if (user.getID() == i) {
					user.setPermission(perm);
				}
			}
			
		}

		public void setScreenShotImage(BufferedImage screenShotImage2) {
			this.screenShotImage = screenShotImage2;
			this.screenShotWindow.close();
			this.screenShotWindow = null;
			
			if (this.userList.size()==0) {
				this.receivedChatMessage("Du hast keine Freunde denen du Screenshots senden könntest!", 0000);
			} else {
			String[] choices = new String[this.userList.size()];
			int i = 0;
			Iterator<userList> userIterator = userList.iterator();
			while (userIterator.hasNext()) {
				userList user = userIterator.next();
				choices[i] = user.getName();
				i++;
			}
			
			
			
			
			String toWhom = askUser(choices);
			
			int idtoWhom = 0;
			
			Iterator<userList> userIterator2 = userList.iterator();
			while (userIterator2.hasNext()) {
				userList user = userIterator2.next();
				if (user.getName().equals(toWhom)) {
					idtoWhom = user.getID();
				}
			}
			
			
			client.setScreenShot(screenShotImage2, idtoWhom+"");
			}
		}
		
		public String askUser(String[] choices) {
	        String s = (String) JOptionPane.showInputDialog(
	                null,
	                "Wem willst du das Bild senden?",
	                "Wem willst du das Bild senden?",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                choices,
	                choices[0]);
	        return s;
	    }
		
	
	

	
}





