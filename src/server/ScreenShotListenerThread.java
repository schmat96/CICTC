package server;

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
@SuppressWarnings("unused")
public class ScreenShotListenerThread extends Thread {
	
	private Boolean isRunning = true;
	private Socket socket;
	 private ServerSocket serverSocket;
     Socket server;
	private clientThread clientThread;
    
    
	public ScreenShotListenerThread(int port, clientThread ct) {
		isRunning = false;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   this.clientThread = ct;
		   
	}
	
	public void run() {
		while(isRunning) {
			if (socket == null) {
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				 try {
					 
					 System.out.println("received Screenshot!");
					 
				        InputStream inputStream = socket.getInputStream();

				        System.out.println("Reading: " + System.currentTimeMillis());

				        byte[] sizeAr = new byte[4];
				        inputStream.read(sizeAr);
				        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

				        byte[] imageAr = new byte[size];
				        inputStream.read(imageAr);

				        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

				        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
		                  JFrame frame1 = new JFrame();
		                  frame1.getContentPane().add(new JLabel(new ImageIcon(image)));
		                  frame1.pack();
		                  frame1.setVisible(true);      
		          
			        } catch (IOException e) {
			        	e.printStackTrace();
			        }
				 catch(Exception ex)
		            {
		                  System.out.println(ex);
		            }
				 try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 socket = null;
			}
		}
	}


	public Boolean getIsRunning() {
			return isRunning;
		}

		public void setIsRunning(Boolean isRunning) {
			this.isRunning = isRunning;
		}
}
