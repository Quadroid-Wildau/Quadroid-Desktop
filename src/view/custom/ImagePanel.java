package view.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	BufferedImage img = null;

	public void displayImage(IplImage img) {
		this.img = img.getBufferedImage();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		super.paintComponent(g2);
		g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}
}
