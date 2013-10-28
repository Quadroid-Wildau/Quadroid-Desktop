package model;

/**
 * Datenhaltungsmodel für die Telemetriedaten:
 * Zeit, Neigung um X-, Y-, Z- Achse, Batteriestand, Geschwindigkeit und ein GeoDataModel.
 */
public class MetaData {

	private GeoData geodata;

	private Long time;

	private Float yaw;

	private Float roll;

	private Float pitch;

	private Float batteryState;

	private Float speed;

	private Float course;

	public void setGeodata(GeoData geodata) {

	}

	public GeoData getGeodata() {
		return null;
	}

	public long getTime() {
		return 0;
	}

	public void setYaw(float yaw) {

	}

	public float getYaw() {
		return 0;
	}

	public void setPitch(float pitch) {

	}

	public float getPitch() {
		return 0;
	}

	public void setRoll(float roll) {

	}

	public float getRoll() {
		return 0;
	}

	public void setBatteryState(float batteryState) {

	}

	public float getBatteryState() {
		return 0;
	}

	public void setTime(long time) {

	}

	public void setSpeed(Float speed) {

	}

	public Float getSpeed() {
		return null;
	}

	public void setCourse(Float course) {

	}

	public Float getCourse() {
		return null;
	}

}
