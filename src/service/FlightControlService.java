package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import communication.CommunicationStack;

import de.th_wildau.quadroid.models.Waypoint;

/**
 * This service is used to manage Flight control commands.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class FlightControlService extends Observable {

	private static FlightControlService instance;
	
	private List<Waypoint> waypoints = new ArrayList<Waypoint>();
	
	private FlightControlService() {}
	
	/**
	 * Singleton
	 * @return
	 */
	public static FlightControlService getInstance() {
		if (instance == null)
			instance = new FlightControlService();
		return instance;
	}

	/**
	 * Returns the waypoints of Quadroid
	 * @return
	 * 		the waypoints
	 */
	public Waypoint[] getWaypoints() {
		Waypoint[] result = new Waypoint[waypoints.size()];
		waypoints.toArray(result);
		return result;
	}

	/**
	 * Add new waypoint to list and send waypoints to Quadroid quadcopter
	 * @param waypoint
	 */
	public void addWaypoint(Waypoint waypoint) {
		waypoints.add(waypoint);
		CommunicationStack.getInstance().getFlightCommunicator().sendWaypoints(getWaypoints());
	}
	
	/**
	 * Add waypoint at top of the list and send waypoints to Quadroid quadcopter
	 * @param waypoint
	 */
	public void addWaypointAtTop(Waypoint waypoint) {
		List<Waypoint> cache = new ArrayList<>(waypoints);
		waypoints.clear();
		waypoints.add(waypoint);
		waypoints.addAll(cache);
		CommunicationStack.getInstance().getFlightCommunicator().sendWaypoints(getWaypoints());
	}

	/**
	 * Delete waypoint at index and send remaining waypoints to Quadroid quadcopter
	 * @param index
	 */
	public void deleteWaypoint(int index) {
		waypoints.remove(index);
		CommunicationStack.getInstance().getFlightCommunicator().sendWaypoints(getWaypoints());
	}
	
	/**
	 * Delete all waypoints
	 */
	public void clearWaypoints() {
		waypoints.clear();
		CommunicationStack.getInstance().getFlightCommunicator().sendWaypoints(getWaypoints());
	}
	
	/**
	 * callback when waypoint was reached
	 */
	public void waypointReached() {
		waypoints.remove(0);
		setChanged();
		notifyObservers();
	}

}
