package controller;

import java.util.Observable;
import java.util.Observer;

import service.MetaDataService;
import service.VideoStreamService;
import view.VideoStreamView;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import communication.CommunicationStack;

import de.th_wildau.quadroid.models.MetaData;

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

	public void startVideoStream(int videoDevicePort) throws Exception {
		getService().startVideoStream(videoDevicePort);
	}
	
	public void stopGrabbingVideoFrames() {
		getService().stopVideoStream();
	}
	
	public String[] getAvailableCaptureDevices() {
		return getService().getAvailableCaptureDevices();
	}
	
	public void saveScreenShot(String filepath) {
		IplImage frame = getService().getCurrentFrame();
		MetaData metadata = MetaDataService.getInstance().getLastMetaData();
		CommunicationStack.getInstance().getPhotoPersistance().saveScreenShot(filepath, frame, metadata);
	}
	
	public void saveVideoStream(String filepath) {
		getService().saveVideoStream(filepath);
	}
	
	public void stopSavingVideo() {
		getService().stopSaveVideoStream();
	}

	@Override
	public void update(Observable observable, Object obj) {
		IplImage frame = (IplImage) obj;
		getView().showNewVideoFrame(frame);
	}
}
