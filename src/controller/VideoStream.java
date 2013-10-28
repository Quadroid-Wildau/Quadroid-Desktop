package controller;

public class VideoStream {

	private view.VideoStream view;

	public view.VideoStream getView() {
		if (this.view == null) {
			this.view = new view.VideoStream();
		}
		
		return this.view;
	}

}
