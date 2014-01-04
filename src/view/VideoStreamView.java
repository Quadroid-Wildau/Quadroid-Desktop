package view;

import javax.swing.SwingUtilities;

import view.custom.ImagePanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import controller.VideoStreamController;
import java.awt.Dimension;

public class VideoStreamView extends ImagePanel {
	private static final long serialVersionUID = 1L;
		
	private VideoStreamController controller;
	
	public VideoStreamView(controller.VideoStreamController controller) {
		this.controller = controller;
		this.setSize(640, 480);
		this.setMaximumSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(640, 480));
	}

	public void showNewVideoFrame(final IplImage frame) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (frame != null) {
					displayImage(frame);
				}
			}
		});
	}
}
