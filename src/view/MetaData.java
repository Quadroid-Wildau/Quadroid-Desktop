package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class MetaData extends JPanel{

	private controller.MetaData controller;
	
	public MetaData() {
		this.setBackground(Color.orange);
		this.add(new Label("MetaData"));
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
