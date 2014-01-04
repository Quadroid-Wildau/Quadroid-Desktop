package handler;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import interfaces.AbstractReceiver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;

import coder.decoder.ObserverHandler;
import coder.decoder.RxDataDecoder;

/**
 * this handler manage incoming data for xbee device
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7)
 * @see AbstractReceiver 
 * 
 * */

public class XBeeReceiverHandler extends AbstractReceiver
{
	
	@Override
	public void serialEvent(SerialPortEvent evnt, SerialPort port, 
			InputStream istream, OutputStream ostream, Logger logger) 
	{
		//if data available get in
		if(evnt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{	
			
			//buffer for input data
			ByteArrayOutputStream rxbase64buffer =  new ByteArrayOutputStream();
			int rx = 0;
			try 
			{
				while((rx = istream.read()) != -1)
				{
					byte b = (byte)rx;
					if((char)b == '|')//end marker 
						break;
					rxbase64buffer.write(b);//save data into bytestream
				}
				port.notifyOnDataAvailable(false);
			} catch (IOException e) 
			{
				logger.error("Receiver-Exception: ", e);
			}	
				byte[] data = rxbase64buffer.toByteArray();
				if(data.length >= 6)//only do if really data are available no endmarkers
					new RxDataDecoder(data, ObserverHandler.getReference());//decoded data
				port.notifyOnDataAvailable(true);
				try 
				{
					rxbase64buffer.close();//close buffer
				} catch (IOException e)
				{
					logger.error("BufferCloseException", e);
				}
			
		
		}
	}

}
