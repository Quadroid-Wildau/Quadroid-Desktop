package communication;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

/**
 * 
 * This class represents the communicator for all video stuff. It initializes the capture device.
 * 
 * @author Georg Baumgarten
 *
 */
public class VideoCommunicator {
	
	private CvCapture mCvCapture;
	private int videoDevicePort;
	
	public VideoCommunicator(int videoDevicePort) throws Exception {
		if (videoDevicePort < 0)
			videoDevicePort = 0;
		
		this.videoDevicePort = videoDevicePort;
		
		mCvCapture = opencv_highgui.cvCreateCameraCapture(videoDevicePort);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FPS, 30);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
		opencv_highgui.cvSetCaptureProperty(mCvCapture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
	}
	
	/**
	 * get the capture device
	 * @return
	 * 		the capture
	 */
	public CvCapture getCvCapture() {
		return mCvCapture;
	}
	
	/**
	 * Returns the current video device port in use
	 * @return
	 * 		The port
	 */
	public int getVideoDevicePort() {
		return videoDevicePort;
	}
}
