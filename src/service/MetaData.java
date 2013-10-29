package service;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import communication.CommunicationStack;
import communication.Flight;

public class MetaData extends Observable{
	
	private static MetaData instance;

	public static MetaData getInstance() {
		if (instance == null) {
			instance = new MetaData();
			instance.initTestTimer();
		}
		
		return instance;
	}

	public model.MetaData getMetaData() {
		return this.getFlightCommunication().getMetaData();
	}
	
	private Flight getFlightCommunication() {
		return CommunicationStack.getInstance().getFlightCommunicator();
	}
	
	private void initTestTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				MetaData.getInstance().setChanged();
				MetaData.getInstance().notifyObservers();
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
}
