package communication;

import interfaces.IRxListener;

import java.util.Observable;
import java.util.Random;

import coder.decoder.ObserverHandler;
import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.RxData;
import de.th_wildau.quadroid.models.Waypoint;

public class FlightControlCommunicator extends Observable implements IRxListener {
		
	private Waypoint[] currentWaypoints;
	
	public FlightControlCommunicator() {
		ObserverHandler.getReference().register(this);
	}

	public void sendWaypoints(Waypoint[] waypoints) {
		this.currentWaypoints = waypoints;
	}

	public Waypoint[] getWaypoints() {
		return currentWaypoints;
	}
	
	public MetaData getMetaData() {
		MetaData metaData = new MetaData();
		Random rand = new Random();
		
		GNSS geoData = new GNSS();
		geoData.setHeight(rand.nextFloat() * 20);
		geoData.setLatitude(rand.nextFloat() * 180 - 90);
		geoData.setLongitude(rand.nextFloat() * 360 - 180);
		
		Airplane ap = new Airplane();
		ap.setBatteryState((byte)rand.nextInt(100));
		ap.setTime(rand.nextLong() * 100);
		ap.setGeoData(geoData);
		
		Attitude att = new Attitude();
		att.setPitch(rand.nextFloat() * 100);
		att.setRoll(rand.nextFloat() * 100);
		att.setYaw(rand.nextFloat() * 100);
		
		Course course = new Course();
		course.setSpeed(rand.nextFloat() * 20);
		course.setAngleReference(rand.nextFloat() * 360);
		
		metaData.setAirplane(ap);
		metaData.setAttitude(att);
		metaData.setCourse(course);
		
		return metaData;
	}
	
	/**
	 * just for testing
	 */
	public void setChangedPublic() {
		setChanged();
	}

	@Override
	public void rx(RxData data) {
		this.setChanged();
		this.notifyObservers(data);
	}
}
