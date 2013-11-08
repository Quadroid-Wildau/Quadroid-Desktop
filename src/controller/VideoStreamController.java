package controller;

import java.awt.Component;

public class VideoStreamController implements ViewController{

	private view.VideoStreamView view;

	public Component getView() {
		if (this.view == null) {
			this.view = new view.VideoStreamView(this);
		}
		
		return this.view;
	}

}
