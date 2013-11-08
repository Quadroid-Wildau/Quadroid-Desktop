package communication;

import communication.persistance.PhotoPersistance;
import communication.persistance.VideoPersistance;

public class CommunicationStack {

	private static CommunicationStack instance;
	
	private PushCommunicator pushCommunicator;

	private FlightControlCommunicator flightCommunicator;

	private VideoCommunicator videoStreamCommunicator;

	private VideoPersistance videoPersistance;

	private PhotoPersistance photoPersistance;

	public static CommunicationStack getInstance() {
		if (instance == null) {
			instance = new CommunicationStack();
		}
		
		return instance;
	}

	public PushCommunicator getPushCommunicator() {
		if (this.pushCommunicator == null) {
			this.pushCommunicator = new PushCommunicator();
		}
		
		return this.pushCommunicator;
	}

	public FlightControlCommunicator getFlightCommunicator() {
		if (this.flightCommunicator == null) {
			this.flightCommunicator = new FlightControlCommunicator();
		}
		
		return this.flightCommunicator;
	}

	public VideoCommunicator getVideoStreamCommunicator() {
		if (this.videoStreamCommunicator == null) {
			this.videoStreamCommunicator = new VideoCommunicator();
		}
		
		return this.videoStreamCommunicator;
	}

	public VideoPersistance getVideoPersistance() {
		if (this.videoPersistance == null) {
			this.videoPersistance = new VideoPersistance();
		}
		
		return this.videoPersistance;
	}

	public PhotoPersistance getPhotoPersistance() {
		if (this.photoPersistance == null) {
			this.photoPersistance = new PhotoPersistance();
		}
		
		return this.photoPersistance;
	}

}
