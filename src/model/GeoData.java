package model;

/**
 * Datenhaltungsmodell für die Geodaten:
 * Längengrad, Breitengrad, Flughöhe
 */
public class GeoData {

	private Float latitude;
	private Float longitude;
	private Float height;
	
	public Float getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	public Float getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
	public Float getHeight() {
		return height;
	}
	
	public void setHeight(Float height) {
		this.height = height;
	}
}
