/**
 * 
 */
package view;

import helper.DateFormatter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.AdvLandmark;
import view.custom.ImagePanel;
import controller.LandmarkController;
import de.th_wildau.quadroid.models.GNSS;

/**
 * This view display landmark alerts.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class LandmarkAlarmView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private JLabel lblMetadata;
	private JList<String> list;
	private LandmarkController controller;
	private JButton btnSave;
	
	private DefaultListModel<String> placeHolderModel;
	private List<AdvLandmark> currentLandmarks = new ArrayList<AdvLandmark>();
	
	public LandmarkAlarmView(LandmarkController controller) {
		setTitle("Landmarkenalarm");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setPreferredSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		this.controller = controller;
		
		Font borderFont = new Font("Arial", Font.PLAIN, 14);
		
		TitledBorder containerBorder = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Landmarke", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, borderFont, null);
		JPanel imageContainer = new JPanel();
		imageContainer.setBorder(containerBorder);
		getContentPane().add(imageContainer, BorderLayout.CENTER);
		imageContainer.setLayout(new BorderLayout(0, 0));
		
		lblMetadata = new JLabel("");
		lblMetadata.setBorder(new EmptyBorder(5, 5, 5, 5));
		imageContainer.add(lblMetadata, BorderLayout.NORTH);
		
		imagePanel = new ImagePanel();
		imageContainer.add(imagePanel);
		
		btnSave = new JButton("Speichern");
		btnSave.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
		btnSave.addActionListener(mActionListener);
		imageContainer.add(btnSave, BorderLayout.SOUTH);
		
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
		list.addListSelectionListener(mListSelectionListener);
		panel.add(list);
		
		placeHolderModel = new DefaultListModel<>();
		placeHolderModel.addElement("Keine");
		list.setModel(placeHolderModel);
		
		addWindowListener(mWindowListener);
		
		setLandmarkAlarms(controller.getLandmarkAlarms());
		
		setVisible(true);
	}
	
//*************************************************************************************************************************
//	View Listeners
//*************************************************************************************************************************	
	
	private WindowListener mWindowListener = new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent e) {
			controller.notifyViewClosed();
		}
	};
	
	private ListSelectionListener mListSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				if (list.getSelectedIndex() < currentLandmarks.size()) {
					AdvLandmark landmark = currentLandmarks.get(list.getSelectedIndex());
					GNSS gnss = landmark.getMetaData().getAirplane().GeoData();
					
					lblMetadata.setText("Latitude: " + gnss.getLatitude() + 
										", Longitude: " + gnss.getLongitude() + 
										", Hoehe: " + gnss.getHeight() +
										", Zeit: " + DateFormatter.formatDate(landmark.getMetaData().getAirplane().getTime() * 1000));
										
					imagePanel.displayImage(landmark.getPictureoflandmark());
					imagePanel.revalidate();
				}
			}
		}
	};
	
	private ActionListener mActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentLandmarks.size() > 0) {
				String file = getFileFromChooser("png");
				
				if (file != null) {
					if (!file.endsWith(".png")) file += ".png";
					AdvLandmark landmark = currentLandmarks.get(list.getSelectedIndex());
					controller.saveToImageFile(landmark, file);
				}
			}
		}
	};
	

//*************************************************************************************************************************
//	Public methods (called by controller)
//*************************************************************************************************************************	
	
	public void setLandmarkAlarms(List<AdvLandmark> landmarks) {
		if (landmarks == null || landmarks.isEmpty()) {
			list.setModel(placeHolderModel);
			return;
		}
		
		this.currentLandmarks = landmarks;
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < landmarks.size(); i++) {
			AdvLandmark lm = landmarks.get(i);
			String description = "[" + (i+1) + "] " + DateFormatter.formatDate(lm.getMetaData().getAirplane().getTime() * 1000);
			model.addElement(description);
		}
		
		list.setModel(model);
	}
	
	public void addLandmarkAlarm(AdvLandmark landmark) {
		currentLandmarks.add(landmark);
		setLandmarkAlarms(currentLandmarks);
	}
	
//*************************************************************************************************************************
//	Helpers
//*************************************************************************************************************************	
	
	private String getFileFromChooser(final String ending) {
		JFileChooser mFileChooser = new JFileChooser(System.getProperty("user.home"));
		mFileChooser.setAcceptAllFileFilterUsed(false);
		mFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(ending, ending));
		
		int ret = mFileChooser.showSaveDialog(this);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			return mFileChooser.getSelectedFile().getPath();
		}
		
		return null;
	}
}
