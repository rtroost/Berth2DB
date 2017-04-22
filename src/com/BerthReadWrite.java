package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BerthReadWrite implements IWritableData {
	
	private static final boolean DEBUG = true;

	private List<Berth> berths;
	
	public BerthReadWrite() {
		this.berths = new ArrayList<Berth>();
	}
	
	public void readCSV(String csvFile, String delimiter) {
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine(); // Skip header
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] s = line.split(delimiter);

				Berth b = new Berth();
				
				b.setBerthType(s[1].charAt(0));
				b.setBerthNumber(Integer.parseInt(s[2]));
				b.setName(s[3]);
				b.setRds(Integer.parseInt(s[7]), Integer.parseInt(s[8]));
				if( s.length > 12 && ! s[12].equals("") )
					b.setCompanyName(s[12]);
				
				b.setVisibleOnMap(true);
				
				/** Begin blacklisting */
				if( b.getBerthType() == 'G')
				{
					continue;
				}
				
				if(	(b.getLat() == 52.01601f && b.getLng() == 3.9449887f) )
				{
					if(DEBUG) System.out.println("Blacklisting for: " + b.getLat() + ", " + b.getLng());
					b.setVisibleOnMap(false);
				}
				
				if( b.getName().equalsIgnoreCase("VERVALLEN LIGPL") )
				{
					if(DEBUG) System.out.println("Blacklisting for: " + b.getName());
					b.setVisibleOnMap(false);
				}
				
				berths.add(b);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void normalize() {
		
		HashMap<String, List<Berth>> berthsWithSameName = new HashMap<String, List<Berth>>();
		
		/**
		 * 	Group all berths with the same name together
		 */
		for(int i = 0; i < berths.size(); i++)
		{
			Berth berth = berths.get(i);
			
			/**
			 * Don't group terminals that don't have a name or have been disbanded
			 */
			if( berth.getName().equalsIgnoreCase("") || berth.getName().equalsIgnoreCase("GEEN LIGPLAATS") || berth.getName().equalsIgnoreCase("VERVALLEN LIGPL") ) {
				continue;
			}
			
			/**
			 * Check if the hashmap doesn't contain a key with the birth's name yet
			 */
			if( ! berthsWithSameName.containsKey(berth.getName()) )
			{
				berthsWithSameName.put(berth.getName(), new ArrayList<Berth>());
			}
			
			berthsWithSameName.get(berth.getName()).add(berth);
		}
		
		/**
		 * Get the average position for each berth and decide the closest berth
		 */
		for(List<Berth> groupedBerths : berthsWithSameName.values())
		{
			float totalRds_x = 0f, 
				  totalRds_y = 0f;
			
			for(Berth b : groupedBerths)
			{
				totalRds_x += b.getRds_x();
				totalRds_y += b.getRds_y();
			}
			
			float avgRds_x = totalRds_x / groupedBerths.size();
			float avgRds_y = totalRds_y / groupedBerths.size();
			
			/**
			 * Get closest berth location
			 */
			Berth bestBerth = null;
			float smallestDistance = -1f;
			
			for(Berth b : groupedBerths)
			{
				float distanceFromRds = b.getDistanceFromRds(avgRds_x, avgRds_y);
				
				if( smallestDistance == -1f || smallestDistance > distanceFromRds) {
					smallestDistance = distanceFromRds;
					bestBerth = b;
				}
			}
			
			/**
			 * Set normalized berthNumbers
			 */			
			for(Berth b : groupedBerths)
			{
				b.setBerthNumberNormalized(bestBerth.getBerthNumber());
			}
		}
	}

	public void writeToDB(Connection con) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		
		int writes = 0;
		
		for (int i = 0; i < getBerths().size(); i++) {
			Berth b = getBerths().get(i);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = con
					.prepareStatement("INSERT INTO terminals VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)");
			// Parameters start with 1
			preparedStatement.setInt(1, b.getBerthNumber());
			preparedStatement.setInt(2, b.getBerthNumberNormalized());
			preparedStatement.setFloat(3, b.getLat());
			preparedStatement.setFloat(4, b.getLng());
			preparedStatement.setString(5, b.getName());
			preparedStatement.setString(6, String.valueOf(b.getBerthType()));
			preparedStatement.setString(7, b.getCompanyName());
			preparedStatement.setBoolean(8, b.isVisibleOnMap());
			preparedStatement.executeUpdate();
			
			writes++;
			
			if(writes % 100 == 0)
				System.out.println("Written records so far: " + writes);
		}
	}

	public List<Berth> getBerths() {
		return berths;
	}
}
