package communication;

import com.googlecode.javacv.FrameGrabber.Exception;

import communication.persistence.PhotoPersistence;
import communication.persistence.VideoPersistence;

public class CommunicationStack {

	private static CommunicationStack instance;
	
	private PushCommunicator pushCommunicator;

	private FlightControlCommunicator flightCommunicator;

	private VideoCommunicator videoStreamCommunicator;

	private VideoPersistence videoPersistance;

	private PhotoPersistence photoPersistance;

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

	public VideoCommunicator getVideoStreamCommunicator(int videoDevicePort) throws Exception {
		if (this.videoStreamCommunicator == null) {
			this.videoStreamCommunicator = new VideoCommunicator(videoDevicePort);
		} else {
			if (this.videoStreamCommunicator.getVideoDevicePort() != videoDevicePort) {
				this.videoStreamCommunicator = new VideoCommunicator(videoDevicePort);
			}
		}
		
		return this.videoStreamCommunicator;
	}

	public VideoPersistence getVideoPersistance() {
		if (this.videoPersistance == null) {
			this.videoPersistance = new VideoPersistence();
		}
		
		return this.videoPersistance;
	}

	public PhotoPersistence getPhotoPersistance() {
		if (this.photoPersistance == null) {
			this.photoPersistance = new PhotoPersistence();
		}
		
		return this.photoPersistance;
	}

}
