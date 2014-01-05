package view;

import helper.WaypointFactory;

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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

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

import service.FlightControlService;
import view.custom.MapPopupMenu;
import view.helper.RoutePainter;
import view.interfaces.WaypointDeletedInterface;
import de.th_wildau.quadroid.models.GNSS;

public class MapView extends JPanel implements WaypointDeletedInterface {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.MapController controller;
	private JLabel label;
	private JXMapViewer mapViewer;
	private JCheckBox cbAutoRefreshMap;
	private MapPopupMenu mpm;
	private JPanel panel;
	private JButton btnShowWaypoints;
	private JLabel labelSeperator;
	
	private List<GeoPosition> track = new ArrayList<GeoPosition>(); 
	private Set<Waypoint> currentRoutePos = new HashSet<Waypoint>();
	private Set<Waypoint> currentClickPos = new HashSet<Waypoint>();
	private Set<Waypoint> waypoints = new HashSet<Waypoint>();
	
	//Painters
	private RoutePainter routePainter = new RoutePainter();
	private WaypointPainter<Waypoint> currentRoutePosPainter = new WaypointPainter<Waypoint>();
	private WaypointPainter<Waypoint> currentClickPainter = new WaypointPainter<Waypoint>();
	private WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
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
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        cbAutoRefreshMap = new JCheckBox("Kartenposition automatisch setzen");
        panel.add(cbAutoRefreshMap);
        
        labelSeperator = new JLabel("|");
        panel.add(labelSeperator);
        
        btnShowWaypoints = new JButton("Wegpunkte anzeigen");
        btnShowWaypoints.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShowWaypoints.addActionListener(mButtonListener);
        panel.add(btnShowWaypoints);
        cbAutoRefreshMap.addChangeListener(mCheckBoxChangeListener);
        
        currentRoutePosPainter.setAntialiasing(true);
        painters.add(routePainter);
        painters.add(currentRoutePosPainter);
        painters.add(waypointPainter);
        painters.add(currentClickPainter);
        mapPainter.setPainters(painters);
	}
	
	public void setGeoData(GNSS geoData) {
		label.setText("Karte: " + geoData.getLatitude() + " | " + geoData.getLongitude());
		GeoPosition geoPos = new GeoPosition(geoData.getLatitude(), geoData.getLongitude());
		track.add(geoPos);
		
		currentRoutePos.clear();
		currentRoutePos.add(new DefaultWaypoint(geoPos));
		
		routePainter.setTrack(track);		
        currentRoutePosPainter.setWaypoints(currentRoutePos);
        
        mapViewer.setOverlayPainter(mapPainter);
        
        if (cbAutoRefreshMap.isSelected())
        	mapViewer.setAddressLocation(geoPos);
	}
	
	private void showPointAt(int x, int y) {
		GeoPosition pos = mapViewer.convertPointToGeoPosition(new Point(x,y));
		currentClickPos.add(new DefaultWaypoint(pos));
		currentClickPainter.setWaypoints(currentClickPos);
		mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void clearLastPoint() {
		currentClickPos.clear();
		currentClickPainter.setWaypoints(currentClickPos);
		currentClickPainter.clearCache();
		mapViewer.setOverlayPainter(mapPainter);
	}
	
	private void addWaypoint(GeoPosition pos) {
		waypoints.add(new DefaultWaypoint(pos));
	    waypointPainter.setWaypoints(waypoints);
	}
	
	private MouseListener mMouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			clearLastPoint();
			
			if (SwingUtilities.isRightMouseButton(e)) {
				cbAutoRefreshMap.setSelected(false);
				mpm = new MapPopupMenu(e.getX(), e.getY());
				mpm.addOnActionListener(mPopupMenuListener);
				mpm.show(e.getComponent(), e.getX(), e.getY());
				showPointAt(e.getX(), e.getY());
			}
		}
	};
	
	private ActionListener mPopupMenuListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int x = mpm.getSourceX();
			int y = mpm.getSourceY();
			
			GeoPosition pos = mapViewer.convertPointToGeoPosition(new Point(x,y));
			
			if (e.getActionCommand().equals(MapPopupMenu.ACTION_ADD_WAYPOINT)) {
				de.th_wildau.quadroid.models.Waypoint wp = WaypointFactory.createFromGeoPosition(pos);
			    FlightControlService.getInstance().addWaypoint(wp);
			    addWaypoint(pos);
			} else if (e.getActionCommand().equals(MapPopupMenu.ACTION_NAVIGATE_NOW)) {
				
			}
			
			
		}
	};
	
	private ActionListener mButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnShowWaypoints) {
				new WaypointView();
			}
		}
	};
	
	private ChangeListener mCheckBoxChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (cbAutoRefreshMap.isSelected()) {
				clearLastPoint();
			}
		}
	};

	@Override
	public void onWaypointDeleted(int index) {
		
	}
	
}
