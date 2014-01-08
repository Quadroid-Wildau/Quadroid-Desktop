package helper;

import java.io.File;

/**
 * 
 * This class provides helper methods for the file system.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class FileHelper {

	/**
	 * Generates a filepath that can be used to save a video file. 
	 * 
	 * @param baseDir
	 * 			Predefined directory for the videopath. If null or empty, the
	 * 			users home dir is used
	 * @return
	 * 			String representation of the absolute path to the video file
	 */
	public static String getPredefinedVideoPath(String baseDir) {
		if (baseDir == null || baseDir.equals("")) {
			baseDir = System.getProperty("user.home");
		}
		
		File folder = new File(baseDir);
		int i = 0;
		
		String filename = "quadroid_video_" + i + ".mpg";
		File file = new File(folder, filename);
		
		while (file.exists()) {
			i++;
			filename = "quadroid_video_" + i + ".mpg";
			file = new File(folder, filename);
		}
		
		return file.getAbsolutePath();
	}
	
	/**
	 * Generates a filepath that can be used to save an image
	 * 
	 * @param baseDir
	 * 			Predefined directory for the image. If null or empty, the
	 * 			users home dir is used
	 * @return
	 * 			String representation of the absolute path to the image
	 */
	public static String getPredefinedScreenshotPath(String baseDir) {
		if (baseDir == null || baseDir.equals("")) {
			baseDir = System.getProperty("user.home");
		}
		
		File folder = new File(baseDir);
		int i = 0;
		
		String filename = "quadroid_screenshot_" + i + ".png";
		File file = new File(folder, filename);
		
		while (file.exists()) {
			i++;
			filename = "quadroid_screenshot_" + i + ".png";
			file = new File(folder, filename);
		}
				
		return file.getAbsolutePath();
	}
	
	/**
	 * Generate a filepath that can be used to store the image of a pending landmark alert.
	 * 
	 * @param baseDir
	 * 			Predefined directory
	 * @return
	 * 			String representation of the absolute path to the landmark image file
	 */
	public static String getPendingAlertPath(String baseDir) {
		if (baseDir == null || baseDir.equals("")) {
			baseDir = System.getProperty("user.home");
		}
		
		File folder = new File(baseDir);
		int i = 0;
		
		String filename = "quadroid_pending_alert_" + i + ".png";
		File file = new File(folder, filename);
		
		while (file.exists()) {
			i++;
			filename = "quadroid_pending_alert_" + i + ".png";
			file = new File(folder, filename);
		}
				
		return file.getAbsolutePath();
	}
}
