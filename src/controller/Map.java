package controller;

import java.util.Observable;
import java.util.Observer;

import communication.CommunicationStack;

import service.MetaData;

import model.Waypoint;

public class Map implements ViewController, Observer {

	private view.Map view;
	
	public Map() {
		this.getService().addObserver(this);
	}

	public view.Map getView() {
		if (this.view == null) {
			this.view = new view.Map(this);
		}
		
		return this.view;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(getService().getMetaData().getGeodata().getLatitude());
		this.view.setGeoData(getService().getMetaData().getGeodata());
	}
	
	private MetaData getService() {
		return MetaData.getInstance();
	}
}
