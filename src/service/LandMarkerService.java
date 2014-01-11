package service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.AdvLandmark;

import communication.CommunicationStack;
import communication.FlightControlCommunicator;

import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.RxData;

/**
 * This service is used to manage landmark alarms
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class LandMarkerService extends Observable implements Observer {
	
	private static LandMarkerService instance;
	
	/**
	 * landmark history
	 */
	private List<AdvLandmark> landmarkHistory = new ArrayList<AdvLandmark>();
	
	private LandMarkerService() {
		CommunicationStack.getInstance().getFlightCommunicator().addObserver(this);
	}

	/**
	 * Singleton
	 * @return
	 */
	public static LandMarkerService getInstance() {
		if (instance == null)
			instance = new LandMarkerService();
		return instance;
	}

	/**
	 * get all landmark alarms
	 * @return
	 * 		List of {@link AdvLandmark}
	 */
	public List<AdvLandmark> getLandmarkAlarms() {
		return landmarkHistory;
	}
	
	/**
	 * Gets last Landmar alarm in list
	 * @return
	 * 		last {@link AdvLandmark}
	 */
	public AdvLandmark getLastLandmarkAlarm() {
		if (landmarkHistory.size() > 0)
			return landmarkHistory.get(landmarkHistory.size() - 1);
		
		return null;
	}
	
	/**
	 * This method can be used to simulate a landmark alarm
	 * @param image
	 * 			The landmark image
	 */
	public void simulateLandmarkAlarm(BufferedImage image) {
		GNSS geoData = new GNSS();
		geoData.setHeight(21);
		geoData.setLatitude(54.489165f);
		geoData.setLongitude(14.568f);
		
		AdvLandmark landmark = new AdvLandmark();
		MetaData metadata = new MetaData();
		Airplane ap = new Airplane();
		
		ap.setGeoData(geoData);
		ap.setTime(System.currentTimeMillis()/1000);
		ap.setGeoData(geoData);				
		
		metadata.setAirplane(ap);
		
		landmark.setPictureoflandmark(image);
		landmark.setMetaData(metadata);
		
		landmarkHistory.add(landmark);
		
		//push to server which will deliver it to mobile users
		CommunicationStack.getInstance().getPushCommunicator().pushLandmarkAlarm(landmark);
		
		setChanged();
		notifyObservers(getLastLandmarkAlarm());
	}

	@Override
	public void update(Observable o, Object obj) {
		if (o instanceof FlightControlCommunicator) {
			RxData data = (RxData) obj;
						
			if (data != null && data.getLandmarklist() != null && data.getLandmarklist().size() > 0) {
				//has new landmark alert
				
				for (Landmark lm : data.getLandmarklist()) {
					AdvLandmark advLandmark = new AdvLandmark(lm);
					landmarkHistory.add(advLandmark);
					
					//push to server which will deliver it to mobile users
					CommunicationStack.getInstance().getPushCommunicator().pushLandmarkAlarm(advLandmark);
				}
				
				setChanged();
				notifyObservers(getLastLandmarkAlarm());
			}
		}
	}

}
