package controller;

import javax.swing.JComponent;

import org.apache.commons.lang3.SystemUtils;

import view.MainView;

public class MainController implements ViewController{
	private MainView view;
	private ViewController mapController;
	private ViewController videoStreamController;
	private ViewController map3DController;
	private ViewController metaDataController;
	
	public MainController() {
	}
	
	@Override
	public JComponent getView() {
		if (view == null) {
			this.view = new view.MainView(this);
			this.view.setMap(this.getMapController().getView());
			
			//Because of incompatibility between Java3D Java 7 and Mac OSX, do not display the 3D view on Mac OSX
			if (!SystemUtils.IS_OS_MAC_OSX) {
				this.view.setMap3D(this.getMap3DController().getView());
			}
			
			this.view.setMetaData(this.getMetaDataController().getView());
			this.view.setVideoStream(this.getVideoStreamController().getView());
		}
		
		return this.view;
	}
	
	public ViewController getMapController() {
		if (this.mapController == null) {
			this.mapController = new controller.MapController();
		}
		
		return mapController;
	}

	public ViewController getVideoStreamController() {
		if (this.videoStreamController == null) {
			this.videoStreamController = new controller.VideoStreamController();
		}
		
		return videoStreamController;
	}

	public ViewController getMap3DController() {
		if (this.map3DController == null) {
			this.map3DController = new controller.Map3DController();
		}
		
		return map3DController;
	}

	public ViewController getMetaDataController() {
		if (this.metaDataController == null) {
			this.metaDataController = new controller.MetaDataController();
		}
		
		return metaDataController;
	}
}
