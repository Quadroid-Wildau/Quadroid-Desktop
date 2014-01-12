package controller;

import java.util.Observable;
import java.util.Observer;

import service.MetaDataService;
import service.VideoStreamService;
import view.VideoStreamView;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import communication.CommunicationStack;

import controller.interfaces.ViewController;
import de.th_wildau.quadroid.models.MetaData;

/**
 * Controller for the videostream view
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class VideoStreamController implements ViewController, Observer {

	private VideoStreamView view;

	public VideoStreamController() {
		getService().addObserver(this);
	}
	
	public VideoStreamView getView() {
		if (this.view == null) {
			this.view = new VideoStreamView(this);
		}
		return this.view;
	}
	
	private VideoStreamService getService() {
		return VideoStreamService.getInstance();
	}

	/**
	 * Tell the service to start and show the video stream
	 * @param videoDevicePort
	 * 				The number of the port to open (capture device)
	 * @throws Exception
	 */
	public void startVideoStream(int videoDevicePort) throws Exception {
		getService().startVideoStream(videoDevicePort);
	}
	
	/**
	 * Tell the service to stop grab frames and release the capture device
	 */
	public void stopGrabbingVideoFrames() {
		getService().stopVideoStream();
	}
	
	/**
	 * Get all capture devices. THis will only work on Windows OS.
	 * @return
	 * 		A string array which contains the names of all capture devices
	 */
	public String[] getAvailableCaptureDevices() {
		return getService().getAvailableCaptureDevices();
	}
	
	/**
	 * Save a screenshot of the current frame with {@link MetaData}
	 * @param filepath
	 * 			The path to save the image to
	 */
	public void saveScreenShot(String filepath) {
		IplImage frame = getService().getCurrentFrame();
		MetaData metadata = MetaDataService.getInstance().getLastMetaData();
		CommunicationStack.getInstance().getPhotoPersistance().saveScreenShot(filepath, frame, metadata);
	}
	
	/**
	 * Start recording video
	 * @param filepath
	 * 			The path to record the video to
	 */
	public void saveVideoStream(String filepath) {
		getService().saveVideoStream(filepath);
	}
	
	/**
	 * Stop recording video
	 */
	public void stopSavingVideo() {
		getService().stopSaveVideoStream();
	}

	@Override
	public void update(Observable observable, Object obj) {
		IplImage frame = (IplImage) obj;
		getView().showNewVideoFrame(frame);
	}
}
