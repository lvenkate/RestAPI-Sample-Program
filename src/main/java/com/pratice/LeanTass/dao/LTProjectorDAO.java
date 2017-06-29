package com.pratice.LeanTass.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.pratice.LeanTass.dbconnection.DBConnection;

import com.pratice.LeanTass.model.Projector;
import com.pratice.LeanTass.model.Reservation;

public class LTProjectorDAO {


	private static String AVAILABLE_PROJECTORS = " SELECT name From  projectors WHERE "
			+ " id IN (SELECT id FROM projectors MINUS SELECT pid FROM allocations WHERE TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') between "
			+ " pfrom AND pto OR TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') BETWEEN pfrom AND pto)";

	private static String ALL_PROJECTORS = " SELECT name FROM projectors";

	private static String RESERVE_PROJECTOR = " INSERT INTO ALLOCATIONS VALUES(TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') , TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') , ? , ?)";

	private static String DELETE_ALLOCATION = "DELETE FROM allocations"
			+ " WHERE pfrom =  TO_DATE(?,'yyyy/mm/dd hh24:mi:ss')" + " AND pto =  TO_DATE(?,'yyyy/mm/dd hh24:mi:ss')"
			+ " AND tid = ? ";

	/**
	 * @return String[]
	 * @Description
	 */
	public List<Projector> getProjectors(String from, String to, String interval) {

		List<Projector> projectors = new LinkedList<Projector>() ;
		Connection connection = null;
		DBConnection dbConnection = new DBConnection();
		PreparedStatement 	p =null ;
		try {
			connection = dbConnection.getConnection();
			if (to != null && from != null) {
				if (interval == null){
				 	p = connection.prepareStatement(AVAILABLE_PROJECTORS);
				 	System.out.println("Came here");
				}
				else {
					StringBuilder queryBuilder = new StringBuilder("");
					queryBuilder.append(
							"SELECT name FROM projectors WHERE id IN (SELECT id FROM projectors MINUS SELECT pid  FROM allocations WHERE TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') + interval ");
					queryBuilder.append("'" + interval + "'");
					queryBuilder
							.append(" minute BETWEEN pfrom AND pto OR TO_DATE(?,'yyyy/mm/dd hh24:mi:ss') + interval ");
					queryBuilder.append("'" + interval + "'");
					queryBuilder.append(" minute BETWEEN pfrom AND pto)");
					p = connection.prepareStatement(queryBuilder.toString());
				}
				p.setString(1, from);
				p.setString(2, to);
				System.out.println(from +  " " + to);
				System.out.println("Here");
			} 
			else{
				p = connection.prepareStatement(ALL_PROJECTORS);
			   System.out.println("here too ");	
			}

			 Boolean noData = false ;
			ResultSet rs = p.executeQuery();
            while (rs.next()) {
				Projector projector = new Projector();
				projector.setName(rs.getString("name"));
				projectors.add(projector);
				noData = true;
			}
			if(!noData){
			    System.out.println("no data");
				return null;
			}
			rs.close();
			p.close();

		} catch (Exception e) {
			System.out.println("Issue");
			e.printStackTrace();
		} finally {

			dbConnection.closeConnection(connection);
		}
		return projectors;
	}

	public Reservation reserveProjector(String from, String to, String team) {
		List<Projector> projectors;
		Reservation reservation = new Reservation();
		Connection connection = null;
		DBConnection dbConnection = new DBConnection();
		String TEAMID = "SELECT id FROM team WHERE name = ? ";
		String PID = "SELECT id FROM projectors WHERE name = ? ";
		try {

			connection = dbConnection.getConnection();
			projectors = getProjectors(from, to, null);

			if(projectors == null)
			return null;
			PreparedStatement p = connection.prepareStatement(TEAMID);

			p.setString(1, team);
			int teamId = 0;
			ResultSet rs = p.executeQuery();
			if (rs.next())
				teamId = rs.getInt("id");
			p = connection.prepareStatement(PID);

			 Collections.shuffle(projectors, new Random());
         	p.setString(1, projectors.get(0).getName());
			int pId = 0;
			rs = p.executeQuery();
			if (rs.next())
				pId = rs.getInt("id");

			p = connection.prepareStatement(RESERVE_PROJECTOR);

			p.setString(1, from);
			p.setString(2, to);
			p.setInt(3, pId);
			p.setInt(4, teamId);
			p.executeUpdate();

			reservation.setFrom(from);
			reservation.setTo(to);
			reservation.setProjector(projectors.get(0).getName());
			reservation.setTeam(team);
			rs.close();
			p.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			dbConnection.closeConnection(connection);
		}
		return reservation;
	}

	public Boolean cancelProjector(String from, String to, String team) {

		List<String> projectors = new LinkedList<String>();
		Connection connection = null;
		DBConnection dbConnection = new DBConnection();
		String TEAMID = "SELECT id FROM team WHERE name = ? ";

		try {
			connection = dbConnection.getConnection();

			PreparedStatement p = connection.prepareStatement(TEAMID);
			p.setString(1, team);

			ResultSet rs = p.executeQuery();
			int teamId = 0;
			if (rs.next())
				teamId = rs.getInt("id");

			p = connection.prepareStatement(DELETE_ALLOCATION);
			p.setString(1, from);
			p.setString(2, to);
			p.setInt(3, teamId);
			p.execute();

			rs.close();
			p.close();

		} catch (Exception e) {
			System.out.println("Issue");
			e.printStackTrace();
			return false;
		} finally {
			dbConnection.closeConnection(connection);
		}
		return true;
	}
}
