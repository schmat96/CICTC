package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class screenShotListener extends Thread {
	
	private InputStream inp = null;

	private Boolean isRunning = true;

	
	ServerSocket serversocket = null;
	Socket socket = null;
    
	public screenShotListener(ServerSocket s) {
		   this.serversocket = s;
		   this.run();
	}
	
	public void run() {
		while(isRunning) {
			if (socket==null) {
				try {
					socket = serversocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
			 try {
		            inp = socket.getInputStream();
		        } catch (IOException e) {
		            return;
		        }
		       
		            receivedScreenShot(inp);
		            
			}
		}
	}


	
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
            this.isRunning = false;
            this.socket.close();
            this.serversocket.close();
            return;
            
        } catch (IOException e) {
			System.out.println("There was an unexcpected Error");
			e.printStackTrace();
			this.isRunning = false;
			return;
		}
        
        
        

        
		
	}
	
	
	

}
