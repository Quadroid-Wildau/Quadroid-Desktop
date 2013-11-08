package communication;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.FrameGrabber.ImageMode;
import com.googlecode.javacv.OpenCVFrameGrabber;

public class VideoCommunicator {
	
	private FrameGrabber mFrameGrabber = null;
	private int videoDevicePort;
	
	public VideoCommunicator(int videoDevicePort) throws Exception {
		if (videoDevicePort < 0)
			videoDevicePort = 0;
		
		this.videoDevicePort = videoDevicePort;
		mFrameGrabber = new OpenCVFrameGrabber(videoDevicePort);
		mFrameGrabber.setImageMode(ImageMode.COLOR);
		mFrameGrabber.setDeinterlace(true);
	}
	
	public FrameGrabber getFrameGrabber() {
		return mFrameGrabber;
	}
	
	public int getVideoDevicePort() {
		return videoDevicePort;
	}
}
