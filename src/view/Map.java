package view;

import java.awt.Color;
import java.awt.Label;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Map extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map controller;
	private JLabel label;
	private view.helper.GoogleMaps googleMapsHelper;
	private BufferedImage map;
	private JLabel mapLabel;

	public Map(controller.Map controller) {
		this.setBackground(Color.blue);
		this.add(new Label("Map"));
		this.controller = controller;
		this.googleMapsHelper = new view.helper.GoogleMaps();
		
		this.label = new JLabel();
		this.add(this.label);
	}
	
	public void setGeoData(model.GeoData geoData) {
		this.label.setText(geoData.getLatitude() + " | " + geoData.getLongitude());
		this.googleMapsHelper.setGeoData(geoData);
		map = this.googleMapsHelper.getStaticImage();
		
		if (this.mapLabel != null) {
			this.remove(this.mapLabel);
		}
		
		if (map != null) {
			this.mapLabel = new JLabel(new ImageIcon(map));
			add(this.mapLabel);
		}
	}
	
	//http://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&sensor=false
}
