package service;

import java.util.Observable;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import com.googlecode.javacv.cpp.videoInputLib.videoInput;
import communication.CommunicationStack;
import communication.VideoCommunicator;

public class VideoStreamService extends Observable {
	
	

	private static VideoStreamService instance;
	
	private VideoCommunicator mVideoCommunicator;
	private CvCapture mCvCapture;
	private GrabberThread mGrabberThread;
	private IplImage currentFrame;
	private FFmpegFrameRecorder mFrameRecorder;
	private volatile boolean canRecord = false;
	
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
		canRecord = false;
		if (mGrabberThread != null) {
			mGrabberThread.stopGrab();
			mGrabberThread.interrupt();
		}
	}
	
	private VideoCommunicator getVideoCommunicator(int videoDevicePort) throws Exception {
		return CommunicationStack.getInstance().getVideoStreamCommunicator(videoDevicePort);
	}

	public void saveVideoStream(String filepath) {
		try {
			mFrameRecorder = CommunicationStack.getInstance().getVideoPersistance().createRecorder(mCvCapture, filepath);
			mFrameRecorder.start();
			
			canRecord = true;
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopSaveVideoStream() {
		canRecord = false;
		try {
			mFrameRecorder.stop();
			mFrameRecorder.release();
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {}
		finally {
			mFrameRecorder = null;
		}
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
		
		public GrabberThread(CvCapture mCvCapture) {
			this.mCvCapture = mCvCapture;
		}
		
		public synchronized void stopGrab() {
			canGrab = false;
			if (mFrameRecorder != null) {
				stopSaveVideoStream();
			}
			opencv_highgui.cvReleaseCapture(mCvCapture);
		}
		
		public void run() {
			while (canGrab) {
				currentFrame = opencv_highgui.cvQueryFrame(mCvCapture);				
				if (canRecord && mFrameRecorder != null) {
					try {
						mFrameRecorder.record(currentFrame);
					} catch (com.googlecode.javacv.FrameRecorder.Exception e) {}
				}
				
				synchronized (this) {
					VideoStreamService.this.setChanged();
					VideoStreamService.this.notifyObservers(currentFrame);
				}
			}
		}
	}
}
