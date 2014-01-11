package main;


import helper.FileHelper;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ProxySelector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.AdvLandmark;

import org.apache.commons.lang3.SystemUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.LandMarkerService;

import com.mashape.unirest.http.Unirest;
import communication.CommunicationStack;

import connection.Connect;
import controller.MainController;
import controller.VideoStreamController;

/**
 * This is the main project class and entry point for the application.
 * 
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class Main extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	private static MainController mainController;
	private static Logger logger = LoggerFactory.getLogger(Main.class.getName());
	
	//Menu
	private JMenuBar menuBar;
	private JMenu menuFile, menuVideo, menuLandmarkAlarm, subMenuVideoDevice, saveScreenshotMenu, saveVideoMenu, menuXbee, xbee;
	private JMenuItem itemSaveVideoPredefinedPath, itemSaveVideo, itemStopSavingVideo, itemShowLandmarkAlerts;
	
	private Icon iconNewLandmarkAlert;
	
	private JComponent mapView;
	private JComponent map3DView;
	private JComponent metaDataView;
	private JComponent videoStreamView;
	private JPanel rightSubPanel;	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
		System.getProperties().put("http.proxyPort", "8080");
		
		PropertyConfigurator.configure("log4j.properties");
		logger = LoggerFactory.getLogger(Main.class.getName());
		logger.info("Init Logger");
		
		//init and show main window
		long start = System.currentTimeMillis();
		getMainController().getView().setVisible(true);
		long end = System.currentTimeMillis();
		System.out.println("Start Time: " + (end-start) + "ms");
		
		SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
		
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom().setRoutePlanner(routePlanner).build();
		
		Unirest.setHttpClient(httpclient);
		Unirest.setAsyncHttpClient(asyncClient);
		
		//Make sure the application gets a login token from Quadroid server by instantiating the communicator
		CommunicationStack.getInstance().getPushCommunicator();
	}

	public Main() {
		setTitle("Quadroid Desktop");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/logo.png")));
		setSize(1280, 800);
		addWindowListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
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
		
		iconNewLandmarkAlert = new ImageIcon(getClass().getResource("/images/alert.png"));
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("Datei");
		menuVideo = new JMenu("Video");
		menuLandmarkAlarm = new JMenu("Landmarkenalarm");
		menuXbee = new JMenu("XBee");
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		menuBar.add(menuLandmarkAlarm);
		menuBar.add(menuXbee);
		
		xbee = new JMenu("Connect xBee-Pro");
		for(String s : Connect.getAvailablePorts())
		{
			JMenuItem item = new JMenuItem(s);
			item.setActionCommand("open_xbee_connection");
			item.addActionListener(this);
			xbee.add(item);
		}
		menuXbee.add(xbee);
				
		JMenuItem itemExit = new JMenuItem("Beenden");
		itemExit.setActionCommand("exit");
		itemExit.addActionListener(this);
		menuFile.add(itemExit);
		
		subMenuVideoDevice = new JMenu("Video Device");
		menuVideo.add(subMenuVideoDevice);

		//Add Devices if Windows
		if (SystemUtils.IS_OS_WINDOWS) {
			final VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
			String[] captureDevices = videoStreamController.getAvailableCaptureDevices();
			if (captureDevices.length > 0) {
				for (int i = 0; i < captureDevices.length; i++) {
					JMenuItem item = new JMenuItem(captureDevices[i]);
					item.setActionCommand(String.valueOf(i));
					item.addActionListener(this);
					subMenuVideoDevice.add(item);
				}
			} else {
				JMenuItem item = new JMenuItem("Keine Ger\u00e4te gefunden");
				item.setEnabled(false);
				subMenuVideoDevice.add(item);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				JMenuItem item = new JMenuItem("Device " + i);
				item.setActionCommand(String.valueOf(i));
				item.addActionListener(this);
				subMenuVideoDevice.add(item);
			}
		}
		
		//create Video Menu
		saveVideoMenu = new JMenu("Video speichern");
		
		//Get users home directory for predefined paths
		String homeDir = System.getProperty("user.home");
		
		//Create save video menu items
		itemSaveVideoPredefinedPath = new JMenuItem("Speichern in: " + homeDir);
		itemSaveVideoPredefinedPath.setActionCommand("saveVideoPredefinedPath");
		itemSaveVideoPredefinedPath.addActionListener(this);
		itemSaveVideo = new JMenuItem("Pfad angeben");
		itemSaveVideo.setActionCommand("saveVideo");
		itemSaveVideo.addActionListener(this);
		itemStopSavingVideo = new JMenuItem("Aufnahme stoppen");
		itemStopSavingVideo.setActionCommand("stopSavingVideo");
		itemStopSavingVideo.addActionListener(this);
		itemStopSavingVideo.setEnabled(false);
		
		//add them to the menu
		saveVideoMenu.add(itemSaveVideoPredefinedPath);
		saveVideoMenu.add(itemSaveVideo);
		saveVideoMenu.add(itemStopSavingVideo);
		
		//create save screenshot menu
		saveScreenshotMenu = new JMenu("Screenshot speichern");
		
		//create save screenshot menu items
		JMenuItem itemSaveScreenshotPredefinedPath = new JMenuItem("Speichern in: " + homeDir);
		itemSaveScreenshotPredefinedPath.setActionCommand("saveScreenshotPredefinedPath");
		itemSaveScreenshotPredefinedPath.addActionListener(this);
		JMenuItem itemSaveScreenshot = new JMenuItem("Pfad angeben");
		itemSaveScreenshot.setActionCommand("saveScreenshot");
		itemSaveScreenshot.addActionListener(this);
		
		//add them to the menu
		saveScreenshotMenu.add(itemSaveScreenshotPredefinedPath);
		saveScreenshotMenu.add(itemSaveScreenshot);
		
		//add to root menu
		menuVideo.add(saveVideoMenu);
		menuVideo.add(saveScreenshotMenu);
		
		//disable the save menus, because after starting the application, we don't have any capture device selected.
		//The user should not have any possibility to click one of these menu items.
		disableSaveMenus();
		
		JMenuItem itemSimulateLandmarkAlarm = new JMenuItem("Landmarkenalarm simulieren");
		itemSimulateLandmarkAlarm.setActionCommand("simulateAlarm");
		itemSimulateLandmarkAlarm.addActionListener(this);
		menuLandmarkAlarm.add(itemSimulateLandmarkAlarm);
		
		itemShowLandmarkAlerts = new JMenuItem("Landmarkenalarme anzeigen");
		itemShowLandmarkAlerts.setActionCommand("showLandmarkAlerts");
		itemShowLandmarkAlerts.addActionListener(this);
		menuLandmarkAlarm.add(itemShowLandmarkAlerts);
		
		setJMenuBar(menuBar);
		
		add(getRightSubPanel(), java.awt.BorderLayout.EAST);
	}
	
	public static MainController getMainController() {
		if (mainController == null) {
			mainController = new MainController();
		}
		return mainController;
	}
	
	private JPanel getRightSubPanel() {
		if (this.rightSubPanel == null) {			
			this.rightSubPanel = new JPanel();
			this.rightSubPanel.setLayout(new BoxLayout(rightSubPanel, BoxLayout.Y_AXIS));
			this.rightSubPanel.setPreferredSize(new Dimension(600, 0));
			this.rightSubPanel.setMinimumSize(new Dimension(200, 0));
		}
		
		return this.rightSubPanel;
	}
	
	public void setVideoStream(JComponent view) {
		if (this.videoStreamView != view) {
			if (this.videoStreamView != null) {				
				this.remove(this.videoStreamView);
			}
			
			this.videoStreamView = view;
		}
		
		add(this.videoStreamView, java.awt.BorderLayout.CENTER);
		revalidate();
	}
	
	public void setMap(JComponent view) {
		if (this.mapView != view) {
			if (this.mapView != null) {				
				this.remove(this.mapView);
			}
			
			this.mapView = view;
		}
		
		getRightSubPanel().add(this.mapView, java.awt.BorderLayout.CENTER);
		revalidate();
	}
	
	public void setMap3D(JComponent view) {
		if (this.map3DView != view) {
			if(this.map3DView != null) { 
				this.remove(this.map3DView);
			}
			
			this.map3DView = view;
		}
		
		getRightSubPanel().add(this.map3DView, java.awt.BorderLayout.SOUTH);
		revalidate();
	}
	
	public void setMetaData(JComponent view) {
		if (this.metaDataView != view) {
			if (this.metaDataView != null) {				
				this.remove(this.metaDataView);
			}
			
			this.metaDataView = view;
		}
		
		add(this.metaDataView, java.awt.BorderLayout.SOUTH);
		revalidate();
	}
	
	public void newLandMarkAlarm(AdvLandmark landmark) {
		showAlertIconForLandmark(true);
		Toolkit.getDefaultToolkit().beep();
	}
	
//***************************************************************************************************************
//	Helpers
//***************************************************************************************************************
	
	private void disableSaveMenus() {
		saveScreenshotMenu.setEnabled(false);
		saveVideoMenu.setEnabled(false);
	}
	
	private void enableSaveMenus() {
		saveScreenshotMenu.setEnabled(true);
		saveVideoMenu.setEnabled(true);
	}
	
	private void enableAllCaptureDevices() {
		for (int i = 0; i < subMenuVideoDevice.getItemCount(); i++) {
			subMenuVideoDevice.getItem(i).setEnabled(true);
		}
	}
	
	private void configureVideoRecordingOptions(boolean isRecording) {
		itemSaveVideo.setEnabled(!isRecording);
		itemSaveVideoPredefinedPath.setEnabled(!isRecording);
		itemStopSavingVideo.setEnabled(isRecording);
	}
	
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
	
	private String openFile() {
		JFileChooser mFileChooser = new JFileChooser(System.getProperty("user.home"));
		int ret = mFileChooser.showOpenDialog(this);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			return mFileChooser.getSelectedFile().getPath();
		}
		
		return null;
	}
	
	private void showAlertIconForLandmark(boolean show) {
		if (show) {
			menuLandmarkAlarm.setIcon(iconNewLandmarkAlert);
			itemShowLandmarkAlerts.setIcon(iconNewLandmarkAlert);
		} else {
			menuLandmarkAlarm.setIcon(null);
			itemShowLandmarkAlerts.setIcon(null);
		}
	}
	
//***************************************************************************************************************
//	View Listeners
//***************************************************************************************************************

	@Override
	public void actionPerformed(ActionEvent e) {
		VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
		String command = e.getActionCommand();
		logger.info(command);
		//Check command
		if (command.equals("saveVideoPredefinedPath")) {
			videoStreamController.saveVideoStream(FileHelper.getPredefinedVideoPath(""));
			configureVideoRecordingOptions(true);
		} else if (command.equals("saveVideo")) {
			String videofile = getFileFromChooser("mpg");
			if (videofile != null) {
				if (!videofile.endsWith(".mpg")) videofile += ".mpg";
				videoStreamController.saveVideoStream(videofile);
			}
			configureVideoRecordingOptions(true);
		} else if (command.equals("stopSavingVideo")) {
			videoStreamController.stopSavingVideo();
			configureVideoRecordingOptions(false);
		} else if (command.equals("saveScreenshotPredefinedPath")) {
			videoStreamController.saveScreenShot(FileHelper.getPredefinedScreenshotPath(""));
		} else if (command.equals("saveScreenshot")) {
			String imagefile = getFileFromChooser("png");
			if (imagefile != null) {
				if (!imagefile.endsWith(".png")) imagefile += ".png";
				videoStreamController.saveScreenShot(imagefile);
			}
		} else if (command.equals("exit")) {
			System.exit(0);
		} else if (command.equals("open_xbee_connection")) {
			//if actioncommand is connect it must be an event of 
			//JMenuItem (only set in JMenuItem) 
			JMenuItem item = ((JMenuItem)e.getSource());
			//get selected port 
			String port = item.getText();
			
			//pass port to Communicator
			boolean success = CommunicationStack.getInstance().getFlightCommunicator().openXbeeConnection(port);
			
			if (success)
				item.setEnabled(false);
		} else if (command.equals("simulateAlarm")) {
			String imagefile = openFile();
			
			if (imagefile != null) {
				try {
					BufferedImage testImage = ImageIO.read(new File(imagefile));
					LandMarkerService.getInstance().simulateLandmarkAlarm(testImage);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (command.equals("showLandmarkAlerts")) {
			//Show landmark alarms view
			mainController.getLandmarkController().getView();
			showAlertIconForLandmark(false);
		} else {
			// If none of the actions matched until now, the action command must contain a video device port
			try {
				//disable all save menus in case the device can't be opened
				disableSaveMenus();
				
				//get device index from action command
				int device = Integer.parseInt(command);
				
				//open device
				videoStreamController.startVideoStream(device);
				
				//re-enable all capture devices in device chooser menu
				enableAllCaptureDevices();
				
				//disable the device in device chooser menu so it can't be reselected
				subMenuVideoDevice.getItem(device).setEnabled(false);
				
				//enable the menu items again, if we reach this code the device was opened successfully
				enableSaveMenus();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void windowClosing(WindowEvent e) {
		//When user presses X, stop grabbing frames and release camera
		VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
		videoStreamController.stopGrabbingVideoFrames();

		CommunicationStack.getInstance().getFlightCommunicator().closeXbeeConnection();
		
		//Stop Unirest threads
		try {
			Unirest.shutdown();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//Unused
	public void mouseEntered(MouseEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
