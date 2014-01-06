package view.helper;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class QuadroidWaypointRenderer implements WaypointRenderer<Waypoint> {

	private BufferedImage wpImage;
	 
	public QuadroidWaypointRenderer(String imagePath) {
		try {
			wpImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, Waypoint w) {
        g = (Graphics2D)g.create();
        
        if (wpImage == null)
                return;
        
        Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
        
        int x = (int)point.getX();
        int y = (int)point.getY();
        
        g.drawImage(wpImage, x - (wpImage.getWidth() / 2), y - (wpImage.getHeight() / 2), null);
        
        g.dispose();
    }
}
