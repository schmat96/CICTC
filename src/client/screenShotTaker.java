package client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class screenShotTaker implements KeyListener {
	
	//private static final long WAITUNTILUPDATEMS = 1000;
	private JFrame frame;
	private BufferedImage screenShotImage = null;
	private JLabel screenHolder;
	private WindowThread WT;
	
	//private long onLastChangeMS;
	
	
	public screenShotTaker(WindowThread WT) {
		this.WT = WT;
		//onLastChangeMS = System.currentTimeMillis();
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
		
		frame.addComponentListener( new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				screenShoter(false);
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				screenShoter(false);
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				screenShoter(false);
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				screenShoter(false);
				
			}
 
        } );
		
		screenHolder.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        if (evt.getClickCount() == 2) {
		        	screenShoter(true);
		        	
		        }
		       
		    }
		});
		
	}



	protected void screenShoter(boolean requested) {
		/*
		Boolean going = false;
		if (requested==false) {
			if (onLastChangeMS<System.currentTimeMillis()+this.WAITUNTILUPDATEMS) {
				onLastChangeMS = System.currentTimeMillis();
			} else {
				going = true;
			}
		} else {
			going = true;
		}
		*/
		if (requested) {
    		int x = frame.getLocationOnScreen().x;
		int y = frame.getLocationOnScreen().y;
		int height = frame.getBounds().height;
		int width = frame.getBounds().width;        		
		frame.setVisible(false);
//		frame.setLocation(1000, 1000);
		frame.setSize(new Dimension(0,0));
		frame.remove(screenHolder);
		long time = System.currentTimeMillis();
		System.out.println("Da Windows einfach schlechter ist als Mac muss ich hier 0.5secs warten");
		while(time + 500 > System.currentTimeMillis()) {
			
		}
		 try {
			screenShotImage = new Robot().createScreenCapture(new Rectangle(x+10,y+11, width, height));
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
		 frame.add(screenHolder);
		}
		
	}



	protected BufferedImage makeImageOpaque(BufferedImage screenShotImage2) {
		
		
		
		BufferedImage image = screenShotImage2;
		return image;
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
