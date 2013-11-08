package service;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import communication.CommunicationStack;
import communication.FlightControlCommunicator;

public class MetaDataService extends Observable implements Observer{
	
	private static MetaDataService instance;
	private ArrayList<model.MetaData> metaDataHistory;

	public static MetaDataService getInstance() {
		if (instance == null) {
			instance = new MetaDataService();
		}
		
		return instance;
	}
	
	private MetaDataService() {
		this.metaDataHistory = new ArrayList<model.MetaData>();
		this.getFlightCommunication().addObserver(this);
	}
	
	public model.MetaData getMetaData() {
		model.MetaData metaData = null;
		
		if(this.metaDataHistory.size() > 0) {
			int index = this.metaDataHistory.size() - 1;
			metaData = this.metaDataHistory.get(index);
		}
		
		return metaData;
	}
	
	public ArrayList<model.MetaData> getMetaDatas() {
		return this.metaDataHistory;
	}
	
	private FlightControlCommunicator getFlightCommunication() {
		return CommunicationStack.getInstance().getFlightCommunicator();
	}

	@Override
	public void update(Observable o, Object arg) {
		model.MetaData metaData = this.getFlightCommunication().getMetaData();
		this.metaDataHistory.add(metaData);
		this.setChanged();
		this.notifyObservers();
	}
}
