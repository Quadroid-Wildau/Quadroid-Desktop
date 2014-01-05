package view.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

public class RoutePainter implements Painter<JXMapViewer> {

	private List<GeoPosition> track;

	/**
	 * @param track
	 *            the track
	 */
	public RoutePainter(List<GeoPosition> track) {
		this.track = new ArrayList<GeoPosition>(track);
	}

	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
		g = (Graphics2D) g.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);
		
		//draw outer line
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(4));
		drawRoute(g, map);

		//draw inner line
		g.setColor(Color.BLUE);
		g.setStroke(new BasicStroke(2));
		drawRoute(g, map);

		g.dispose();
	}
	
	private void drawRoute(Graphics2D g, JXMapViewer map) {
		Point2D last = null;

		//draw line for all points
		for (GeoPosition gp : track) {
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

			if (last != null) {
				g.drawLine((int) last.getX(), (int) last.getY(), (int) pt.getX(), (int) pt.getY());
			}

			last = pt;
		}
	}
}