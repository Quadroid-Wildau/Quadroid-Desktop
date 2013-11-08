package main;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import communication.CommunicationStack;

import controller.ViewController;

public class Main {
	private static JFrame mainWindow;
	private static ViewController mainController;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().put("http.proxyHost", "proxy.th-wildau.de");
		System.getProperties().put("http.proxyPort", "8080");
		
		mainWindow = new JFrame("Mission Control");
		mainWindow.setSize(800, 400);
		mainWindow.getContentPane().add(getMainController().getView());
		mainWindow.setVisible(true);
		
		initTestTimer();
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
			mainController = new controller.Main();
		}
		
		return mainController;
	}
}
