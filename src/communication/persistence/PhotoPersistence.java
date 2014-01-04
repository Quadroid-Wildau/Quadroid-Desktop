package communication.persistence;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

public class PhotoPersistence {
	
	public void saveScreenShot(String filepath, IplImage frame) {
		opencv_highgui.cvSaveImage(filepath, frame);
	}
}
