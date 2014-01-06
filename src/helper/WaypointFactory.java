package helper;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Waypoint;

public class WaypointFactory {

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
