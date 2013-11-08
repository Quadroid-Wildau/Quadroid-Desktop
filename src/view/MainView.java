package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import controller.MainController;

public class MainView extends JPanel {

	@SuppressWarnings("unused")
	private controller.MainController controller;
	
	private static final long serialVersionUID = 1L;
	
	private Component mapView;
	private Component map3DView;
	private Component metaDataView;
	private Component videoStreamView;
	private JPanel rightSubPanel;	
	
	public MainView(MainController controller) {
		this.controller = controller;
		this.setBackground(Color.red);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(this.getRightSubPanel(), java.awt.BorderLayout.EAST);
	}
	
	private JPanel getRightSubPanel() {
		if (this.rightSubPanel == null) {			
			this.rightSubPanel = new JPanel();
			this.rightSubPanel.setLayout(new BorderLayout());
			this.rightSubPanel.setPreferredSize(new Dimension(400, 9000));
		}
		
		return this.rightSubPanel;
	}
	
	public void setMap(Component view) {
		if (this.mapView != view) {
			if (this.mapView != null) {				
				this.remove(this.mapView);
			}
			
			this.mapView = view;	
			this.mapView.setPreferredSize(new Dimension(400, 400));
		}
		
		this.rightSubPanel.add(this.mapView, java.awt.BorderLayout.NORTH);
	}
	
	public void setMap3D(Component view) {
		if (this.map3DView != view) {
			if(this.map3DView != null) { 
				this.remove(this.map3DView);
			}
			
			this.map3DView = view;
			this.map3DView.setPreferredSize(new Dimension(400, 400));
		}
		
		this.rightSubPanel.add(this.map3DView, java.awt.BorderLayout.CENTER);
	}
	
	public void setMetaData(Component view) {
		if (this.metaDataView != view) {
			if (this.metaDataView != null) {				
				this.remove(this.metaDataView);
			}
			
			this.metaDataView = view;		
			this.metaDataView.setPreferredSize(new Dimension(900, 160));
		}
		
		this.add(this.metaDataView, java.awt.BorderLayout.SOUTH);
	}
	
	public void setVideoStream(Component view) {
		if (this.videoStreamView != view) {
			if (this.videoStreamView != null) {				
				this.remove(this.videoStreamView);
			}
			
			this.videoStreamView = view;
			this.videoStreamView.setPreferredSize(new Dimension(600, 400));
		}
		
		this.add(this.videoStreamView, java.awt.BorderLayout.CENTER);
	}
}
