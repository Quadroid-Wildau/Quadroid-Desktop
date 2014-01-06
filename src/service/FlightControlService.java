package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.th_wildau.quadroid.models.Waypoint;

public class FlightControlService extends Observable {

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
	
	public void addWaypointAtTop(Waypoint waypoint) {
		List<Waypoint> cache = new ArrayList<>(waypoints);
		waypoints.clear();
		waypoints.add(waypoint);
		waypoints.addAll(cache);
	}

	public void deleteWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
	}
	
	public void clearWaypoints() {
		waypoints.clear();
	}
	
	public void waypointReached() {
		waypoints.remove(0);
		setChanged();
		notifyObservers();
	}

}
