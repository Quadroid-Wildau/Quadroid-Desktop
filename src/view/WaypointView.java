package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.interfaces.WaypointDeletedInterface;
import controller.MapController;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Waypoint;

public class WaypointView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> model;
	private JList<String> list;
	private WaypointDeletedInterface deletedListener;
	private Waypoint[] waypoints;
	private MapController controller;

	public WaypointView(WaypointDeletedInterface deletedListener, MapController controller) {
		setTitle("Wegpunkte");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 480);
		setPreferredSize(new Dimension(640, 480));
		setLocationRelativeTo(null);
		
		this.controller = controller;
		this.deletedListener = deletedListener;
		
		waypoints = controller.getWaypointsFromFlightCtrl();
		
		list = new JList<String>();
		list.addKeyListener(mKeyListener);
		getContentPane().add(list, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnSchlieen = new JButton("Schlie\u00DFen");
		btnSchlieen.setActionCommand("close");
		btnSchlieen.addActionListener(mButtonListener);
		panel.add(btnSchlieen, BorderLayout.EAST);
		
		JButton btnDeleteSelected = new JButton("Ausgew\u00E4hlte L\u00F6schen");
		btnDeleteSelected.setActionCommand("deleteSelected");
		btnDeleteSelected.addActionListener(mButtonListener);
		panel.add(btnDeleteSelected, BorderLayout.CENTER);
		
		showWaypoints();
		setVisible(true);
	}

	private void showWaypoints() {
		model = new DefaultListModel<String>();
		for (int i = 0; i < waypoints.length; i++) {
			final GNSS gnss = waypoints[i].getPosition();
			model.addElement("[" + (i+1) + "] Latitude: " + gnss.getLatitude() + ", Longitude: " + gnss.getLongitude() + ", Hoehe: " + gnss.getHeight());
		}
		list.setModel(model);
	}
	
	private void deleteSelectedWaypointsWithConfirmation() {
		int selection = JOptionPane.showConfirmDialog(WaypointView.this, 
				"Sollen die ausgew\u00E4hlten Wegpunkte wirklich gel\u00F6scht werden?", 
				"Wegpunkte l\u00F6schen", 
				JOptionPane.YES_NO_OPTION);

		if (selection == JOptionPane.YES_OPTION) {
			for (int i : list.getSelectedIndices()) {
				if (deletedListener != null) deletedListener.onWaypointDeleted(i);
			}
			
			//reload waypoints
			waypoints = controller.getWaypointsFromFlightCtrl();
			showWaypoints();
		}
	}
	
	private ActionListener mButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("close")) {
				dispose();
			} else if (e.getActionCommand().equals("deleteSelected")) {
				deleteSelectedWaypointsWithConfirmation();
			}
		}
	};
	
	private KeyListener mKeyListener = new KeyListener() {
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				deleteSelectedWaypointsWithConfirmation();
			}
		}
	};
}
