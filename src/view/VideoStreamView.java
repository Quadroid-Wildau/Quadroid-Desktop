package view;

import javax.swing.SwingUtilities;

import view.custom.ImagePanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import controller.VideoStreamController;

public class VideoStreamView extends ImagePanel {
	private static final long serialVersionUID = 1L;
		
	private VideoStreamController controller;
	
	public VideoStreamView(controller.VideoStreamController controller) {
		this.controller = controller;
		this.setSize(800, 600);
		this.setMaximumSize(getSize());
		this.setMinimumSize(getSize());
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
