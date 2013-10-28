package controller;

public class Map3D implements ViewController{

	private view.Map3D view;

	public view.Map3D getView() {
		if (this.view == null) {
			this.view = new view.Map3D();
		}
		
		return this.view;
	}

}
