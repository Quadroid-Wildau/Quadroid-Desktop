package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class MetaDataView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unused")
	private controller.MetaDataController controller;
	private JLabel lblMetadaten;
	private JPanel panel;
	private JLabel lblPosition;
	private JLabel lblZeit;
	private JLabel lblGeschwindigkeit;
	private JLabel lblAkku;
	private JLabel lblHoehe;
	private JLabel lblKurs;
	private JTextField position;
	private JTextField time;
	private JTextField power;
	private JTextField speed;
	private JTextField height;
	private JTextField course;
	
	public MetaDataView(controller.MetaDataController controller) {
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		this.controller = controller;
		setLayout(new BorderLayout(0, 0));
		lblMetadaten = new JLabel("Metadaten");
		lblMetadaten.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		lblMetadaten.setFont(new Font("Tahoma", Font.BOLD, 18));
		add(lblMetadaten, BorderLayout.NORTH);
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 3, 10, 0));
		
		lblPosition = new JLabel("Position:");
		lblPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPosition.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblPosition.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(lblPosition);
		
		position = new JTextField();
		position.setBackground(Color.WHITE);
		position.setEditable(false);
		position.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(position);
		position.setColumns(10);
		
		lblAkku = new JLabel("Akku:");
		lblAkku.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblAkku);
		
		power = new JTextField();
		power.setBackground(Color.WHITE);
		power.setEditable(false);
		power.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(power);
		power.setColumns(10);
		
		lblHoehe = new JLabel("Hoehe:");
		lblHoehe.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblHoehe);
		
		height = new JTextField();
		height.setBackground(Color.WHITE);
		height.setEditable(false);
		height.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(height);
		height.setColumns(10);
		
		lblZeit = new JLabel("Zeit:");
		lblZeit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZeit.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(lblZeit);
		
		time = new JTextField();
		time.setBackground(Color.WHITE);
		time.setEditable(false);
		time.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(time);
		time.setColumns(10);
		
		lblGeschwindigkeit = new JLabel("Geschwindigkeit:");
		lblGeschwindigkeit.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblGeschwindigkeit);
		
		speed = new JTextField();
		speed.setBackground(Color.WHITE);
		speed.setEditable(false);
		speed.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(speed);
		speed.setColumns(10);
		
		lblKurs = new JLabel("Kurs:");
		lblKurs.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblKurs);
		
		course = new JTextField();
		course.setBackground(Color.WHITE);
		course.setEditable(false);
		course.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(course);
		course.setColumns(10);
	}

	public void setMetaData(model.MetaData metaData) {
		this.position.setText(metaData.getGeodata().getLatitude() + " | " + metaData.getGeodata().getLongitude());
		this.power.setText(metaData.getBatteryState() + "% ");
		this.height.setText(metaData.getGeodata().getHeight() + "m");
		this.time.setText(metaData.getTime() + "s");
		this.speed.setText(metaData.getSpeed() + "km/h");
		this.course.setText(metaData.getCourse() + "");
	}
}
