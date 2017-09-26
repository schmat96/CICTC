package client;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;



public class screenShotTaker implements KeyListener {
	
	private JFrame frame;
	private Boolean isRunning = true;
	private Long againMS = System.currentTimeMillis();
	private BufferedImage screenShotImage = null;
	private JLabel screenHolder;
	private WindowThread WT;
	
	
	
	public screenShotTaker(WindowThread WT) {
		this.WT = WT;
		frame = new JFrame("Screenshottaker");
		frame.setSize(new Dimension(500,600));
		screenHolder = new JLabel();
		screenHolder.setPreferredSize(new Dimension(300,300));
		frame.setLocation(100, 100);
		frame.add(screenHolder);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		frame.setFocusable(true);
		frame.addKeyListener(this);
		
		screenHolder.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        if (evt.getClickCount() == 2) {
		        	int x = frame.getLocationOnScreen().x;
	        		int y = frame.getLocationOnScreen().y;
	        		int height = frame.getBounds().height;
	        		int width = frame.getBounds().width;        		
	        		frame.setVisible(false);
	        		frame.setSize(new Dimension(0,0));
					 try {
						screenShotImage = new Robot().createScreenCapture(new Rectangle(x,y, width, height));
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 //screenHolder.setVisible(true);
					 frame.setLocation(x, y);
		        		frame.setVisible(true);
					 ImageIcon imageIcon = new ImageIcon(screenShotImage);
					 screenHolder.setIcon(imageIcon);
					 
					 frame.setSize(new Dimension(width,height));
		        	
		        }
		       
		    }
		});
		
	}



	public void close() {
		frame.dispose();
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		WT.setScreenShotImage(this.screenShotImage);
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
