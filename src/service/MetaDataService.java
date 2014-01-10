package service;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import communication.CommunicationStack;
import communication.FlightControlCommunicator;

import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.RxData;

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
	
	public MetaData getFirstMetaData() {
		if (metaDataHistory != null && metaDataHistory.size() > 0)
			return metaDataHistory.get(0);
		return null;
	}
	
	public MetaData getLastMetaData() {
		if (metaDataHistory != null && metaDataHistory.size() > 0)
			return metaDataHistory.get(metaDataHistory.size() - 1);
		return getFlightCommunication().getMetaData();
	}

	@Override
	public void update(Observable o, Object obj) {
		if (o instanceof FlightControlCommunicator) {
			RxData data = (RxData) obj;
			
			if (data != null && data.getMetadatalist() != null && data.getMetadatalist().size() > 0) {
				//has new metadata
				metaDataHistory.addAll(data.getMetadatalist());
			} else {
				metaDataHistory.add(getFlightCommunication().getMetaData());
			}
			
			setChanged();
			notifyObservers(getLastMetaData());
		}
	}
}
