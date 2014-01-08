package view.custom;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jdesktop.swingx.JXMapViewer;

/**
 * This {@link JPopupMenu} holds 2 options and is intended to be used on a {@link JXMapViewer}.
 * It shows a popup menu at x-y coordinates on the source view and has 2 options:
 * Navigate to Waypoint now and Add Waypoint to queue.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class MapPopupMenu extends JPopupMenu {
	
	/**
	 * Action command of the "navigate to waypoint now" menu item
	 */
	public static final String ACTION_NAVIGATE_NOW = "actionnavnow";
	
	/**
	 * Action command of the "add waypoint to queue" menu item
	 */
	public static final String ACTION_ADD_WAYPOINT = "actionnaddwp";

	private static final long serialVersionUID = 1L;
	
	private JMenuItem itemNavigateNow, itemAddWaypoint;
	private int sourceX, sourceY;
	
	/**
	 * Constructor
	 * @param sourceX
	 * 			The x coordinate of the mouse click event
	 * @param sourceY
	 * 			The y coordinate of the mouse click event
	 */
	public MapPopupMenu(int sourceX, int sourceY) {
		itemNavigateNow = new JMenuItem("Diesen Punkt anfliegen");
		itemAddWaypoint = new JMenuItem("Diesen Punkt als Wegpunkt hinzuf\u00FCgen");
		itemAddWaypoint.setActionCommand(ACTION_ADD_WAYPOINT);
		itemNavigateNow.setActionCommand(ACTION_NAVIGATE_NOW);
		add(itemNavigateNow);
		add(itemAddWaypoint);
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}
	
	/**
	 * Adds action listener to both {@link JMenuItem}s
	 * @param listener
	 */
	public void addOnActionListener(ActionListener listener) {
		itemAddWaypoint.addActionListener(listener);
		itemNavigateNow.addActionListener(listener);
	}

	/**
	 * gets the original click position (x axis) of the parent view
	 * @return
	 */
	public int getSourceX() {
		return sourceX;
	}

	/**
	 * gets the original click position (y axis) of the parent view
	 * @return
	 */
	public int getSourceY() {
		return sourceY;
	}
	
}
