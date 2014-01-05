package view.custom;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MapPopupMenu extends JPopupMenu {
	
	public static final String ACTION_NAVIGATE_NOW = "actionnavnow";
	public static final String ACTION_ADD_WAYPOINT = "actionnaddwp";

	private static final long serialVersionUID = 1L;
	
	private JMenuItem itemNavigateNow, itemAddWaypoint;
	private int sourceX, sourceY;
	
	public MapPopupMenu(int sourceX, int sourceY) {
		itemNavigateNow = new JMenuItem("Diesen Punkt anfliegen");
		itemAddWaypoint = new JMenuItem("Diesen Punkt als Wegpunkt hinzufügen");
		itemAddWaypoint.setActionCommand(ACTION_ADD_WAYPOINT);
		itemNavigateNow.setActionCommand(ACTION_NAVIGATE_NOW);
		add(itemNavigateNow);
		add(itemAddWaypoint);
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}
	
	public void addOnActionListener(ActionListener listener) {
		itemAddWaypoint.addActionListener(listener);
		itemNavigateNow.addActionListener(listener);
	}

	public int getSourceX() {
		return sourceX;
	}

	public int getSourceY() {
		return sourceY;
	}
	
}
