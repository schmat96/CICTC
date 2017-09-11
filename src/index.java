import client.Client;
import client.WindowThread;
import server.Server;

public class index {

	public static void main(String[] args) {
		

			

			
		
		
		switch(args[0]) {
			case "1":
				try {
					int port = Integer.parseInt(args[1]);
					Client client = new Client(port,args[2]);
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			case "0":
				try {
					int port = Integer.parseInt(args[1]);
					Server server = new Server(port);
				}
					catch (Exception e) {
					// TODO: handle exception
				}
		
		}
		
		
		//Client client = new Client(17000,"172.16.2.177");
		
		

	}

}
