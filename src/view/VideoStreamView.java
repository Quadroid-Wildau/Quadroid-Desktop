package view;

import static com.googlecode.javacv.cpp.opencv_core.cvFlip;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import view.custom.ImagePanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import controller.VideoStreamController;

public class VideoStreamView extends ImagePanel {
	private static final long serialVersionUID = 1L;
		
	private VideoStreamController controller;
	
	public VideoStreamView(controller.VideoStreamController controller) {
		this.controller = controller;
	}

	public void showNewVideoFrame(final IplImage frame) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (frame != null) {
					cvFlip(frame, frame, 1);
					displayImage(frame);
				}
			}
		});
	}	
}
