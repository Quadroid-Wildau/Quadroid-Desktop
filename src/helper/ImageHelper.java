package helper;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;

public class ImageHelper {
	
	public static IplImage drawMetaDataOnIplImage(IplImage image, int x, int y, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime() * 1000);
		}
		
		IplImage img = image.clone();
		opencv_core.CvFont font = opencv_core.cvFont(0.7f, 1);
		CvPoint point = opencv_core.cvPoint(x, y);
		
		opencv_core.cvPutText(img, text, point, font, CvScalar.WHITE);
		
		return img;
	}
	
	public static BufferedImage drawMetaDataOnImage(BufferedImage image, int x, int y, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime() * 1000);
		}
		
		return drawTextOnImage(image, 10, image.getHeight() - 10, text);
	}

	public static BufferedImage drawTextOnImage(BufferedImage image, int x, int y, String text) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.setPaint(java.awt.Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString(text, 10, h - 10);
		g.dispose();
		
		return img;
	}
}
