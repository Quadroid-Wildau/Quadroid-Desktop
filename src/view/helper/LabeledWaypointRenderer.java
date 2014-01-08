package view.helper;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.CustomWaypoint;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 * This class can be used as a renderer for {@link Waypoint} on a {@link JXMapViewer}.
 * It renders a marker with a small label.
 * 
 * @author Georg Baumgarten
 *
 */
public class LabeledWaypointRenderer implements WaypointRenderer<CustomWaypoint> {

	private BufferedImage wpImage;
	 
	public LabeledWaypointRenderer(String imagePath) {
		try {
			wpImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, CustomWaypoint w) {
        g = (Graphics2D)g.create();
        
        if (wpImage == null)
                return;
        
        Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
        
        int x = (int)point.getX();
        int y = (int)point.getY();
        
        g.drawImage(wpImage, x -wpImage.getWidth() / 2, y -wpImage.getHeight(), null);
        
        String label = w.getLabel();
        FontMetrics metrics = g.getFontMetrics();
        int tw = metrics.stringWidth(label);
        int th = 1 + metrics.getAscent();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(label, x - tw / 2, y + th - wpImage.getHeight());
        
        g.dispose();
    }
}
