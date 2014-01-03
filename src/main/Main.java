package main;
import helper.FileHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mashape.unirest.http.Unirest;

import model.GeoData;
import model.Landmark;
import model.MetaData;

import communication.CommunicationStack;

import controller.MainController;
import controller.VideoStreamController;
import controller.ViewController;

public class Main extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	
	private static MainController mainController;
	private JMenuBar menuBar;
	private JMenu menuFile, menuVideo, menuLandmarkAlarm, subMenuVideoDevice, saveScreenshotMenu, saveVideoMenu;
	private JMenuItem itemSaveVideoPredefinedPath, itemSaveVideo, itemStopSavingVideo;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
//		System.getProperties().put("http.proxyPort", "8080");
		
		getMainController();
		new Main();
		
		initTestTimer();
		
		CommunicationStack.getInstance().getPushCommunicator();
	}

	public Main() {
		setSize(1280, 800);
		getContentPane().add(getMainController().getView());
		addWindowListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("Datei");
		menuVideo = new JMenu("Video");
		menuLandmarkAlarm = new JMenu("Landmarkenalarm");
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		menuBar.add(menuLandmarkAlarm);
		
		subMenuVideoDevice = new JMenu("Video Device");
		menuVideo.add(subMenuVideoDevice);
		
		//Add Devices
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
			JMenuItem item = new JMenuItem("Keine Geräte gefunden");
			item.setEnabled(false);
			subMenuVideoDevice.add(item);
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
		
		JMenuItem itemExit = new JMenuItem("Beenden");
		itemExit.setActionCommand("exit");
		itemExit.addActionListener(this);
		menuFile.add(itemExit);
		
		JMenuItem itemSimulateLandmarkAlarm = new JMenuItem("Landmarkenalarm simulieren");
		itemSimulateLandmarkAlarm.setActionCommand("simulateAlarm");
		itemSimulateLandmarkAlarm.addActionListener(this);
		menuLandmarkAlarm.add(itemSimulateLandmarkAlarm);
		
		setJMenuBar(menuBar);
		
		setVisible(true);
	}
	
	public static ViewController getMainController() {
		if (mainController == null) {
			mainController = new MainController();
		}
		
		return mainController;
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
	
	private static void initTestTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				CommunicationStack.getInstance().getFlightCommunicator().setChangedPublic();
				CommunicationStack.getInstance().getFlightCommunicator().notifyObservers();
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
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
	
//***************************************************************************************************************
//	View Listeners
//***************************************************************************************************************

	@Override
	public void actionPerformed(ActionEvent e) {
		VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
		String command = e.getActionCommand();
		
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
		} else if (command.equals("simulateAlarm")) {
			String imagefile = openFile();
			
			try {
				BufferedImage testImage = ImageIO.read(new File(imagefile));
				GeoData geoData = new GeoData(54.584f, 14.65f, 20.0f);
				Landmark landmark = new Landmark();
				MetaData metadata = new MetaData();
				metadata.setGeodata(geoData);
				metadata.setTime(System.currentTimeMillis());
				landmark.setLandmarkPicture(testImage);
				landmark.setMetaData(metadata);
				CommunicationStack.getInstance().getPushCommunicator().pushLandmarkAlarm(landmark);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
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
		
		//Stop Unirest threads
		try {
			Unirest.shutdown();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//Unused
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
