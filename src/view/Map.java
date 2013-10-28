package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class Map extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map controller;

	public Map(controller.Map controller) {
		this.setBackground(Color.blue);
		this.add(new Label("Map"));
		this.controller = controller;
	}
	
	public void setWaypoints(model.Waypoint[] waypoints) {
		
	}

}
