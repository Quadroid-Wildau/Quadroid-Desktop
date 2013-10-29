package communication;

import java.util.Random;

import model.Landmark;
import model.Waypoint;
import model.MetaData;

public class Flight {

	public void sendWaypoints(Waypoint[] waypoints) {

	}

	public Waypoint[] getWaypoints() {
		return null;
	}

	public MetaData getMetaData() {
		MetaData metaData = new MetaData();
		Random rand = new Random();
		
		metaData.setBatteryState(rand.nextFloat() * 100);
		metaData.setCourse(rand.nextFloat() * 360);
		metaData.setPitch(rand.nextFloat() * 100);
		metaData.setRoll(rand.nextFloat() * 100);
		metaData.setYaw(rand.nextFloat() * 180);
		metaData.setSpeed(rand.nextFloat() * 20);
		metaData.setTime(rand.nextLong() * 100);
		
		return metaData;
	}

	public Landmark getLandmarkAlarm() {
		return null;
	}
}
