package controller;

public class Map3DController implements ViewController{

	private view.Map3DView view;

	public view.Map3DView getView() {
		if (this.view == null) {
			this.view = new view.Map3DView(this);
		}
		
		return this.view;
	}

}
