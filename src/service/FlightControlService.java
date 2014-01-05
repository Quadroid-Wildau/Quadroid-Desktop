package service;

import java.util.ArrayList;
import java.util.List;

import de.th_wildau.quadroid.models.Waypoint;

public class FlightControlService {

	private static FlightControlService instance;
	
	private List<Waypoint> waypoints = new ArrayList<Waypoint>();
	
	private FlightControlService() {}
	
	public static FlightControlService getInstance() {
		if (instance == null)
			instance = new FlightControlService();
		return instance;
	}

	public Waypoint[] getWaypoints() {
		Waypoint[] result = new Waypoint[waypoints.size()];
		waypoints.toArray(result);
		return result;
	}

	public void addWaypoint(Waypoint waypoint) {
		waypoints.add(waypoint);
	}

	public void deleteWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
	}

}
