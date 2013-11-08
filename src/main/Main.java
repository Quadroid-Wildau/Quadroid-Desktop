package main;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MainController mainController;
	private JMenuBar menuBar;
	private JMenu menuFile, menuVideo, subMenuVideoDevice;
	private JMenuItem menuItemVideo0, menuItemVideo1, menuItemVideo2, menuItemVideo3;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
		System.getProperties().put("http.proxyPort", "8080");
		
		getMainController();
		new Main();
		
		initTestTimer();
	}

	public Main() {
		setSize(1024, 768);
		getContentPane().add(getMainController().getView());
		
		menuBar = new JMenuBar();
		menuFile = new JMenu("Datei");
		menuVideo = new JMenu("Video");
		
		menuBar.add(menuFile);
		menuBar.add(menuVideo);
		
		subMenuVideoDevice = new JMenu("Video Device");
		menuVideo.add(subMenuVideoDevice);
		
		menuItemVideo0 = new JMenuItem("Device 1");
		menuItemVideo0.addActionListener(this);
		menuItemVideo1 = new JMenuItem("Device 2");
		menuItemVideo1.addActionListener(this);
		menuItemVideo2 = new JMenuItem("Device 3");
		menuItemVideo2.addActionListener(this);
		menuItemVideo3 = new JMenuItem("Device 4");
		menuItemVideo3.addActionListener(this);
		
		subMenuVideoDevice.add(menuItemVideo0);
		subMenuVideoDevice.add(menuItemVideo1);
		subMenuVideoDevice.add(menuItemVideo2);
		subMenuVideoDevice.add(menuItemVideo3);
		
		setJMenuBar(menuBar);
		
		setVisible(true);
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
	
	public static ViewController getMainController() {
		if (mainController == null) {
			mainController = new MainController();
		}
		
		return mainController;
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		VideoStreamController videoStreamController = (VideoStreamController) mainController.getVideoStreamController();
		
		try {
			if (e.getSource() == menuItemVideo0) {
				videoStreamController.startVideoStream(0);
			} else if (e.getSource() == menuItemVideo1) {
				videoStreamController.startVideoStream(1);
			} else if (e.getSource() == menuItemVideo2) {
				videoStreamController.startVideoStream(2);
			} else if (e.getSource() == menuItemVideo3) {
				videoStreamController.startVideoStream(3);
			}
		} catch (Exception ex) {
			System.err.println("Verdammt n Error man");
			ex.printStackTrace();
		}
	}
}
