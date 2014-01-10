package controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import communication.CommunicationStack;

import model.AdvLandmark;
import service.LandMarkerService;
import view.LandmarkAlarmView;

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
	
	public void notifyViewClosed() {
		view = null;
	}

	public List<AdvLandmark> getLandmarkAlarms() {
		return getService().getLandmarkAlarms();
	}
	
	public void saveToImageFile(AdvLandmark landmark, String filepath) {
		CommunicationStack.getInstance().getPhotoPersistance().saveScreenShot(
										filepath, 
										landmark.getPictureoflandmark(), 
										landmark.getMetaData());
	}
}
