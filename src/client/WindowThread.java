					package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.text.html.HTML;

import server.clientThread;

public class WindowThread {
	
	
		

	private ArrayList<userList> userList = new ArrayList<userList>();
	DefaultListModel<String> userListModel = new DefaultListModel<>();
	private ArrayList<lobbyList> lobbyList = new ArrayList<lobbyList>();
	DefaultListModel<String> lobbyListModel = new DefaultListModel<>();

	private Client client;
	
	private static final String VERSION = "1.05";
	
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
	
	private JList JList_lobbyList;
	String[] dataUsers;
	private JList JList_userList;
	
	private JScrollPane editorScrollPane;
	 
	private ArrayList<String> chatText = new ArrayList<String>();
	
	private JTabbedPane tabpane;
	
	JFrame frame = null;
	
	
	public WindowThread(Client c) {
		
		//JPANEL_MAIN.setLayout(new GridLayout(10,1,10,1));
		
		this.client = c;
		
		frame = new JFrame("CICTC Version" + VERSION);
		frame.setSize(new Dimension(500,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		JList_userList = new JList(userListModel);
		JList_userList.setPreferredSize(new Dimension(100, JEDITORPANE_Chat.getPreferredSize().height-100));
		
		JList_userList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		            int index = list.locationToIndex(evt.getPoint());
		            lastSelectedUser = index;
		            changeShowView(list.getSelectedValue());
		        } else if (evt.getClickCount() == 3) {

		            int index = list.locationToIndex(evt.getPoint());
		            addUser("name", 3);
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
		
		this.addLobby("System", 0);
		
	      
	      
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
	      
	      JButton addLobbyButton = new JButton("add Lobby");
	      addLobbyButton.setMinimumSize(new Dimension(80,50));
	      addLobbyButton.setToolTipText("Add a new Lobby!");
	      addLobbyButton.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	addLobby(); 	
	        }
	      });
	      
	      JPANEL_Input.add(JTEXTFIELD_Input);
	      JPANEL_Input.add(jb);
	      JPANEL_Input.setPreferredSize(JPANEL_Input.getPreferredSize());
	      JPANEL_Ping.add(JLabel_PingText);
	      JPANEL_Lobby.add(listScroller);
	      JPANEL_Lobby.add(tabpane);
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
	}

	protected void changeShowView(Object selectedValue) {
		
		
	}

	protected void addLobby() {
		String name = "";
		while (true) {
			name = JOptionPane.showInputDialog(frame, "Name für die Lobby?");
			if (name.length() < 15) {
				break;
			}
		}
		
		client.sendMessageToServer("LOBBY ADD "+ name);
		
		
	}

	protected void sendMessage() {
		if (JTEXTFIELD_Input.getText().equals("")) {
		} else {
			if (JTEXTFIELD_Input.getText().equals("clean")) {
				chatText = new ArrayList<String>(); 
				this.receivedChatMessage("View cleaned up!", "System");
				JTEXTFIELD_Input.setText("");
			} else {
				if (checkForHtml(JTEXTFIELD_Input.getText())) {
				client.sendMessageToServer("CHAT "+JTEXTFIELD_Input.getText());
	        	this.receivedChatMessage("You: "+JTEXTFIELD_Input.getText(), "System");
	        	JTEXTFIELD_Input.setText("");
				} else {
					this.receivedChatMessage("<font color=\"red\">GRRRRRR! HTML Tags werden hier nicht gedulded!</font>", "System");
					JTEXTFIELD_Input.setText("");
				}
			}
        	
        	}
		
		
	}

	public void receivedChatMessage(String message, String lobbyName) {
		Iterator<lobbyList> userIterator = lobbyList.iterator();
		while (userIterator.hasNext()) {
			lobbyList user = userIterator.next();
			if (user.getName().equals(lobbyName)) {
				user.addText(message);
				JList_lobbyList.setSelectedIndex(0);
				JList_lobbyList.setSelectionBackground(Color.red);
			} else {
				System.out.println("was not able to resolve lobby");
			}
		}
		//ADD CALL ONLY IF TAB IS ON THAT LOBBY
		reloadChatText();
	}
	
	public void receivedWhisperMessage(String message, String name) {
		Iterator<userList> userIterator = userList.iterator();
		while (userIterator.hasNext()) {
			userList user = userIterator.next();
			if (user.getName() == name) {
				user.addText(message);
				
			} else {
				System.out.println("was not able to resolve user");
			}
		}
		//ADD CALL ONLY IF TAB IS ON THAT USER
		reloadChatText();
	}

	private void reloadChatText() {
		String hue = "";
		if (tabpane.getSelectedIndex()==1) {
			String s = this.userListModel.getElementAt(lastSelectedUser);
			Iterator<userList> userIterator = userList.iterator();
			while (userIterator.hasNext()) {
				userList user = userIterator.next();
				if (user.getName().equalsIgnoreCase(s)) {
					hue = user.getConversation();
				}
			}
		} else {
			String s = this.lobbyListModel.getElementAt(lastSelectedLobby);
			Iterator<lobbyList> userIterator = lobbyList.iterator();
			while (userIterator.hasNext()) {
				lobbyList user = userIterator.next();
				if (user.getName().equalsIgnoreCase(s)) {
					hue = user.getConversation();
				}
			}
		}
		
		JEDITORPANE_Chat.setText( hue );
		this.JEDITORPANE_Chat.validate();
		scrollDown();
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

		public void receivedSystemMessage(String shutdownMes) {
			String hue = "";
			chatText.add("<h3><b>System: "+shutdownMes+"</b></h3>");
			Iterator<String> userIterator = chatText.iterator();
			while (userIterator.hasNext()) {
				String user = userIterator.next();
				hue = hue + "<br>" + user;
			}
			JEDITORPANE_Chat.setText( hue );
			JEDITORPANE_Chat.setContentType("text/html");
			JScrollBar vertical = this.editorScrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
			scrollDown();
			
		}

		private void scrollDown() {
			this.JPANEL_Show.validate();
			JScrollBar vertical = this.editorScrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
			
		}

		public void popUpMessage(String message) {
			JOptionPane.showMessageDialog(frame, message);
			
		}

		public void lobbyAdded(String string) {
			this.receivedSystemMessage("Lobby added: "+string);
			/*
			for (int i = 0; i<dataLobby.length;i++) {
				if (dataLobby[i].equals("") || dataLobby[i] == null) {
					dataLobby[i] = string;
					break;
				}
			}
			*/
			
		}
		
		public void addUser(String name, int id) {
			userListModel.addElement(name);
			userList e = new userList(name, id);
			this.userList.add(e);
		}
		
		public void addLobby(String name, int id) {
			lobbyListModel.addElement(name);
			lobbyList e = new lobbyList(name, id);
			this.lobbyList.add(e);
		}
		
	
	

	
}





