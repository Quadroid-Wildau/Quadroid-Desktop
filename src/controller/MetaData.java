package controller;

public class MetaData {

	private view.MetaData view;

	public view.MetaData getView() {
		if (this.view == null) {
			this.view = new view.MetaData();
		}
		
		return this.view;
	}
}
