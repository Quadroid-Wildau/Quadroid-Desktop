package model;

import java.awt.Color;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;

public class CustomWaypoint extends DefaultWaypoint {
    private int pos;
    private Color textColor;

    public CustomWaypoint(int pos, Color textColor, GeoPosition geoPos) {
        super(geoPos);
        this.pos = pos;
        this.textColor = textColor;
    }

    /**
     * @return label text
     */
    public String getLabel() {
        return String.valueOf(pos);
    }
    
    /**
     * 
     * @return the waypoint position
     */
    public int getPos() {
    	return pos;
    }
    
    /**
     * increase position by 1
     */
    public void incPos() {
    	pos++;
    }
    
    /**
     * decrease position by 1
     */
    public void decPos() {
    	pos--;
    }

    /**
     * @return color
     */
    public Color getColor() {
        return textColor;
    }
}
