package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class Map3D extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map3D controller;
	
	public Map3D(controller.Map3D controller) {
		this.setBackground(Color.green);
		this.add(new Label("Map3D"));
		this.controller = controller;
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
