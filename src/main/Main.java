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
import java.util.Timer;
import java.util.TimerTask;
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
	}

	public Main() {
		setSize(1024, 768);
		getContentPane().add(getMainController().getView());
		addWindowListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("Datei");
		menuVideo = new JMenu("Video");
		
		xbee = new JMenu("Connect xBee-Pro");
		xbee.addMouseListener(this);
		for(String s : Connect.getAvailablePorts())
		{
			xbee.add(s);
		}
		
		menuFile.add(xbee);
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		
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
		saveVideoPredefinedPath = new JMenuItem("Speichern in: " + homeDir);
		saveVideoPredefinedPath.setActionCommand("saveVideoPredefinedPath");
		saveVideoPredefinedPath.addActionListener(this);
		saveVideo = new JMenuItem("Pfad angeben");
		saveVideo.setActionCommand("saveVideo");
		saveVideo.addActionListener(this);
		stopSavingVideo = new JMenuItem("Aufnahme stoppen");
		stopSavingVideo.setActionCommand("stopSavingVideo");
		stopSavingVideo.addActionListener(this);
		stopSavingVideo.setEnabled(false);
		
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
	
	private void configureVideoRecordingOptions(boolean isRecording) {
		saveVideo.setEnabled(!isRecording);
		saveVideoPredefinedPath.setEnabled(!isRecording);
		stopSavingVideo.setEnabled(isRecording);
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
			
		}else {
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
