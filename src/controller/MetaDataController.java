package controller;

import java.util.Observable;
import java.util.Observer;

import de.th_wildau.quadroid.models.MetaData;

public class MetaDataController implements ViewController, Observer{

	private view.MetaDataView view;

	public view.MetaDataView getView() {
		if (this.view == null) {
			this.view = new view.MetaDataView(this);
			this.getService().addObserver(this);
		}
		
		return this.view;
	}
	
	private service.MetaDataService getService() {
		return service.MetaDataService.getInstance();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		MetaData metaData = this.getService().getMetaData();
		this.getView().setMetaData(metaData);
	}
}
