package communication.persistence;

import helper.DateFormatter;
import helper.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;

public class PhotoPersistence {
	
	public void saveScreenShot(String filepath, IplImage frame, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime() * 1000);
		}
		
		saveScreenShot(filepath, frame.getBufferedImage(), text);
	}
	
	public void saveScreenShot(String filepath, BufferedImage image, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime() * 1000);
		}
		
		saveScreenShot(filepath, image, text);
	}
	
	public void saveScreenShot(String filepath, BufferedImage image, String text) {		
		BufferedImage img = ImageHelper.drawTextOnImage(image, 10, image.getHeight() - 10, text);
		
		try {
			ImageIO.write(img, "png", new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
