package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class Map extends JPanel{

	private controller.Map controller;

	public Map() {
		this.setBackground(Color.blue);
		this.add(new Label("Map"));
	}
	
	public void setWaypoints(model.Waypoint[] waypoints) {
		
	}

}
