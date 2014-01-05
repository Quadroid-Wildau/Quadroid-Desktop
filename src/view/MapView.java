package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

import view.custom.RoutePainter;
import de.th_wildau.quadroid.models.GNSS;

public class MapView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.MapController controller;
	private JLabel label;
	private JXMapViewer mapViewer;
	private JCheckBox cbAutoRefreshMap;
	
	private List<GeoPosition> track = new ArrayList<GeoPosition>(); 
	private Set<Waypoint> waypoints = new HashSet<Waypoint>();

	public MapView(controller.MapController controller) {
		setPreferredSize(new Dimension(500, 500));
		setMinimumSize(new Dimension(200, 200));
		setMaximumSize(new Dimension(500, 1000));
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
        
        cbAutoRefreshMap = new JCheckBox("Kartenposition automatisch setzen");
        add(cbAutoRefreshMap, BorderLayout.SOUTH);
	}
	
	public void setGeoData(GNSS geoData) {
		label.setText("Karte: " + geoData.getLatitude() + " | " + geoData.getLongitude());
		GeoPosition geoPos = new GeoPosition(geoData.getLatitude(), geoData.getLongitude());
		track.add(geoPos);
		
		waypoints.clear();
		waypoints.add(new DefaultWaypoint(geoPos));
		
		RoutePainter routePainter = new RoutePainter(track);
				
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setAntialiasing(true);
        waypointPainter.setWaypoints(waypoints);
        
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);
		
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
        
        if (cbAutoRefreshMap.isSelected())
        	mapViewer.setAddressLocation(geoPos);
	}
}
