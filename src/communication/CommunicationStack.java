package communication;

import communication.persistance.Photo;
import communication.persistance.Video;

public class CommunicationStack {

	private static CommunicationStack instance;
	
	private Push pushCommunicator;

	private Flight flightCommunicator;

	private VideoStream videoStreamCommunicator;

	private Video videoPersistance;

	private Photo photoPersistance;

	public CommunicationStack getInstance() {
		if (instance == null) {
			instance = new CommunicationStack();
		}
		
		return instance;
	}

	public Push getPushCommunicator() {
		if (this.pushCommunicator == null) {
			this.pushCommunicator = new Push();
		}
		
		return this.pushCommunicator;
	}

	public Flight getFlightCommunicator() {
		if (this.flightCommunicator == null) {
			this.flightCommunicator = new Flight();
		}
		
		return this.flightCommunicator;
	}

	public VideoStream getVideoStreamCommunicator() {
		if (this.videoStreamCommunicator == null) {
			this.videoStreamCommunicator = new VideoStream();
		}
		
		return this.videoStreamCommunicator;
	}

	public Video getVideoPersistance() {
		if (this.videoPersistance == null) {
			this.videoPersistance = new Video();
		}
		
		return this.videoPersistance;
	}

	public Photo getPhotoPersistance() {
		if (this.photoPersistance == null) {
			this.photoPersistance = new Photo();
		}
		
		return this.photoPersistance;
	}

}
