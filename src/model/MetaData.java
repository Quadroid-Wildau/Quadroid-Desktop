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

	public Long getTime() {
		return time;
	}


	public void setTime(Long time) {
		this.time = time;
	}


	public Float getYaw() {
		return yaw;
	}


	public void setYaw(Float yaw) {
		this.yaw = yaw;
	}


	public Float getRoll() {
		return roll;
	}


	public void setRoll(Float roll) {
		this.roll = roll;
	}


	public Float getPitch() {
		return pitch;
	}


	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}


	public Float getBatteryState() {
		return batteryState;
	}


	public void setBatteryState(Float batteryState) {
		this.batteryState = batteryState;
	}


	public Float getSpeed() {
		return speed;
	}


	public void setSpeed(Float speed) {
		this.speed = speed;
	}


	public Float getCourse() {
		return course;
	}


	public void setCourse(Float course) {
		this.course = course;
	}


	public void setGeodata(GeoData geodata) {
		this.geodata = geodata;
	}

	public GeoData getGeodata() {
		return this.geodata;
	}
}
