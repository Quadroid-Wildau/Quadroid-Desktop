package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class MetaDataView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unused")
	private controller.MetaDataController controller;
	private Label position;
	private Label positionHeadline;
	private Label powerHeadline;
	private Label power;
	private Label heightHeadline;
	private Label height;
	private Label timeHeadline;
	private Label time;
	private Label speedHeadline;
	private Label speed;
	private Label courseHeadline;
	private Label course;
	private JPanel subPanel;
	
	public MetaDataView(controller.MetaDataController controller) {
		this.setBackground(Color.orange);
		this.controller = controller;
		
		this.positionHeadline = new Label("Position: ");
		this.position = new Label();
		this.add(this.positionHeadline);
		this.add(this.position);
		
		this.powerHeadline = new Label("Akku: ");
		this.power = new Label();
		this.add(this.powerHeadline);
		this.add(this.power);
		
		this.heightHeadline = new Label("Hšhe: ");
		this.height = new Label();
		this.add(this.heightHeadline);
		this.add(this.height);
		
		this.timeHeadline = new Label("Zeit: ");
		this.time = new Label();
		this.add(this.timeHeadline);
		this.add(this.time);
		
		this.speedHeadline = new Label("Geschwindigkeit: ");
		this.speed = new Label();
		this.add(this.speedHeadline);
		this.add(this.speed);
		
		this.courseHeadline = new Label("Kurs: ");
		this.course = new Label();
		this.add(this.courseHeadline);
		this.add(this.course);
		
		this.subPanel = new JPanel(new BorderLayout());
	}

	public void setMetaData(model.MetaData metaData) {
		this.position.setText(metaData.getGeodata().getLatitude() + " | " + metaData.getGeodata().getLongitude());
		this.power.setText(metaData.getBatteryState() + "% ");
		this.height.setText(metaData.getGeodata().getHeight() + "m");
		this.time.setText(metaData.getTime() + "s");
		this.speedHeadline.setText(metaData.getSpeed() + "km/h");
		this.course.setText(metaData.getCourse() + "");
	}
}
