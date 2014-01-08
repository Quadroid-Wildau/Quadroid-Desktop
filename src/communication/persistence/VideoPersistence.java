package communication.persistence;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

public class VideoPersistence {
	
	private static final int BITRATE = 20 * 1024 * 1024; //20 Mbit/s

	public FFmpegFrameRecorder createRecorder(CvCapture capture, String filepath) throws Exception {
		int width = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH);
		int height = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT);
		double fps = opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FPS);
		
		FFmpegFrameRecorder mFrameRecorder = FFmpegFrameRecorder.createDefault(filepath, width, height);
		mFrameRecorder.setFrameRate(fps);
		
		mFrameRecorder.setVideoBitrate(BITRATE);
		
		//MPEG2 Codec
		mFrameRecorder.setVideoCodec(2);
		
		return mFrameRecorder;
	}
}
