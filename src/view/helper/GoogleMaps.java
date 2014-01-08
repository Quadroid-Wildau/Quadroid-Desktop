package view.helper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import de.th_wildau.quadroid.models.GNSS;

public class GoogleMaps {
	//config
	private static int ZOOM_LEVEL = 4;
	private static int SIZE_WIDTH = 400;
	private static int SIZE_HEIGHT = 400;
	
	private GNSS geoData;
	private ArrayList<String> points;
	private ArrayList<String> markers;
	
	public GoogleMaps() {
		this.markers = new ArrayList<String>();
		this.points = new ArrayList<String>();
	}
	
	public BufferedImage getStaticImage() {
		BufferedImage image = null;
		URL url = this.getUrl();
		
		if (url != null) {
			try {
				image = ImageIO.read(url);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
		return image;
	}
	
	public void setGeoData(GNSS geoData) {
		this.geoData = geoData;
		this.setMarker(geoData);
		this.addLine(geoData);
	}
	
	private URL getUrl() {
		URL url = null;
		
		if(this.geoData != null) {
			String urlString = "http://maps.googleapis.com/maps/api/staticmap?center="+this.getCenterLocation()+"&zoom=" + ZOOM_LEVEL + "&size=" + SIZE_WIDTH + "x" + SIZE_HEIGHT + "&maptype=roadmap&sensor=false" + this.getMarkersString() + this.getLinesString();
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		return url;
	}
	
	private String getMarkersString() {
		String string = "";
		
		for (Iterator iterator = this.markers.iterator(); iterator.hasNext();) {
			string += iterator.next();
		}
		
		return string;
	}
	
	private String getLinesString() {
		String string = "&path=color:0xff0000ff|weight:5";
		
		for (Iterator iterator = this.points.iterator(); iterator.hasNext();) {
			string += iterator.next();
		}
		
		return string;
	}
	
	private void addMarker(GNSS geoData) {
		this.markers.add("&markers=color:blue%7Clabel:S%7C"+this.getStringFromGeodata(geoData));
	}
	
	private void setMarker(GNSS geoData) {
		this.markers = new ArrayList<String>();
		this.markers.add("&markers=color:blue%7Clabel:S%7C"+this.getStringFromGeodata(geoData));
	}
	
	private void addLine(GNSS geoData) {
		this.points.add("|" + this.getStringFromGeodata(geoData));
	}
	
	private String getStringFromGeodata(GNSS geoData) {
		return geoData.getLatitude() + "," + geoData.getLongitude();
	}
	
	private String getCenterLocation(){
		return getStringFromGeodata(this.geoData);
	}
}
