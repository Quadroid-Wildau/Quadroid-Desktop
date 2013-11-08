package service;

import java.util.Observable;

import model.Landmark;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import communication.CommunicationStack;
import communication.VideoCommunicator;


public class VideoStreamService extends Observable {

	private static VideoStreamService instance;
	private VideoCommunicator mVideoCommunicator;
	private CvCapture mCvCapture;
	private GrabberThread mGrabberThread;
	
	public static VideoStreamService getInstance() {
		if (instance == null)
			instance = new VideoStreamService();
		
		return instance;
	}
	
	public void startVideoStream(int videoDevicePort) throws Exception {
		mVideoCommunicator = getVideoCommunicator(videoDevicePort);
		mCvCapture = mVideoCommunicator.getCvCapture();
		
		//just in case...
		stopVideoStream();
		
		mGrabberThread = new GrabberThread(mCvCapture);
		mGrabberThread.start();
	}
	
	public void stopVideoStream() {
		if (mGrabberThread != null) {
			mGrabberThread.stopGrab();
		}
	}
	
	private VideoCommunicator getVideoCommunicator(int videoDevicePort) throws Exception {
		return CommunicationStack.getInstance().getVideoStreamCommunicator(videoDevicePort);
	}

	public void saveVideoStream() {

	}

	public Landmark saveScreenshot() {
		return null;
	}
	
	private class GrabberThread extends Thread {
		
		private final CvCapture mCvCapture;
		private boolean canGrab = true;
		private IplImage currentFrame;
		
		public GrabberThread(CvCapture mCvCapture) {
			this.mCvCapture = mCvCapture;
		}
		
		public synchronized void stopGrab() {
			canGrab = false;
			opencv_highgui.cvReleaseCapture(mCvCapture);
		}
		
		public void run() {
			while (canGrab) {
				currentFrame = opencv_highgui.cvQueryFrame(mCvCapture);
				synchronized (this) {
					VideoStreamService.this.setChanged();
					VideoStreamService.this.notifyObservers(currentFrame);
				}
			}
		}
	}
}
