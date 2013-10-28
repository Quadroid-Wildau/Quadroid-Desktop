package controller;

import view.View;

public class Main implements ViewController{
	private view.Main view;
	private controller.Map mapController;
	private controller.VideoStream videoStreamController;
	private controller.Map3D map3DController;
	private controller.MetaData metaDataController;
	
	@Override
	public view.View getView() {
		if (view == null) {
			this.view = new view.Main();
		}
		
		return this.view;
	}
}
