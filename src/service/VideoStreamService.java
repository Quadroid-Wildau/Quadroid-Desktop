package service;

import java.util.Observable;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import com.googlecode.javacv.cpp.opencv_highgui.CvVideoWriter;
import com.googlecode.javacv.cpp.videoInputLib.videoInput;
import communication.CommunicationStack;
import communication.VideoCommunicator;

public class VideoStreamService extends Observable {

	private static VideoStreamService instance;
	private VideoCommunicator mVideoCommunicator;
	private CvCapture mCvCapture;
	private GrabberThread mGrabberThread;
	private IplImage currentFrame;
	
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
			mGrabberThread.interrupt();
		}
	}
	
	private VideoCommunicator getVideoCommunicator(int videoDevicePort) throws Exception {
		return CommunicationStack.getInstance().getVideoStreamCommunicator(videoDevicePort);
	}

	public void saveVideoStream(String filepath) {
		if (mGrabberThread != null && mGrabberThread.isAlive()) {
			mGrabberThread.stopGrab();
			mGrabberThread.interrupt();
		}
		
		mGrabberThread = new GrabberThread(mCvCapture, filepath);
		mGrabberThread.start();
	}
	
	public void stopSaveVideoStream() {
		if (mGrabberThread != null) {
			mGrabberThread.stopGrab();
		}
		mGrabberThread = new GrabberThread(mCvCapture);
		mGrabberThread.start();
	}
	
	public IplImage getCurrentFrame() {
		return currentFrame;
	}
	
	public String[] getAvailableCaptureDevices() {
		int count = videoInput.listDevices();
		String[] devices = new String[count];
		for (int i = 0; i < count; i++) {
			devices[i] = videoInput.getDeviceName(i);
		}
		
		return devices;
	}
	
	private class GrabberThread extends Thread {
		
		private final CvCapture mCvCapture;
		private boolean canGrab = true;
		
		private boolean saveVideo = false;
		private CvVideoWriter mVideoWriter;
		
		public GrabberThread(CvCapture mCvCapture) {
			this.mCvCapture = mCvCapture;
			System.out.println("Creating " + getClass());
		}
		
		public GrabberThread(CvCapture mCvCapture, String videoFilePath) {
			this.mCvCapture = mCvCapture;
			
			System.out.println("Creating " + getClass() + " with Saving");
			
			saveVideo = true;
			int width = (int) opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH);
			int height = (int) opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT);
			mVideoWriter = opencv_highgui.cvCreateVideoWriter(
					"F:\\lol.avi",//videoFilePath, 
					-1, 
					opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FPS), 
					new CvSize(width, height), 
					1);
			
			if (mVideoWriter == null)
				System.out.println("Cannot create VideoWriter");
		}
		
		public synchronized void stopGrab() {
			canGrab = false;
			
			if (saveVideo) {
				opencv_highgui.cvReleaseVideoWriter(mVideoWriter);
			}
			opencv_highgui.cvReleaseCapture(mCvCapture);
		}
		
		public void run() {
			while (canGrab) {
				currentFrame = opencv_highgui.cvQueryFrame(mCvCapture);				
				
				synchronized (this) {
					if (saveVideo && mVideoWriter != null) {
						System.out.println("Recording Frame");
						opencv_highgui.cvWriteFrame(mVideoWriter, currentFrame);
						System.out.println("Frame recorded");
					}
					
					VideoStreamService.this.setChanged();
					VideoStreamService.this.notifyObservers(currentFrame);
				}
			}
		}
	}
}
