package controller;

import java.awt.Component;

public class VideoStream implements ViewController{

	private view.VideoStream view;

	public Component getView() {
		if (this.view == null) {
			this.view = new view.VideoStream(this);
		}
		
		return this.view;
	}

}
