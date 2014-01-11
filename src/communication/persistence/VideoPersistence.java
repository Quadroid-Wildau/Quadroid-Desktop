package communication.persistence;

import helper.ImageHelper;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

import de.th_wildau.quadroid.models.MetaData;

public class VideoPersistence {
	
	private static final int BITRATE = 5 * 1024 * 1024; //5 Mbit/s
	
	private volatile FFmpegFrameRecorder mFrameRecorder;

	public void createRecorder(CvCapture capture, String filepath) throws Exception {
		int width = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH);
		int height = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT);
		
		mFrameRecorder = FFmpegFrameRecorder.createDefault(filepath, width, height);
		
		mFrameRecorder.setVideoBitrate(BITRATE);
		
		//MPEG2 Codec
		mFrameRecorder.setVideoCodec(2);
		
		mFrameRecorder.start();
	}
	 
	
	public void recordFrame(IplImage frame, MetaData metadata) throws Exception {
		if (mFrameRecorder != null) {
			IplImage img = ImageHelper.drawMetaDataOnIplImage(frame, 10, frame.getBufferedImage().getHeight() - 10, metadata);
			mFrameRecorder.record(img);
		}
	}
	
	public void stopRecord() {
		try {
			if (mFrameRecorder != null) {
				mFrameRecorder.stop();
				mFrameRecorder.release();
			}
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {}
		finally {
			mFrameRecorder = null;
		}
	}
}
