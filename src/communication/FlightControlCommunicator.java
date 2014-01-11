package communication;

import handler.XBeeReceiverHandler;
import handler.XBeeTransmitterHandler;
import interfaces.IRxListener;

import java.util.Observable;
import java.util.Random;

import model.XBeeRxTx;
import coder.decoder.ObserverHandler;
import coder.encoder.TxDataEncoder;
import connection.Connect;
import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.RxData;
import de.th_wildau.quadroid.models.Waypoint;
import enums.XBee;

/**
 * Class for flight control of Quadroid. Used for sending Waypoints.
 * Before you can do this, you need to set a valif {@link Connect} object, so the 
 * transmitter can be initiated.
 * 
 * @see XBeeTransmitterHandler
 * 
 * @author Georg Baumgarten
 *
 */
public class FlightControlCommunicator extends Observable implements IRxListener {
		
	private Waypoint[] currentWaypoints;
	private XBeeTransmitterHandler mTransmitterHandler;
	private TxDataEncoder encoder = new TxDataEncoder();
	private Connect xbeeconnection;
	
	public FlightControlCommunicator() {
		ObserverHandler.getReference().register(this);
	}
	
	public void closeXbeeConnection() {
		if (xbeeconnection != null)
			xbeeconnection.disconnect();
	}
	
	public boolean openXbeeConnection(String port) {
		//create xbee device
		XBeeRxTx xbeedevice = new XBeeRxTx();
		xbeedevice.setBaud(XBee.BAUD.getValue());
		xbeedevice.setDatabits(XBee.DATABITS.getValue());
		xbeedevice.setParity(XBee.PARITY.getValue());
		xbeedevice.setPort(port);//set selected port
		xbeedevice.setStopbits(XBee.STOPBITS.getValue());
		xbeedevice.setDevicename(XBee.DEVICENAME.getName());
		
		//create connection to xbee device
		xbeeconnection = Connect.getInstance(xbeedevice);
		//registered observer, transmitter and receiver
		if (xbeeconnection != null) {
			//Set xbee connection
			XBeeReceiverHandler receiver = new XBeeReceiverHandler();
			mTransmitterHandler = new XBeeTransmitterHandler(xbeeconnection);
			
	        ObserverHandler oh = ObserverHandler.getReference();
	        oh.register(this);
	        xbeeconnection.addSerialPortEventListener(receiver);
	        
			return true;
		}
		
		return false;
	}

	public void sendWaypoints(Waypoint[] waypoints) {
		this.currentWaypoints = waypoints;
		
		if (waypoints.length > 0 && mTransmitterHandler != null) {
			byte[] data = encoder.waypointToBytes(waypoints[0]);
			for (int i = 1; i < waypoints.length; i++) {
				data = encoder.appendBytes(data, encoder.waypointToBytes(waypoints[i]));
			}
			
			mTransmitterHandler.transmit(data);
		}
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

	@Override
	public void rx(RxData data) {
		this.setChanged();
		this.notifyObservers(data);
	}
}
