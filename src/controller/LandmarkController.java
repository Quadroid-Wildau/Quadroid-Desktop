package controller;

import java.util.Observable;
import java.util.Observer;

import view.LandmarkView;

public class LandmarkController implements Observer {

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	public LandmarkView getView() {
		return new LandmarkView(this);
	}

}
