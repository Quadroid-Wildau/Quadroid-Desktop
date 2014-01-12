package controller;

import java.util.Observable;
import java.util.Observer;

import service.MetaDataService;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;

import controller.interfaces.ViewController;
import de.th_wildau.quadroid.models.MetaData;

/**
 * Controller for Metadata view
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
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
	
	/**
	 * Gets the first {@link MetaData} object from {@link MetaDataService}
	 * @return
	 * 		the {@link Metadata} object
	 */
	public MetaData getFirstMetaData() {
		return getService().getFirstMetaData();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		MetaData metaData = getService().getMetaData();
		getView().setMetaData(metaData);
	}
}
