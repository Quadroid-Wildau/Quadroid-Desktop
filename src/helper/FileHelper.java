package helper;

import java.io.File;

public class FileHelper {

	public static String getPredefinedVideoPath(String baseDir) {
		if (baseDir == null || baseDir.equals("")) {
			baseDir = System.getProperty("user.home");
		}
		
		File folder = new File(baseDir);
		int i = 0;
		
		String filename = "quadroid_video_" + i + ".avi";
		File file = new File(folder, filename);
		
		while (file.exists()) {
			i++;
			filename = "quadroid_video_" + i + ".avi";
			file = new File(folder, filename);
		}
		
		return file.getAbsolutePath();
	}
	
	public static String getPredefinedScreenshotPath(String baseDir) {
		if (baseDir == null || baseDir.equals("")) {
			baseDir = System.getProperty("user.home");
		}
		
		File folder = new File(baseDir);
		int i = 0;
		
		String filename = "quadroid_screenshot_" + i + ".bmp";
		File file = new File(folder, filename);
		
		while (file.exists()) {
			i++;
			filename = "quadroid_screenshot_" + i + ".bmp";
			file = new File(folder, filename);
		}
				
		return file.getAbsolutePath();
	}
}
