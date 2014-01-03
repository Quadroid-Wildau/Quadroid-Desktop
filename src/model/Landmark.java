package model;

import helper.FileHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Datenhaltungsmodell für Landmarkenalarme:
 * Enthält ein BufferedImage für den Snapshot der Landmarke und ein TData Model.
 */
public class Landmark {

	private MetaData tdata;

	private BufferedImage landmarkPicture;
	
	private File imageFile = null;

	public void setLmpic(BufferedImage lmpic) {
		this.landmarkPicture = lmpic;
	}

	public BufferedImage getLandmarkPicture() {
		return landmarkPicture;
	}

	public File getLandmarkPictureAsFile() {
		if (imageFile != null)
			imageFile = new File(FileHelper.getPendingAlertPath(""));
		try {
			ImageIO.write(landmarkPicture, "png", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageFile;
	}
	
	public void setTdata(MetaData tdata) {

	}

	public MetaData getTdata() {
		return null;
	}

}
