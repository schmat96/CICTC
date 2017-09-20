package server;

import java.sql.*;

public class database {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/cictc_database";
	static final String DB_URL_old = "jdbc:mysql://172.16.2.177/cictc_database"; // Hier Schmids IP einfügen
																			//um Datenbank zu laden

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "";

	static Connection conn = null;

	public database() {
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM client";
			ResultSet rs = stmt.executeQuery(sql);

			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				String name = rs.getString("name");
				String ip = rs.getString("ip");
				int coins = rs.getInt("coins");

				//Display values
				System.out.print("ID: " + id);
				System.out.print(", Name: " + name);
				System.out.print(", ip: " + ip);
				System.out.println(", coins: " + coins);
			}
			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
		}catch(SQLException se){
			System.out.print("Cant connect to JDBC libary jar.");
			System.out.print("Please setup your Database");
			se.printStackTrace();
		}catch(Exception e){
			System.out.print("Cant connect to database");
			e.printStackTrace();
		}finally{

			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}
		}

	}

	public boolean clientWithIPAlreadyExists(String substring) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM client";
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String ip = rs.getString("ip");
				if (ip.equalsIgnoreCase(substring)) {
					rs.close();
					stmt.close();
					return true;
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public void createNewClient(String substring) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "INSERT INTO `client` (`ID`, `name`, `ip`, `coins`, `rechte`) VALUES (NULL, '', '"+substring+"', '10', '1');";
			
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void setUsername(int id, String username) {
		Statement stmt = null;
		
		
		try {
			stmt = conn.createStatement();
			String sql = "UPDATE `client` SET `name` = '"+username+"' WHERE `client`.`ID` = "+id+";";
			
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getIDfromDBdependingOnIP (String substring) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM client";
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String ip = rs.getString("ip");
				if (ip.equalsIgnoreCase(substring)) {
					int coins =  rs.getInt("ID");
					rs.close();
					stmt.close();
					return coins;
					
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int getCoins (int id) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
		
		String sql;
		sql = "SELECT * FROM client";
		ResultSet rs = stmt.executeQuery(sql);

		//STEP 5: Extract data from result set
		while(rs.next()){
			if (rs.getInt("ID")==id) {
				int coins = rs.getInt("coins");
				rs.close();
				stmt.close();
				
				return coins;
			}
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	

	public int clientearnedCoin(int i, int id) {
		Statement stmt = null;
		int coins = getCoins(id);
		coins = coins + i;

		try {
			stmt = conn.createStatement();
			String sql = "UPDATE `client` SET `coins` = '"+coins+"' WHERE `client`.`ID` = "+id+";";
			stmt.executeUpdate(sql);
			stmt.close();
			return coins;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coins;
	}

	public int getUserPermission(Client client) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
		
		String sql;
		sql = "SELECT * FROM client";
		ResultSet rs = stmt.executeQuery(sql);

		//STEP 5: Extract data from result set
		while(rs.next()){
			if (rs.getString("Name").equalsIgnoreCase(client.getUsername())) {
				int permission = rs.getInt("rechte");
				rs.close();
				stmt.close();
				
				return permission;
			}
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}


}
