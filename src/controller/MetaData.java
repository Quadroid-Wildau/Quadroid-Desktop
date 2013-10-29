package controller;

import java.util.Observable;
import java.util.Observer;

public class MetaData implements ViewController, Observer{

	private view.MetaData view;

	public view.MetaData getView() {
		if (this.view == null) {
			this.view = new view.MetaData(this);
			this.getService().addObserver(this);
		}
		
		return this.view;
	}
	
	private service.MetaData getService() {
		return service.MetaData.getInstance();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		model.MetaData metaData = this.getService().getMetaData();
		this.getView().setMetaData(metaData);
	}
}
