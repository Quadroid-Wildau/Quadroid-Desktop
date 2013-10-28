package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

import model.MetaData;

public class Map3D extends JPanel{
	private controller.Map3D controller;
	
	public Map3D() {
		this.setBackground(Color.green);
		this.add(new Label("Map3D"));
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
