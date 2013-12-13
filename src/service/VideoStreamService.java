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
	
	private static final int BITRATE = 20971520; //20 Mbit/s

	private static VideoStreamService instance;
	private VideoCommunicator mVideoCommunicator;
	private CvCapture mCvCapture;
	private GrabberThread mGrabberThread;
	private IplImage currentFrame;
	private FFmpegFrameRecorder rec;
	
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
		try {
			int width = (int) opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH);
			int height = (int) opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT);
			double fps = opencv_highgui.cvGetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FPS);
			
			System.out.println(filepath);
			rec = FFmpegFrameRecorder.createDefault(filepath, width, height);
			rec.setFrameRate(fps);
			
			rec.setVideoBitrate(BITRATE);
			
			//MPEG2 Codec
			rec.setVideoCodec(2);
			
			rec.start();
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopSaveVideoStream() {
		try {
			rec.stop();
			rec.release();
			rec = null;
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
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
			if (rec != null) {
				stopSaveVideoStream();
			}
			opencv_highgui.cvReleaseCapture(mCvCapture);
		}
		
		public void run() {
			while (canGrab) {
				currentFrame = opencv_highgui.cvQueryFrame(mCvCapture);				
				if (rec != null) {
					try {
						rec.record(currentFrame);
					} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
						e.printStackTrace();
					}
				}
				
				synchronized (this) {
					VideoStreamService.this.setChanged();
					VideoStreamService.this.notifyObservers(currentFrame);
				}
			}
		}
	}
}
