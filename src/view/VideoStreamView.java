package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class VideoStreamView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unused")
	private controller.VideoStreamController controller;
	
	public VideoStreamView(controller.VideoStreamController controller) {
		this.setBackground(Color.cyan);
		this.add(new Label("Video Stream"));
		this.controller = controller;
	}

	public void showVideoStream(model.VideoStream videoStream) {

	}

}
