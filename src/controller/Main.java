package controller;

import java.awt.Component;

public class Main implements ViewController{
	private view.Main view;
	private controller.ViewController mapController;
	private controller.ViewController videoStreamController;
	private controller.ViewController map3DController;
	private controller.ViewController metaDataController;
	
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
	
	public ViewController getMapController() {
		if (this.mapController == null) {
			this.mapController = new controller.Map();
		}
		
		return mapController;
	}

	public ViewController getVideoStreamController() {
		if (this.videoStreamController == null) {
			this.videoStreamController = new controller.VideoStream();
		}
		
		return videoStreamController;
	}

	public ViewController getMap3DController() {
		if (this.map3DController == null) {
			this.map3DController = new controller.Map3D();
		}
		
		return map3DController;
	}

	public ViewController getMetaDataController() {
		if (this.metaDataController == null) {
			this.metaDataController = new controller.MetaData();
		}
		
		return metaDataController;
	}
}
