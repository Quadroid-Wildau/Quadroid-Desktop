package controller;

import java.awt.Component;

public class Main implements ViewController{
	private view.Main view;
	private controller.Map mapController;
	private controller.VideoStream videoStreamController;
	private controller.Map3D map3DController;
	private controller.MetaData metaDataController;
	
	public Main() {
	}
	
	@Override
	public Component getView() {
		if (view == null) {
			this.view = new view.Main(this);
			this.view.setMap(this.getMapController().getView());
			this.view.setMap3D(this.getMap3DController().getView());
			this.view.setMetaData(this.getMetaDataController().getView());
			this.view.setVideoStream(this.getVideoStreamController().getView());
		}
		
		return this.view;
	}
	
	public controller.Map getMapController() {
		if (this.mapController == null) {
			this.mapController = new controller.Map();
		}
		
		return mapController;
	}

	public controller.VideoStream getVideoStreamController() {
		if (this.videoStreamController == null) {
			this.videoStreamController = new controller.VideoStream();
		}
		
		return videoStreamController;
	}

	public controller.Map3D getMap3DController() {
		if (this.map3DController == null) {
			this.map3DController = new controller.Map3D();
		}
		
		return map3DController;
	}

	public controller.MetaData getMetaDataController() {
		if (this.metaDataController == null) {
			this.metaDataController = new controller.MetaData();
		}
		
		return metaDataController;
	}
}
