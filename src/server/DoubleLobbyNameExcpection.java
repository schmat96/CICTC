package server;

@SuppressWarnings("serial")
public class DoubleLobbyNameExcpection extends Exception {
	public DoubleLobbyNameExcpection (){
		System.out.println("There was an attempt to create a duplicate lobby name.");
	}
}
