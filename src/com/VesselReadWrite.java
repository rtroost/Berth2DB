package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VesselReadWrite implements IWritableData {
	
	private List<Vessel> vessels;
	
	public VesselReadWrite() {
		this.vessels = new ArrayList<Vessel>();
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
				
				for(int i = 0; i < s.length; i++) {
					s[i] = s[i].trim();
					s[i] = s[i].replace("\t", "");
				}

				Vessel v = new Vessel();
				
				v.setVesselId(Integer.parseInt(s[0]));
				
				/** Quick fix for filtering test ships */
				if( ! s[2].matches("[0-9]+"))
					continue;
				
				v.setShipNumber(Integer.parseInt(s[2]));
				v.setShipType(s[3]);
				v.setShipName(s[5]);
				if( s.length > 6 && ! s[6].equals("") )
					v.setLength(Float.parseFloat(s[6]));
				if( s.length > 7 && ! s[7].equals("") )
					v.setBeam(Float.parseFloat(s[7]));
				if( s.length > 9 && ! s[9].equals("") )
					v.setMaxDraught(Integer.parseInt(s[9]));
				if( s.length > 11 && ! s[11].equals("") )
					v.setCategory(s[11]);

				vessels.add(v);
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

	public void writeToDB(Connection con) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		
		int writes = 0;
		
		for (int i = 0; i < getVessels().size(); i++)
		{
			Vessel v = getVessels().get(i);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = con
					.prepareStatement("INSERT INTO vessels VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?)");
			// Parameters start with 1
			preparedStatement.setInt(1, v.getVesselId());
			preparedStatement.setInt(2, v.getShipNumber());
			preparedStatement.setString(3, v.getShipType());
			preparedStatement.setString(4, v.getShipName());
			preparedStatement.setFloat(5, v.getLength());
			preparedStatement.setFloat(6, v.getBeam());
			preparedStatement.setInt(7, v.getMaxDraught());
			preparedStatement.setString(8, v.getCategory());
			preparedStatement.executeUpdate();
			
			writes++;
			
			if(writes % 100 == 0)
				System.out.println("Written records so far: " + writes);
		}
	}

	public List<Vessel> getVessels() {
		return vessels;
	}

}
