package controller;

import java.util.Observable;
import java.util.Observer;

import service.FlightControlService;
import view.LandmarkAlarmView;

public class LandmarkController implements Observer {
	
	private LandmarkAlarmView view;

	@Override
	public void update(Observable clz, Object obj) {
		if (clz instanceof FlightControlService) {
			
		}
	}

	public LandmarkAlarmView getView() {
		if (view == null)
			view = new LandmarkAlarmView(this);
		
		return view;
	}
	
	public void notifyViewClosed() {
		view = null;
	}

}
