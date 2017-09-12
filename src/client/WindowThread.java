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
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	
	
		

	

	private Client client;
	
	private static final String VERSION = "1.05";
	
	private static final int NUMBER_ROWS = 15;
	
	private JPanel JPANEL_Ping = new JPanel();
	private JPanel JPANEL_Input = new JPanel();
	private JPanel JPANEL_Show = new JPanel();
	
	private JPanel JPANEL_MAIN = new JPanel();
	
	private JEditorPane JEDITORPANE_Chat = new JEditorPane();
	
	private JTextField JTEXTFIELD_Input = new JTextField();
	private JLabel JLabel_PingText = new JLabel();
	
	private JList lobbyList;
	private JList userList;
	
	private JScrollPane editorScrollPane;
	
	String[] dataLobby;
	 
	
	private final String[] styleJT = {"<html>","</html>"};
	private ArrayList<String> chatText = new ArrayList<String>();
	
	JFrame frame = null;
	
	
	public WindowThread(Client c) {
		
		//JPANEL_MAIN.setLayout(new GridLayout(10,1,10,1));
		
		this.client = c;
		
		frame = new JFrame("CICTC Version" + VERSION);
		frame.setSize(new Dimension(500,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame.setLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		GridLayout gl = new GridLayout(1,10);
		
		JPANEL_MAIN.add(JPANEL_Show, gl);   
		
		//JPANEL_Show.setLayout(new GridLayout(1,1));
		//JPANEL_Input.setLayout(new GridLayout(1,1));
    
	    constraints.weighty = 0;
	    constraints.gridheight = 8;
	    constraints.gridwidth = 1;
	    constraints.gridy = 0;
	    constraints.gridx = 0;
	    JPANEL_MAIN.add(JPANEL_Show, constraints);     

	    constraints.weighty = 0;
	    constraints.gridheight = 2;
	    constraints.gridwidth = 1;
	    constraints.gridy = 8;
	    constraints.gridx = 0;
	    JPANEL_MAIN.add(JPANEL_Input, constraints);
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPANEL_Input.setBorder(blackline);
		JPANEL_Show.setBorder(blackline);
		
		
		JTEXTFIELD_Input.setText("Eine normale Nachricht zum nicht verr�ckt werden");
		JTEXTFIELD_Input.setPreferredSize(JTEXTFIELD_Input.getPreferredSize());
		JTEXTFIELD_Input.setText("");
	     
		JEDITORPANE_Chat.setContentType("text/html");
		
		dataLobby = new String[NUMBER_ROWS];
		String s = "";
		for (int i = 0;i<NUMBER_ROWS;i++) {
			s = s + "<br>sasdadsdas dsasdasdasdas sdaadsdsadsddasdsaaddsa sdaasdadsadsads";
		}
		
		JEDITORPANE_Chat.setText(s);
		lobbyList = new JList(dataLobby); 
		JEDITORPANE_Chat.setPreferredSize(JEDITORPANE_Chat.getPreferredSize());
		lobbyList.setPreferredSize(new Dimension(100, JEDITORPANE_Chat.getPreferredSize().height));
		JEDITORPANE_Chat.setText("");
		
		String[] dataUsers = new String[NUMBER_ROWS];
		dataUsers[1] = "Mathias";
		dataUsers[0] = "Kevin";
		userList = new JList(dataUsers);
		userList.setPreferredSize(new Dimension(100, JEDITORPANE_Chat.getPreferredSize().height));

		
		lobbyList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		lobbyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		lobbyList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(lobbyList);
		
		JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT );
		tabpane.addTab("Lobby", lobbyList);
		tabpane.addTab("Users", userList);
		
	      
	      
		  editorScrollPane = new JScrollPane(JEDITORPANE_Chat);
		  editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
	      addLobbyButton.setPreferredSize(new Dimension(80,50));
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
	      //JPANEL_Input.setPreferredSize(JPANEL_Input.getPreferredSize());
	      JPANEL_Ping.add(JLabel_PingText);
	      JPANEL_Show.add(listScroller);
	      JPANEL_Show.add(tabpane);
	      JPANEL_Show.add(addLobbyButton);
	      JPANEL_Show.add(editorScrollPane);
	      
	      	
	    
	    frame.add(JPANEL_MAIN);
	    
	    Color col = new Color(255, 204, 0);
	    
	    
	    

	    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher(this));
	    //frame.setPreferredSize(new Dimension(500, 600));    
	    frame.setVisible(true);
		frame.pack();
		frame.setFocusable(true);
		frame.setVisible(true);
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
				this.receivedChatMessage("View cleaned up!");
				JTEXTFIELD_Input.setText("");
			} else {
				if (checkForHtml(JTEXTFIELD_Input.getText())) {
				client.sendMessageToServer("CHAT "+JTEXTFIELD_Input.getText());
	        	this.receivedChatMessage("You: "+JTEXTFIELD_Input.getText());
	        	JTEXTFIELD_Input.setText("");
				} else {
					this.receivedChatMessage("<font color=\"red\">GRRRRRR! HTML Tags werden hier nicht gedulded!</font>");
					JTEXTFIELD_Input.setText("");
				}
			}
        	
        	}
		
		
	}

	public void receivedChatMessage(String message) {
		
		String hue = "";
		chatText.add(message);
		Iterator<String> userIterator = chatText.iterator();
		while (userIterator.hasNext()) {
			String user = userIterator.next();
			hue = hue + "<br>" + user;
		}
		JEDITORPANE_Chat.setText( hue );
		JEDITORPANE_Chat.setContentType("text/html");
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
			for (int i = 0; i<dataLobby.length;i++) {
				if (dataLobby[i].equals("") || dataLobby[i] == null) {
					dataLobby[i] = string;
					break;
				}
			}
			
		}
		
	
	

	
}





