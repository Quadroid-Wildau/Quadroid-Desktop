package service;

import java.util.Observable;

import model.Landmark;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

import communication.CommunicationStack;
import communication.VideoCommunicator;


public class VideoStreamService extends Observable {

	private static VideoStreamService instance;
	private VideoCommunicator mVideoCommunicator;
	private FrameGrabber mFrameGrabber;
	private GrabberThread mGrabberThread;
	
	public static VideoStreamService getInstance() {
		if (instance == null)
			instance = new VideoStreamService();
		
		return instance;
	}
	
	public void startVideoStream(int videoDevicePort) throws Exception {
		mVideoCommunicator = getVideoCommunicator(videoDevicePort);
		mFrameGrabber = mVideoCommunicator.getFrameGrabber();
		
		//just in case...
		stopVideoStream();
		
		mGrabberThread = new GrabberThread(mFrameGrabber);
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
		
		private final FrameGrabber mFrameGrabber;
		private boolean canGrab = true;
		private IplImage currentFrame;
		
		public GrabberThread(FrameGrabber mFrameGrabber) {
			this.mFrameGrabber = mFrameGrabber;
		}
		
		public synchronized void stopGrab() {
			canGrab = false;
			
			try {
				if (currentFrame != null)
					currentFrame.release();
				mFrameGrabber.stop();
				mFrameGrabber.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				mFrameGrabber.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			while (canGrab) {
				try {
					currentFrame = mFrameGrabber.grab();
					synchronized (this) {
						VideoStreamService.this.setChanged();
						VideoStreamService.this.notifyObservers(currentFrame);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
