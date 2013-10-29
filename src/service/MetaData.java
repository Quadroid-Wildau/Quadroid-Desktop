package service;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import communication.CommunicationStack;
import communication.Flight;

public class MetaData extends Observable implements Observer{
	
	private static MetaData instance;
	private ArrayList<model.MetaData> metaDataHistory;

	public static MetaData getInstance() {
		if (instance == null) {
			instance = new MetaData();
		}
		
		return instance;
	}
	
	private MetaData() {
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
	
	private Flight getFlightCommunication() {
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
