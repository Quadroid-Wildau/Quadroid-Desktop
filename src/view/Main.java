package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class Main extends JPanel {

	private controller.Main controller;
	
	private static final long serialVersionUID = 1L;
	
	private Component mapView;
	private Component map3DView;
	private Component metaDataView;
	private Component videoStreamView;
	private JPanel rightSubPanel; 
	
	public Main(controller.Main controller) {
		this.setBackground(Color.red);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.rightSubPanel = new JPanel();
		this.rightSubPanel.setLayout(new BorderLayout());
		this.add(this.rightSubPanel, java.awt.BorderLayout.EAST);
	}
	
	public void setMap(Component view) {
		if (this.mapView != view) {
			if (this.mapView != null) {				
				this.remove(this.mapView);
			}
			
			this.mapView = view;			
		}
		
		this.rightSubPanel.add(this.mapView, java.awt.BorderLayout.NORTH);
	}
	
	public void setMap3D(Component view) {
		if (this.map3DView != view) {
			if(this.map3DView != null) { 
				this.remove(this.map3DView);
			}
			
			this.map3DView = view;			
		}
		
		this.rightSubPanel.add(this.map3DView, java.awt.BorderLayout.SOUTH);
	}
	
	public void setMetaData(Component view) {
		if (this.metaDataView != view) {
			if (this.metaDataView != null) {				
				this.remove(this.metaDataView);
			}
			
			this.metaDataView = view;			
		}
		
		this.add(this.metaDataView, java.awt.BorderLayout.SOUTH);
	}
	
	public void setVideoStream(Component view) {
		if (this.videoStreamView != view) {
			if (this.videoStreamView != null) {				
				this.remove(this.videoStreamView);
			}
			
			this.videoStreamView = view;
			this.videoStreamView.setSize(400, 300);
		}
		
		this.add(this.videoStreamView, java.awt.BorderLayout.CENTER);
	}
}
