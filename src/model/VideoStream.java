package model;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VideoStream {

	private FrameGrabber mFrameGrabber = null;
	
	public VideoStream() {
		mFrameGrabber = new OpenCVFrameGrabber("");
		mFrameGrabber.setDeinterlace(true);
	}
	
	public VideoStream(int videoDeviceNumber) {
		if (videoDeviceNumber < 0)
			videoDeviceNumber = 0;
		
		mFrameGrabber = new OpenCVFrameGrabber(videoDeviceNumber);
		mFrameGrabber.setDeinterlace(true);
	}
	
	public VideoStream(String filename) {
		if (filename == null)
			throw new IllegalArgumentException("Filename can''t be null");
		
		mFrameGrabber = new OpenCVFrameGrabber(filename);
		mFrameGrabber.setDeinterlace(true);
	}
	
	public void startGrab() throws Exception {
		if (mFrameGrabber != null)
			mFrameGrabber.start();
	}
	
	public void stopGrab() throws Exception {
		if (mFrameGrabber != null)
			mFrameGrabber.stop();
	}
	
	public IplImage getNextFrame() throws Exception {
		if (mFrameGrabber != null) {
			return mFrameGrabber.grab();
		}
		return null;
	}
}
