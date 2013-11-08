package communication;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

public class VideoCommunicator {
	
	private CvCapture mCvCapture;
	private int videoDevicePort;
	
	public VideoCommunicator(int videoDevicePort) throws Exception {
		if (videoDevicePort < 0)
			videoDevicePort = 0;
		
		this.videoDevicePort = videoDevicePort;
		
		mCvCapture = opencv_highgui.cvCreateCameraCapture(0);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 600);
        opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 800);
        opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FPS, 30);
	}
	
	public CvCapture getCvCapture() {
		return mCvCapture;
	}
	
	public int getVideoDevicePort() {
		return videoDevicePort;
	}
}
