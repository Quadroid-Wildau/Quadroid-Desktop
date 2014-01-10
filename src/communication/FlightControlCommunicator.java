package communication;

import java.util.Observable;
import java.util.Random;

import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.Waypoint;

public class FlightControlCommunicator extends Observable {

	public void sendWaypoints(Waypoint[] waypoints) {

	}

	public Waypoint[] getWaypoints() {
		return null;
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
	
	public void setChangedPublic() {
		this.setChanged();
	}

	public Landmark getLandmarkAlarm() {
		return null;
	}
}
