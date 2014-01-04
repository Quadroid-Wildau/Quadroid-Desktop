package model;

import java.awt.Dimension;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VideoStream {

	private FrameGrabber mFrameGrabber;
	private IplImage currentFrame;
	private Dimension videoSize;
	
	public VideoStream(FrameGrabber frameGrabber) {
		mFrameGrabber = frameGrabber;
		mFrameGrabber.setDeinterlace(true);
		videoSize = new Dimension(mFrameGrabber.getImageWidth(), mFrameGrabber.getImageHeight());
	}
	
	public IplImage getNextFrame() throws Exception {
		if (mFrameGrabber != null) {
			currentFrame = mFrameGrabber.grab();
		}
		return currentFrame;
	}
	
	public void startGrab() throws Exception {
		mFrameGrabber.start();
	}
	
	public IplImage getCurrentFrame() {
		return currentFrame;
	}
	
	public Dimension getVideoSize() {
		return videoSize;
	}
}
