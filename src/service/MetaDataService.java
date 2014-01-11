package service;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import communication.CommunicationStack;
import communication.FlightControlCommunicator;

import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.RxData;

/**
 * This service is used to manage Metadata
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class MetaDataService extends Observable implements Observer{
	
	private static MetaDataService instance;
	
	/**
	 * History of metadatas
	 */
	private ArrayList<MetaData> metaDataHistory;

	/**
	 * Singleton
	 * @return
	 * 		the instance
	 */
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
	
	/**
	 * Get the current {@link MetaData} object
	 * @return
	 * 		The {@link MetaData} object or null, if no metadata packets were received yet
	 */
	public MetaData getMetaData() {
		MetaData metaData = null;
		
		if(this.metaDataHistory.size() > 0) {
			int index = this.metaDataHistory.size() - 1;
			metaData = this.metaDataHistory.get(index);
		}
		
		return metaData;
	}
	
	/**
	 * Get all {@link MetaData}s
	 * @return
	 * 		List of {@link MetaData}
	 */
	public ArrayList<MetaData> getMetaDatas() {
		return this.metaDataHistory;
	}
	
	private FlightControlCommunicator getFlightCommunication() {
		return CommunicationStack.getInstance().getFlightCommunicator();
	}
	
	/**
	 * Get first {@link MetaData} in list
	 * @return
	 * 		first {@link MetaData}
	 */
	public MetaData getFirstMetaData() {
		if (metaDataHistory != null && metaDataHistory.size() > 0)
			return metaDataHistory.get(0);
		return null;
	}
	
	/**
	 * get last {@link MetaData} in List
	 * @return
	 * 		last {@link MetaData}
	 */
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
				System.out.println("Data Size: " + data.getMetadatalist().size() + ", History Size: " + metaDataHistory.size());
				
				//has new metadata
				metaDataHistory.addAll(data.getMetadatalist());
				
				MetaData last = getLastMetaData();
				
				//Use first yaw as reference yaw
				if (last != null) {
					float referenceYaw = 0.0f;
					for (int i = 0; i < metaDataHistory.size(); i++) {
						referenceYaw = metaDataHistory.get(i).getAttitude().getYaw();
						if (referenceYaw != 0.0f)
							break;
					}
					float newYaw = (last.getAttitude().getYaw() - referenceYaw) % 360f;
					System.out.println("Reference: " + referenceYaw + ", NEwYaw: " + newYaw);
					last.getAttitude().setYaw(newYaw);
				}
				
				setChanged();
				notifyObservers(last);
			}
		}
	}
}
