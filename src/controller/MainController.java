package controller;

import javax.swing.JComponent;

import view.MainView;

public class MainController implements ViewController{
	private MainView view;
	private ViewController mapController;
	private ViewController videoStreamController;
	private ViewController map3DController;
	private ViewController metaDataController;
	
	public MainController() {
	}
	
	@Override
	public JComponent getView() {
		if (view == null) {
			this.view = new view.MainView(this);
			this.view.setMap(this.getMapController().getView());
			this.view.setMap3D(this.getMap3DController().getView());
			this.view.setMetaData(this.getMetaDataController().getView());
			this.view.setVideoStream(this.getVideoStreamController().getView());
		}
		
		return this.view;
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
}
