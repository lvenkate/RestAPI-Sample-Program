package com.pratice.LeanTass.model;

import java.util.LinkedList;
import java.util.List;

public class Reservation {
	
	String projector ;
	String to;
	
	public String getProjector() {
		return projector;
	}
	public void setProjector(String projector) {
		this.projector = projector;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	String from;
	String team;
	
	public Reservation()
	{
		projector = new String();
		to= new String();
		from = new String();
		team = new String();
	}

		
}
