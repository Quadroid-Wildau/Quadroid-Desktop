/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controller.LandmarkController;

import view.custom.ImagePanel;

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
	private JLabel lblMetadata;
	private JList<String> list;
	private LandmarkController controller;

	public LandmarkView(LandmarkController controller) {
		setTitle("Landmarkenalarm");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setPreferredSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		
		this.controller = controller;
		
		Font borderFont = new Font("Arial", Font.PLAIN, 14);
		
		TitledBorder containerBorder = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Landmarke", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, borderFont, null);
		JPanel imageContainer = new JPanel();
		imageContainer.setBorder(containerBorder);
		getContentPane().add(imageContainer, BorderLayout.CENTER);
		imageContainer.setLayout(new BorderLayout(0, 0));
		
		lblMetadata = new JLabel("Lat: 52.18118, Long: 13.1151, Height: 25.0");
		lblMetadata.setBorder(new EmptyBorder(5, 5, 5, 5));
		imageContainer.add(lblMetadata, BorderLayout.NORTH);
		
		imagePanel = new ImagePanel();
		imageContainer.add(imagePanel);
		
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(192, 0));
		panel.setMinimumSize(new Dimension(192, 10));
		getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		list = new JList<String>();
		list.setMinimumSize(new Dimension(192, 0));
		list.setSize(new Dimension(192, 0));
		TitledBorder listBorder = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Vorhandene Alarme", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, borderFont, null);
		list.setBorder(listBorder);
		list.setFixedCellWidth(192);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(list);
		
		DefaultListModel<String> placeHolderModel = new DefaultListModel<>();
		placeHolderModel.addElement("Keine");
		list.setModel(placeHolderModel);
		
		setVisible(true);
	}
	
}
