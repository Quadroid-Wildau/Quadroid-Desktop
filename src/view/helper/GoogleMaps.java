package view.helper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GoogleMaps {
	//config
	private static int ZOOM_LEVEL = 4;
	private static int SIZE_WIDTH = 400;
	private static int SIZE_HEIGHT = 400;
	
	private model.GeoData geoData;
	
	public BufferedImage getStaticImage() {
		BufferedImage image = null;
		URL url = this.getUrl();
		
		if (url != null) {
			try {
				image = ImageIO.read(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return image;
	}
	
	public void setGeoData(model.GeoData geoData) {
		this.geoData = geoData;
	}
	
	private URL getUrl() {
		URL url = null;
		
		if(this.geoData != null) {
			String urlString = "http://maps.googleapis.com/maps/api/staticmap?center="+this.getSeperatedLocation()+"&zoom=" + ZOOM_LEVEL + "&size=" + SIZE_WIDTH + "x" + SIZE_HEIGHT + "&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+this.getSeperatedLocation()+"&sensor=false";
			System.out.println(urlString); 
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		return url;
	}
	
	private String getSeperatedLocation(){
		return this.geoData.getLatitude() + "," + this.geoData.getLongitude();
	}
}
