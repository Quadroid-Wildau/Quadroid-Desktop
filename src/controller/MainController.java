package controller;

import java.util.Observable;
import java.util.Observer;

import main.Main;
import model.AdvLandmark;

import org.apache.commons.lang3.SystemUtils;

import service.LandMarkerService;
import controller.interfaces.ViewController;

public class MainController implements Observer {
	private Main mainView;
	private ViewController mapController;
	private ViewController videoStreamController;
	private ViewController map3DController;
	private ViewController metaDataController;
	private LandmarkController landMarkController;
	
	public MainController() {
		System.out.println("New MainController");
		LandMarkerService.getInstance().addObserver(this);
	}
	
	public Main getView() {
		if (mainView == null) {
			this.mainView = new Main();
			this.mainView.setMap(this.getMapController().getView());
			
			//Because of incompatibility between Java3D Java 7 and Mac OSX, do not display the 3D view on Mac OSX
			if (!SystemUtils.IS_OS_MAC_OSX) {
				this.mainView.setMap3D(this.getMap3DController().getView());
			}
			
			this.mainView.setMetaData(this.getMetaDataController().getView());
			this.mainView.setVideoStream(this.getVideoStreamController().getView());
		}
		
		return this.mainView;
	}
	
	public ViewController getMapController() {
		if (this.mapController == null) {
			this.mapController = new controller.MapController();
		}
		
		return mapController;
	}

	public ViewController getVideoStreamController() {
		if (this.videoStreamController == null) {
			this.videoStreamController = new controller.VideoStreamController();
		}
		
		return videoStreamController;
	}

	public ViewController getMap3DController() {
		if (this.map3DController == null) {
			this.map3DController = new controller.Map3DController();
		}
		
		return map3DController;
	}

	public ViewController getMetaDataController() {
		if (this.metaDataController == null) {
			this.metaDataController = new controller.MetaDataController();
		}
		
		return metaDataController;
	}
	
	public LandmarkController getLandmarkController() {
		if (landMarkController == null)
			landMarkController = new LandmarkController();
		return landMarkController;
	}

	@Override
	public void update(Observable o, Object obj) {
		if (o instanceof LandMarkerService) {
			getView().newLandMarkAlarm((AdvLandmark)obj);
		}
	}
}
