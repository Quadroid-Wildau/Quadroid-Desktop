package controller;

import helper.WaypointFactory;

import java.util.Observable;
import java.util.Observer;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import service.FlightControlService;
import service.MetaDataService;
import view.MapView;
import de.th_wildau.quadroid.models.Waypoint;

public class MapController implements ViewController, Observer {

	private MapView view;
	
	public MapController() {
		getService().addObserver(this);
		FlightControlService.getInstance().addObserver(this);
	}

	public view.MapView getView() {
		if (this.view == null) {
			this.view = new view.MapView(this);
		}
		return this.view;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		getView().setGeoData(getService().getMetaData().getAirplane().GeoData());
	}
	
	private MetaDataService getService() {
		return MetaDataService.getInstance();
	}
		
	/**
	 * Add a new waypoint
	 * @param geopos
	 * 			The geo position
	 * @param height
	 * 			The height
	 * 			
	 */
	public void addWaypoint(GeoPosition geopos, float height) {
		Waypoint wp = WaypointFactory.createFromGeoPosition(geopos, height);
	    FlightControlService.getInstance().addWaypoint(wp);
	}
	
	/**
	 * Remove a single waypoint
	 * @param wp
	 */
	public void removeWaypoint(Waypoint wp) {
		FlightControlService.getInstance().deleteWaypoint(wp);
	}
			
	/**
	 * Tell Quadroid to fly to a certain geo position.
	 * @param pos
	 * 			The geo position to fly to
	 * @param height
	 * 			height in m
	 * @param removeWaypoints
	 * 			Remove all saved waypoints?
	 */
	public void navigateTo(GeoPosition pos, float height, boolean removeWaypoints) {
		Waypoint wp = WaypointFactory.createFromGeoPosition(pos, height);
		if (removeWaypoints) {
			FlightControlService.getInstance().clearWaypoints();
			FlightControlService.getInstance().addWaypoint(wp);
		} else {
			FlightControlService.getInstance().addWaypointAtTop(wp);
		}
	}
}
