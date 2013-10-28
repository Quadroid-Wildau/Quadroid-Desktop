package view;

import java.awt.Color;
import java.awt.Label;

import javax.swing.JPanel;

public class VideoStream extends JPanel{

	private controller.VideoStream controller;
	
	public VideoStream() {
		this.setBackground(Color.cyan);
		this.add(new Label("Video Stream"));
	}

	public void showVideoStream(model.VideoStream videoStream) {

	}

}
