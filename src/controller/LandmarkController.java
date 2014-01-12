package controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.AdvLandmark;
import service.LandMarkerService;
import view.LandmarkAlarmView;

import communication.CommunicationStack;

/**
 * Controller for landmark alarm view
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class LandmarkController implements Observer {
	
	private LandmarkAlarmView view;
	
	public LandmarkController() {
		getService().addObserver(this);
	}

	@Override
	public void update(Observable clz, Object obj) {
		if (clz instanceof LandMarkerService) {
			if (view != null) {
				view.addLandmarkAlarm((AdvLandmark) obj);
			}
		}
	}
	
	private LandMarkerService getService() {
		return LandMarkerService.getInstance();
	}

	public LandmarkAlarmView getView() {
		if (view == null)
			view = new LandmarkAlarmView(this);
		
		return view;
	}
	
	/**
	 * Notify the controller that the view has been closed. Important call, because otherwise the view won't be
	 * instantiated again after beeing closed
	 */
	public void notifyViewClosed() {
		view = null;
	}

	/**
	 * Get all landmark alarms
	 * @return
	 */
	public List<AdvLandmark> getLandmarkAlarms() {
		return getService().getLandmarkAlarms();
	}
	
	/**
	 * Save {@link AdvLandmark} to file
	 * @param landmark
	 * 			The landmark to save
	 * @param filepath
	 * 			The file to save the landmark to
	 */
	public void saveToImageFile(AdvLandmark landmark, String filepath) {
		CommunicationStack.getInstance().getPhotoPersistance().saveScreenShot(
										filepath, 
										landmark.getPictureoflandmark(), 
										landmark.getMetaData());
	}
}
