package model;

/**
 * Datenhaltungsmodell für die Geodaten:
 * Längengrad, Breitengrad, Flughöhe
 */
public class GeoData {

	private float latitude;
	private float longitude;
	private float height;
	
	public GeoData() {}
	
	public GeoData(float latitude, float longitude, float height) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.height = height;
	}

	public float getLatitude() {
		return latitude;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
}
