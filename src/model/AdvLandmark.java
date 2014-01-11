package model;

import helper.FileHelper;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.th_wildau.quadroid.models.Landmark;

/**
 * Subclass of {@link Landmark} which adds a method to save the landmark image to file.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class AdvLandmark extends Landmark {
	
	public AdvLandmark(Landmark landmark) {
		this.setMetaData(landmark.getMetaData());
		this.setPictureoflandmark(landmark.getPictureoflandmark());
	}
	
	public AdvLandmark() {}

	private File imageFile;
	
	public File getLandmarkPictureAsFile() {
        if (this.imageFile == null)
                imageFile = new File(FileHelper.getPendingAlertPath(""));
        try {
                ImageIO.write(getPictureoflandmark(), "png", imageFile);
        } catch (IOException e) {
                e.printStackTrace();
        }
        return imageFile;
	}
	
}
