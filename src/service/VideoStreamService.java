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

/**
 * 
 * The service class for all video stuff. Grabbing frames, recording frames and so on is taking place
 * here. 
 * Singleton implementation
 * 
 * @see
 * 	VideoCommunicator
 * 
 * @author Georg Baumgarten
 *
 */
public class VideoStreamService extends Observable {
	
	private static VideoStreamService instance;
	
	private VideoCommunicator mVideoCommunicator;
	private CvCapture mCvCapture;
	private GrabberThread mGrabberThread;
	private IplImage currentFrame;
	private FFmpegFrameRecorder mFrameRecorder;
	private volatile boolean canRecord = false;
	
	private VideoStreamService() {}
	
	public static VideoStreamService getInstance() {
		if (instance == null)
			instance = new VideoStreamService();
		
		return instance;
	}
	
	/**
	 * Start grabbing frames from capture device at device port
	 * @param videoDevicePort
	 * 			The device port
	 * @throws Exception
	 */
	public void startVideoStream(int videoDevicePort) throws Exception {
		mVideoCommunicator = getVideoCommunicator(videoDevicePort);
		mCvCapture = mVideoCommunicator.getCvCapture();
		
		//just in case...
		stopVideoStream();
		
		mGrabberThread = new GrabberThread(mCvCapture);
		mGrabberThread.start();
	}
	
	/**
	 * This will stop grabbing frames and will stop recording
	 */
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

	/**
	 * Start to save the video stream to a file
	 * @param filepath
	 * 			The file to save the video to
	 */
	public void saveVideoStream(String filepath) {
		try {
			mFrameRecorder = CommunicationStack.getInstance().getVideoPersistance().createRecorder(mCvCapture, filepath);
			mFrameRecorder.start();
			
			canRecord = true;
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stop saving video stream if recording is enabled at the moment
	 */
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
	
	/**
	 * gets the current video frame
	 * @return
	 * 		the frame
	 */
	public IplImage getCurrentFrame() {
		return currentFrame;
	}
	
	/**
	 * Get a list of all available capture devices. This will only work on Windows!
	 * @return
	 * 		String array with capture device names. The index in the array is the device port.
	 */
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
				//get frame from capture device
				currentFrame = opencv_highgui.cvQueryFrame(mCvCapture);			
				
				//record the frame if recording is enabled at the moment
				if (canRecord && mFrameRecorder != null) {
					try {
						mFrameRecorder.record(currentFrame);
					} catch (com.googlecode.javacv.FrameRecorder.Exception e) {}
				}
				
				//notify all observers about the new frame
				synchronized (this) {
					VideoStreamService.this.setChanged();
					VideoStreamService.this.notifyObservers(currentFrame);
				}
			}
		}
	}
}
