package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import service.FlightControlService;
import de.th_wildau.quadroid.models.Waypoint;

public class WaypointView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<Waypoint> model;
	private JList<Waypoint> list;

	public WaypointView() {
		setTitle("Wegpunkte");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 480);
		setPreferredSize(new Dimension(640, 480));
		
		list = new JList<Waypoint>();
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
		
		loadWaypoints();
		setVisible(true);
	}

	private void loadWaypoints() {
		model = new DefaultListModel<Waypoint>();
		for (Waypoint wp : FlightControlService.getInstance().getWaypoints()) {
			model.addElement(wp);
		}
		list.setModel(model);
	}
	
	private ActionListener mButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("close")) {
				dispose();
			} else if (e.getActionCommand().equals("deleteSelected")) {
				for (Waypoint wp : list.getSelectedValuesList()) {
					FlightControlService.getInstance().deleteWaypoint(wp);
					model.removeElement(wp);
				}
				list.setModel(model);
			}
		}
	};
}
