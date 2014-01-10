/**
 * 
 */
package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.custom.ImagePanel;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import java.awt.Component;
import javax.swing.AbstractListModel;

/**
 * This view display landmark alerts.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class LandmarkView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private final JLabel lblAlarme = new JLabel("Alarme");

	public LandmarkView() {
		setTitle("Landmarkenalarm");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setPreferredSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		getContentPane().add(panel, BorderLayout.WEST);
		
		lblAlarme.setVerticalAlignment(SwingConstants.TOP);
		panel.add(lblAlarme);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Test"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		list.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(list);
		
		imagePanel = new ImagePanel();
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		
	}
	
}
