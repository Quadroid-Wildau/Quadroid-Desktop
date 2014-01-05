package service;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import communication.CommunicationStack;
import communication.FlightControlCommunicator;

import de.th_wildau.quadroid.models.MetaData;

public class MetaDataService extends Observable implements Observer{
	
	private static MetaDataService instance;
	private ArrayList<MetaData> metaDataHistory;

	public static MetaDataService getInstance() {
		if (instance == null) {
			instance = new MetaDataService();
		}
		
		return instance;
	}
	
	private MetaDataService() {
		this.metaDataHistory = new ArrayList<MetaData>();
		this.getFlightCommunication().addObserver(this);
	}
	
	public MetaData getMetaData() {
		MetaData metaData = null;
		
		if(this.metaDataHistory.size() > 0) {
			int index = this.metaDataHistory.size() - 1;
			metaData = this.metaDataHistory.get(index);
		}
		
		return metaData;
	}
	
	public ArrayList<MetaData> getMetaDatas() {
		return this.metaDataHistory;
	}
	
	private FlightControlCommunicator getFlightCommunication() {
		return CommunicationStack.getInstance().getFlightCommunicator();
	}

	@Override
	public void update(Observable o, Object arg) {
		MetaData metaData = this.getFlightCommunication().getMetaData();
		this.metaDataHistory.add(metaData);
		this.setChanged();
		this.notifyObservers(metaData);
	}
}
