package helper;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Waypoint;

/**
 * This factory can create new {@link Waypoint} objects
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class WaypointFactory {

	/**
	 * Crates a {@link Waypoint} from a given {@link GeoPosition} and height
	 * @param geoPos
	 * 			The geoposition
	 * @param height
	 * 			The height in m
	 * @return
	 * 			The {@link Waypoint}
	 */
	public static Waypoint createFromGeoPosition(GeoPosition geoPos, float height) {
		Waypoint wp = new Waypoint();
		GNSS gnss = new GNSS();
		gnss.setLatitude((float)geoPos.getLatitude());
		gnss.setLongitude((float)geoPos.getLongitude());
		gnss.setHeight(height);
		wp.setPosition(gnss);
		return wp;
	}
}
