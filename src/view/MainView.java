package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
		setDoubleBuffered(true);
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JPanel getRightSubPanel() {
		if (this.rightSubPanel == null) {			
			this.rightSubPanel = new JPanel();
			this.rightSubPanel.setLayout(new BoxLayout(rightSubPanel, BoxLayout.Y_AXIS));
			this.rightSubPanel.setPreferredSize(new Dimension(600, 0));
			this.rightSubPanel.setMinimumSize(new Dimension(200, 0));
		}
		
		return this.rightSubPanel;
	}
	
	public void setVideoStream(JComponent view) {
		if (this.videoStreamView != view) {
			if (this.videoStreamView != null) {				
				this.remove(this.videoStreamView);
			}
			
			this.videoStreamView = view;
		}
		
		this.add(this.videoStreamView, java.awt.BorderLayout.CENTER);
	}
	
	public void setMap(JComponent view) {
		if (this.mapView != view) {
			if (this.mapView != null) {				
				this.remove(this.mapView);
			}
			
			this.mapView = view;
		}
		
		this.rightSubPanel.add(this.mapView, java.awt.BorderLayout.CENTER);
	}
	
	public void setMap3D(JComponent view) {
		if (this.map3DView != view) {
			if(this.map3DView != null) { 
				this.remove(this.map3DView);
			}
			
			this.map3DView = view;
		}
		
		this.rightSubPanel.add(this.map3DView, java.awt.BorderLayout.SOUTH);
	}
	
	public void setMetaData(JComponent view) {
		if (this.metaDataView != view) {
			if (this.metaDataView != null) {				
				this.remove(this.metaDataView);
			}
			
			this.metaDataView = view;
		}
		
		this.add(this.metaDataView, java.awt.BorderLayout.SOUTH);
	}
}
