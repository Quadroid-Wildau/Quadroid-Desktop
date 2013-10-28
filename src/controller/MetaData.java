package controller;

public class MetaData implements ViewController{

	private view.MetaData view;

	public view.MetaData getView() {
		if (this.view == null) {
			this.view = new view.MetaData(this);
		}
		
		return this.view;
	}
}
