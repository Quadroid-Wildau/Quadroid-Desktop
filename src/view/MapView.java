package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

import model.CustomWaypoint;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

import controller.MapController;

import view.custom.MapPopupMenu;
import view.helper.LabeledWaypointRenderer;
import view.helper.QuadroidWaypointRenderer;
import view.helper.RoutePainter;
import view.interfaces.WaypointDeletedInterface;
import de.th_wildau.quadroid.models.GNSS;
import java.awt.FlowLayout;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class MapView extends JPanel implements WaypointDeletedInterface {
	private static final long serialVersionUID = 1L;
	
	private MapController controller;
	private JLabel label;
	private JXMapViewer mapViewer;
	private JCheckBox cbAutoRefreshMap;
	private MapPopupMenu mpm;
	private JPanel panel;
	private JButton btnShowWaypoints;
	
	private List<GeoPosition> mQuadroidTrack = new ArrayList<GeoPosition>(); 
	private Set<Waypoint> mCurrentQuadroidPos = new HashSet<Waypoint>();
	
	//To show a marker when user right clicks on the map
	private Set<Waypoint> mLastRightClickPos = new HashSet<Waypoint>();
	
	private List<GeoPosition> mWaypointsList = new ArrayList<GeoPosition>();
	private Set<CustomWaypoint> mWaypointsSet = new HashSet<CustomWaypoint>();
	
	//Painters
	private RoutePainter routePainter = new RoutePainter();
	private RoutePainter wayPointLinePainter = new RoutePainter(Color.RED);
	private WaypointPainter<Waypoint> currentRoutePosPainter = new WaypointPainter<Waypoint>();
	private WaypointPainter<Waypoint> currentClickPainter = new WaypointPainter<Waypoint>();
	private WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<CustomWaypoint>();
	private List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
	private CompoundPainter<JXMapViewer> mapPainter = new CompoundPainter<JXMapViewer>();

	public MapView(controller.MapController controller) {
		setPreferredSize(new Dimension(600, 500));
		setMinimumSize(new Dimension(200, 200));
		setMaximumSize(new Dimension(600, 1000));
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setDoubleBuffered(true);
		
		this.controller = controller;
		
		label = new JLabel("Karte: ");
		label.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		label.setFont(new Font("Arial", Font.BOLD, 18));
		add(label, BorderLayout.NORTH);
		
		mapViewer = new JXMapViewer();
		add(mapViewer, BorderLayout.CENTER);
		
		TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);
        
        GeoPosition start = new GeoPosition(50.11, 8.68);
        
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(start);
        mapViewer.setDoubleBuffered(true);
        
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(mMouseListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        
        panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        cbAutoRefreshMap = new JCheckBox("Kartenposition automatisch setzen");
        panel.add(cbAutoRefreshMap);
        
        btnShowWaypoints = new JButton("Wegpunkte anzeigen");
        btnShowWaypoints.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShowWaypoints.addActionListener(mButtonListener);
        
        lblNewLabel = new JLabel("| ");
        lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        panel.add(lblNewLabel);
        panel.add(btnShowWaypoints);
        cbAutoRefreshMap.addChangeListener(mCheckBoxChangeListener);
        
        //Configure painters
        currentRoutePosPainter.setAntialiasing(true);
        waypointPainter.setRenderer(new LabeledWaypointRenderer("/images/waypoint_red.png"));
        currentRoutePosPainter.setRenderer(new QuadroidWaypointRenderer("/images/quadroid.png"));
        painters.add(routePainter);
        painters.add(currentRoutePosPainter);
        painters.add(waypointPainter);
        painters.add(currentClickPainter);
        painters.add(wayPointLinePainter);
        mapPainter.setPainters(painters);
	}
	
	public void setGeoData(GNSS geoData) {
		label.setText("Karte: " + geoData.getLatitude() + " | " + geoData.getLongitude());
		GeoPosition geoPos = new GeoPosition(geoData.getLatitude(), geoData.getLongitude());
		mQuadroidTrack.add(geoPos);
		
		mCurrentQuadroidPos.clear();
		mCurrentQuadroidPos.add(new DefaultWaypoint(geoPos));
		
		routePainter.setTrack(mQuadroidTrack);		
        currentRoutePosPainter.setWaypoints(mCurrentQuadroidPos);
        
        mapViewer.setOverlayPainter(mapPainter);
        
        if (cbAutoRefreshMap.isSelected())
        	mapViewer.setAddressLocation(geoPos);
	}
	
	public void removeWaypointFromList(int index) {
		removeWaypoint(index);
	}
	
//**********************************************************************************************************************************
//	View Listeners
//**********************************************************************************************************************************
	
	private MouseListener mMouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			clearLastMouseMarker();
			
			//show popup menu if right mouse button was clicked
			if (SwingUtilities.isRightMouseButton(e)) {
				cbAutoRefreshMap.setSelected(false);
				mpm = new MapPopupMenu(e.getX(), e.getY());
				mpm.addOnActionListener(mPopupMenuListener);
				mpm.show(e.getComponent(), e.getX(), e.getY());
				showMarkerAt(e.getX(), e.getY());
			}
		}
	};
	
	private ActionListener mPopupMenuListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//remove marker at initial click position
			clearLastMouseMarker();
			
			int x = mpm.getSourceX();
			int y = mpm.getSourceY();
			
			//Convert pixels to geo position
			GeoPosition pos = mapViewer.convertPointToGeoPosition(new Point(x,y));
			
			float height = -1f;
			try {
				height = Float.parseFloat(JOptionPane.showInputDialog(MapView.this, "Hoehe in m: "));
			} catch (Exception ex) {}
			
			if (height == -1f)
				return;
			
			if (e.getActionCommand().equals(MapPopupMenu.ACTION_ADD_WAYPOINT)) {
				//add new waypoint at the end of the list
				controller.addWaypoint(pos, height);
			    addWaypoint(pos);
			} else if (e.getActionCommand().equals(MapPopupMenu.ACTION_NAVIGATE_NOW)) {
				//If there are waypoints in list, show dialog
				if (mWaypointsSet.size() > 0) {
					int selection = JOptionPane.showConfirmDialog(
						    MapView.this,
						    "Gesetzte Waypoints löschen?",
						    "Waypoints",
						    JOptionPane.YES_NO_CANCEL_OPTION);
					
					switch (selection) {
					case JOptionPane.CANCEL_OPTION:
						break;
					case JOptionPane.YES_OPTION:
						//delete all waypoints
						controller.navigateTo(pos, height, true);
						clearWaypoints();
						addWaypoint(pos);
						break;
					case JOptionPane.NO_OPTION:
						//keep waypoints and just add a new one at top
						addWaypointAtTop(pos);
						controller.navigateTo(pos, height, false);
						break;
					}
				} else {
					controller.navigateTo(pos, height, false);
				}
			}
		}
	};
	
	private ActionListener mButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnShowWaypoints) {
				//show waypoints
				new WaypointView(MapView.this, controller);
			}
		}
	};
	
	private ChangeListener mCheckBoxChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (cbAutoRefreshMap.isSelected()) {
				//When checking the checkbox, remove the mouse click marker
				clearLastMouseMarker();
			}
		}
	};
	private JLabel lblNewLabel;
	
	
//**********************************************************************************************************************************
//	Callbacks
//**********************************************************************************************************************************

	@Override
	public void onWaypointDeleted(int index) {
		removeWaypoint(index);
		controller.removeWaypoint(index);
	}
	
//**********************************************************************************************************************************
//	Helpers
//**********************************************************************************************************************************
	
	private void showMarkerAt(int x, int y) {
		GeoPosition pos = mapViewer.convertPointToGeoPosition(new Point(x,y));
		mLastRightClickPos.add(new DefaultWaypoint(pos));
		currentClickPainter.setWaypoints(mLastRightClickPos);
		mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void clearLastMouseMarker() {
		mLastRightClickPos.clear();
		currentClickPainter.setWaypoints(mLastRightClickPos);
		currentClickPainter.clearCache();
		mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void addWaypoint(GeoPosition pos) {
		mWaypointsSet.add(new CustomWaypoint(mWaypointsSet.size()+1, Color.BLACK, pos));
	    waypointPainter.setWaypoints(mWaypointsSet);
	    mWaypointsList.add(pos);
	    wayPointLinePainter.setTrack(mWaypointsList);
	    mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void addWaypointAtTop(GeoPosition pos) {
		CustomWaypoint wp = new CustomWaypoint(1, Color.black, pos);
		CustomWaypoint[] waypointsArr = new CustomWaypoint[mWaypointsSet.size()];
		mWaypointsSet.toArray(waypointsArr);
		
		mWaypointsSet.clear();
		mWaypointsSet.add(wp);
		for (CustomWaypoint cwp : waypointsArr) {
			cwp.incPos();
			mWaypointsSet.add(cwp);
		}
		waypointPainter.setWaypoints(mWaypointsSet);
		
		List<GeoPosition> geoCache = new ArrayList<GeoPosition>(mWaypointsList);
		mWaypointsList.clear();
		mWaypointsList.add(pos);
		mWaypointsList.addAll(geoCache);
		wayPointLinePainter.setTrack(mWaypointsList);
	}
	
	private void clearWaypoints() {
		mWaypointsSet.clear();
		waypointPainter.setWaypoints(mWaypointsSet);
		mWaypointsList.clear();
		wayPointLinePainter.setTrack(mWaypointsList);
		mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void removeWaypoint(int index) {
		//remove waypoint from list
		CustomWaypoint[] waypointsArr = new CustomWaypoint[mWaypointsSet.size()];
		mWaypointsSet.toArray(waypointsArr);
		mWaypointsSet.clear();
		for (CustomWaypoint cwp : waypointsArr) {
			if (cwp.getPos() < (index+1)) {
				mWaypointsSet.add(cwp);
			} else if (cwp.getPos() > (index+1)) {
				cwp.decPos();
				mWaypointsSet.add(cwp);
			}
		}
		
		waypointPainter.setWaypoints(mWaypointsSet);
		mWaypointsList.remove(index);
		mapViewer.setOverlayPainter(mapPainter);
	}
}
