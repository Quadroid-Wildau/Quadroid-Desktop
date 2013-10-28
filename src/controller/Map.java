package controller;

import model.Waypoint;

public class Map implements ViewController {

	private view.Map view;

	public void showWaypoint(Waypoint waypoint) {

	}

	public view.Map getView() {
		if (this.view == null) {
			this.view = new view.Map();
		}
		
		return this.view;
	}
}
