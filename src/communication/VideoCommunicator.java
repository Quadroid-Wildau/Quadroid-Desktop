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
		
		mCvCapture = opencv_highgui.cvCreateCameraCapture(videoDevicePort);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FPS, 25);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
	}
	
	public CvCapture getCvCapture() {
		return mCvCapture;
	}
	
	public int getVideoDevicePort() {
		return videoDevicePort;
	}
}
