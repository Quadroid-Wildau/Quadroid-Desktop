package view.interfaces;

import view.MapView;
import view.WaypointView;

/**
 * Used in {@link WaypointView} to notify {@link MapView} when a Waypoint was deleted.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public interface WaypointDeletedInterface {

	/**
	 * Called when a waypoint was deleted
	 * @param index
	 * 			Index of the waypoint in list
	 */
	public void onWaypointDeleted(int index);
}
