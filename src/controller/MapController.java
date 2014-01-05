package controller;

import java.util.Observable;
import java.util.Observer;

import service.MetaDataService;
import view.MapView;

public class MapController implements ViewController, Observer {

	private MapView view;
	
	public MapController() {
		this.getService().addObserver(this);
	}

	public view.MapView getView() {
		if (this.view == null) {
			this.view = new view.MapView(this);
		}
		
		return this.view;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
//		System.out.println(getService().getMetaData().getGeodata().getLatitude());
		this.view.setGeoData(getService().getMetaData().getAirplane().GeoData());
	}
	
	private MetaDataService getService() {
		return MetaDataService.getInstance();
	}
}
