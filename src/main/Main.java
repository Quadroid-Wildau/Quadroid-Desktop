package main;
import helper.FileHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import communication.CommunicationStack;

import controller.MainController;
import controller.VideoStreamController;
import controller.ViewController;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static MainController mainController;
	private JMenuBar menuBar;
	private JMenu menuFile, menuVideo, subMenuVideoDevice, saveScreenshotMenu, saveVideoMenu;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
//		System.getProperties().put("http.proxyPort", "8080");
		
		getMainController();
		new Main();
		
		initTestTimer();
	}

	public Main() {
		setSize(1024, 768);
		getContentPane().add(getMainController().getView());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("Datei");
		menuVideo = new JMenu("Video");
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		
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
		JMenuItem saveVideoPredefinedPath = new JMenuItem("Speichern in: " + homeDir);
		saveVideoPredefinedPath.setActionCommand("saveVideoPredefinedPath");
		saveVideoPredefinedPath.addActionListener(this);
		JMenuItem saveVideo = new JMenuItem("Pfad angeben");
		saveVideo.setActionCommand("saveVideo");
		saveVideo.addActionListener(this);
		JMenuItem stopSavingVideo = new JMenuItem("Aufnahme stoppen");
		stopSavingVideo.setActionCommand("stopSavingVideo");
		stopSavingVideo.addActionListener(this);
		
		//add them to the menu
		saveVideoMenu.add(saveVideoPredefinedPath);
		saveVideoMenu.add(saveVideo);
		saveVideoMenu.add(stopSavingVideo);
		
		//create save screenshot menu
		saveScreenshotMenu = new JMenu("Screenshot speichern");
		
		//create save screenshot menu items
		JMenuItem saveScreenshotPredefinedPath = new JMenuItem("Speichern in: " + homeDir);
		saveScreenshotPredefinedPath.setActionCommand("saveScreenshotPredefinedPath");
		saveScreenshotPredefinedPath.addActionListener(this);
		JMenuItem saveScreenshot = new JMenuItem("Pfad angeben");
		saveScreenshot.setActionCommand("saveScreenshot");
		saveScreenshot.addActionListener(this);
		
		//add them to the menu
		saveScreenshotMenu.add(saveScreenshotPredefinedPath);
		saveScreenshotMenu.add(saveScreenshot);
		
		//add to root menu
		menuVideo.add(saveVideoMenu);
		menuVideo.add(saveScreenshotMenu);
		
		//disable the save menus, because after starting the application, we don't have any capture device selected.
		//The user should not have any possibility to click one of these menu items.
		disableSaveMenus();
		
		JMenuItem menuExit = new JMenuItem("Beenden");
		menuExit.setActionCommand("exit");
		menuExit.addActionListener(this);
		menuFile.add(menuExit);
		
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
	
//***************************************************************************************************************
//	View Listeners
//***************************************************************************************************************

	@Override
	public void actionPerformed(ActionEvent e) {
		VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
		String command = e.getActionCommand();
		
		if (command.equals("saveVideoPredefinedPath")) {
			videoStreamController.saveVideoStream(FileHelper.getPredefinedVideoPath(""));
		} else if (command.equals("saveVideo")) {
			
		} else if (command.equals("stopSavingVideo")) {
			videoStreamController.stopSavingVideo();
		} else if (command.equals("saveScreenshotPredefinedPath")) {
			videoStreamController.saveScreenShot(FileHelper.getPredefinedScreenshotPath(""));
		} else if (command.equals("saveScreenshot")) {
			
		} else if (command.equals("exit")) {
			System.exit(0);
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
				System.err.println("Verdammt n Error man");
				ex.printStackTrace();
			}
		}
	}
}
