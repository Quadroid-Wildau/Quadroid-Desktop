package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import view.custom.ImagePanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import controller.VideoStreamController;
import javax.swing.SwingConstants;

public class VideoStreamView extends JPanel {
	private static final long serialVersionUID = 1L;
		
	@SuppressWarnings("unused")
	private VideoStreamController controller;
	
	private ImagePanel imagePanel;
	private JLabel lblChooseDevice;
	
	public VideoStreamView(controller.VideoStreamController controller) {
		this.controller = controller;
		this.setPreferredSize(new Dimension(640, 480));
		this.setMaximumSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(640, 480));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblMetadaten = new JLabel("Video");
		lblMetadaten.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		lblMetadaten.setFont(new Font("Arial", Font.BOLD, 18));
		add(lblMetadaten, BorderLayout.NORTH);
				
		imagePanel = new ImagePanel();
		imagePanel.setLayout(new BorderLayout());
		add(imagePanel, BorderLayout.CENTER);
		
		lblChooseDevice = new JLabel("W\u00E4hlen Sie ein Video Device aus dem Men\u00FC");
		lblChooseDevice.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseDevice.setFont(new Font("Arial", Font.PLAIN, 18));
		lblChooseDevice.setForeground(Color.gray);
		imagePanel.add(lblChooseDevice, BorderLayout.CENTER);
	}

	public void showNewVideoFrame(final IplImage frame) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (frame != null) {
					if (lblChooseDevice != null)
						imagePanel.remove(lblChooseDevice);
					imagePanel.displayImage(frame);
				}
			}
		});
	}
}
