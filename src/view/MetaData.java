package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class MetaData extends JPanel{
	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unused")
	private controller.MetaData controller;
	
	public MetaData(controller.MetaData controller) {
		this.setBackground(Color.orange);
		this.add(new Label("MetaData"));
		this.controller = controller;
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
