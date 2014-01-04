package main;


import handler.XBeeReceiverHandler;
import handler.XBeeTransmitterHandler;
import helper.FileHelper;
import interfaces.IRxListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import model.XBeeRxTx;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coder.decoder.ObserverHandler;
import coder.encoder.TxDataEncoder;
import com.mashape.unirest.http.Unirest;

import model.GeoData;
import model.Landmark;
import model.MetaData;

import communication.CommunicationStack;

import communication.CommunicationStack;
import connection.Connect;
import controller.MainController;
import controller.VideoStreamController;
import controller.ViewController;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.RxData;
import enums.XBee;

public class Main extends JFrame implements ActionListener, WindowListener, MouseListener, IRxListener {

	private static final long serialVersionUID = 1L;
	private static final String LOGGERPROPERTIES = "log4j.properties";
	private static MainController mainController;
	private static Logger logger = LoggerFactory.getLogger(Main.class.getName());
	private JMenuBar menuBar;
	private JMenu menuFile, menuVideo, subMenuVideoDevice, saveScreenshotMenu, saveVideoMenu, xbee;
	private JMenuItem saveVideoPredefinedPath, saveVideo, stopSavingVideo, disconnectxbee;
	private Connect xbeeconnection = null;
	/**instance for transmission with xbee*/
	private XBeeTransmitterHandler xbeetransmitter = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
//		System.getProperties().put("http.proxyPort", "8080");
		PropertyConfigurator.configure(LOGGERPROPERTIES);
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
		
		xbee = new JMenu("Connect xBee-Pro");
		xbee.addMouseListener(this);
		for(String s : Connect.getAvailablePorts())
		{
			xbee.add(s);
		}
		
		menuFile.add(xbee);
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		menuBar.add(menuLandmarkAlarm);
		
		subMenuVideoDevice = new JMenu("Video Device");
		menuVideo.add(subMenuVideoDevice);
/*		
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
			JMenuItem item = new JMenuItem("Keine Ger�te gefunden");
			item.setEnabled(false);
			subMenuVideoDevice.add(item);
		}
		
*/		//create Video Menu
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
		} else if(command.equals("connect"))
		{
			//if actioncommand is connect it must be an event of 
			//JMenuItem (only set in JMenuItem) 
			JMenuItem item = ((JMenuItem)e.getSource());
			//get selected port 
			String port = item.getText();
			//create xbee device
			XBeeRxTx xbeedevice = new XBeeRxTx();
			xbeedevice.setBaud(XBee.BAUD.getValue());
			xbeedevice.setDatabits(XBee.DATABITS.getValue());
			xbeedevice.setParity(XBee.PARITY.getValue());
			xbeedevice.setPort(port);//set selected port
			xbeedevice.setStopbits(XBee.STOPBITS.getValue());
			xbeedevice.setDevicename(XBee.DEVICENAME.getName());
			//create connection to xbee device
			this.xbeeconnection = Connect.getInstance(xbeedevice);
			//registered observer, transmitter and receiver
			if(this.xbeeconnection != null)
			{
				XBeeReceiverHandler receiver = new XBeeReceiverHandler();
				xbeetransmitter = new XBeeTransmitterHandler(this.xbeeconnection);
				ObserverHandler oh = ObserverHandler.getReference();
				oh.register(this);
				this.xbeeconnection.addSerialPortEventListener(receiver);
				
				menuFile.remove(xbee);
				xbee.removeMouseListener(this);
				xbee.removeAll();
				xbee.setText("xBee-Pro");
				disconnectxbee = new JMenuItem("Disconnect xBee-Pro");
				disconnectxbee.setActionCommand("disconnectxbee");
				disconnectxbee.addActionListener(this);
				xbee.add(disconnectxbee);
				menuFile.add(xbee);
			}	
			
			return;
		}else if(command.equals("disconnectxbee"))
		{
			if(this.xbeeconnection != null)
			{
				this.xbeeconnection.disconnect();
				this.xbeeconnection = null;
				
				menuFile.remove(xbee);
				xbee.removeAll();
				xbee.addMouseListener(this);
				xbee.setText("Connect xBee-Pro");
				menuFile.add(xbee);
			}
			
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

		//disconnect xBee Device
		if(this.xbeeconnection != null)
		   this.xbeeconnection.disconnect();
		
		//Stop Unirest threads
		try {
			Unirest.shutdown();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		xbee.removeAll();
		for(String s : Connect.getAvailablePorts())
		{
			JMenuItem jmi = new JMenuItem(s);
			jmi.addActionListener(this);
			jmi.setActionCommand("connect");
			xbee.add(jmi);
		}
		
		menuFile.add(xbee);
	}
	
	
	
	//Unused
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void rx(RxData data) 
	{
		logger.info("new data available");
		//data.getWaypointlist()
		//data.getAirplanelist()
		//data.getCourselist()
		//data.getGnsslist()
		//data.getLandmarklist()
		//data.getMetadatalist()
		
		GNSS geo = new GNSS();
		
		Course course = new Course();
		
		geo.setLatitude(52.1245f);
		geo.setLongitude(13.12345f);
		geo.setHeight(300.00f);
		
		course.setAngleReference(180.0f);
		course.setSpeed(52.00f);
		
		TxDataEncoder encoder = new TxDataEncoder();
		
		byte[] buffer = encoder.geodataToBytes(geo);
		byte[] txdata = encoder.appendBytes(buffer, encoder.courseToBytes(course));
		
		this.xbeetransmitter.transmit(txdata);
		/**
		 * TODO: remove this example 
		 * 
		 * NOTE: Example for rx/tx data
		 * 
		 * create connection
		 * 1.) this.xbeeconnection = Connect.getInstance(IDevice);
		 * 	
		 * for transmission
		 * 2.) xbeetransmitter = new XBeeTransmitterHandler(this.xbeeconnection);
		 * 
		 * this.xbeetransmitter.transmit(msg);
		 * 
		 * registered observer
		 * 3.1.) ObserverHandler oh = ObserverHandler.getReference();
		 * 	   oh.register(this); 
		 * 
		 *  All Observer must be implemented IRxListener
		 * 
		 * registered receiver
		 * 3.2.)
		 * 		XBeeReceiverHandler receiver = new XBeeReceiverHandler();
		 *		this.xbeeconnection.addSerialPortEventListener(receiver);
		 * 
		 * */
		
	}
}
