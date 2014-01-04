package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class Map3DView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map3DController controller;
	
	public Map3DView(controller.Map3DController controller) {
		this.setBackground(Color.green);
		this.add(new Label("Map3D"));
		this.controller = controller;
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
