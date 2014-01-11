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

/**
 * This class provides some basic methods for image manipulation
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class ImageHelper {
	
	/**
	 * Draws {@link MetaData} on {@link IplImage}
	 * @param image
	 * 			The image
	 * @param x
	 * 			Text begin x coord
	 * @param y
	 * 			Text begin y coord
	 * @param metadata
	 * 			The meta data
	 * @return
	 * 			{@link IplImage} with text
	 */
	public static IplImage drawMetaDataOnIplImage(IplImage image, int x, int y, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime());
		}
		
		IplImage img = image.clone();
		opencv_core.CvFont font = opencv_core.cvFont(0.7f, 1);
		CvPoint point = opencv_core.cvPoint(x, y);
		
		opencv_core.cvPutText(img, text, point, font, CvScalar.WHITE);
		
		return img;
	}
	
	/**
	 * Draws {@link MetaData} on {@link BufferedImage}
	 * @param image
	 * 			The image
	 * @param x
	 * 			Text begin x coord
	 * @param y
	 * 			Text begin y coord
	 * @param metadata
	 * 			The meta data
	 * @return
	 * 			{@link BufferedImage} with text
	 */
	public static BufferedImage drawMetaDataOnImage(BufferedImage image, int x, int y, MetaData metadata) {
		String text = "";
		
		if (metadata != null) {
			GNSS gnss = metadata.getAirplane().GeoData();
			
			if (gnss != null)
				text = "Latitude: " + gnss.getLatitude() + 
					", Longitude: " + gnss.getLongitude() + 
					", Hoehe: " + gnss.getHeight() +
					", Zeit: " + DateFormatter.formatDate(metadata.getAirplane().getTime());
		}
		
		return drawTextOnImage(image, 10, image.getHeight() - 10, text);
	}

	/**
	 * Draws {@link String} on {@link BufferedImage}
	 * @param image
	 * 			The image
	 * @param x
	 * 			Text begin x coord
	 * @param y
	 * 			Text begin y coord
	 * @param metadata
	 * 			The meta data
	 * @return
	 * 			{@link BufferedImage} with text
	 */
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
