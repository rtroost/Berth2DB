package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class App {
	
	private final static boolean DEBUG = true;

	public App() {

	}

	public static void main(String[] args)
	{
		// Berths
		BerthReadWrite brw = new BerthReadWrite();
		brw.readCSV("export_HRE_BERTH.txt", "\t");
		brw.normalize();
//		List<Berth> berths = brw.getBerths();
//		for(Berth b : berths) {
//			System.out.println(b.getBerthNumber());
//		}
		writeToDB(brw);
		
		// Vessels
		VesselReadWrite vrw = new VesselReadWrite();
		vrw.readCSV("export_HRE_VESSEL.txt", "\t");
//		List<Vessel> vessels = vrw.getVessels();
//		System.out.println(vessels.get(21));
		writeToDB(vrw);
	}
	
	private static void writeToDB(IWritableData data) {
		Connection con = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Setup the connection with the DB
			String 	url = "",
					database = "",
					username = "",
					password = "";
			
			con = DriverManager
					.getConnection(url+database, username, password);
			
			if(DEBUG) System.out.println("Writing to DB has begun.");
			data.writeToDB(con);
			if(DEBUG) System.out.println("Writing to DB has finished.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}

}
