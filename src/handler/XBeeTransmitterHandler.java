package handler;

import interfaces.AbstractTransmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connection.Connect;


/**
 * 
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 27.11.2013 (JDK 7)
 *  
 * */

public class XBeeTransmitterHandler extends AbstractTransmitter
{
	/**reference of logger*/
	private Logger logger = LoggerFactory.getLogger(XBeeTransmitterHandler.class.getName());
	
	
	/**
	 * public Constructor 
	 * 
	 * @param connection -
	 * hand over an connection of typ {@link de.th_wildau.quadroid.connection.Connect}
	 * 
	 * @throws NullPointerException if connection are <b>null</b> 
	 * 
	 * */
	public XBeeTransmitterHandler(Connect connection) throws NullPointerException
	{	
		super(connection);
	}
		

	/**
	 * this function transmit data to xBee ground station
	 * 
	 * @param msg - hand over data to transmit
	 * */
	@Override
	public void transmit(byte[] msg)
	{
		if(msg == null)
			return;
		
	  try
	  {	
		super.getConnection().getOutputStream().write(msg);//send data
		super.getConnection().getOutputStream().write("|||||||||".getBytes());//send end marker
		super.getConnection().getOutputStream().flush();//flush pipe
		logger.debug("Transmit Data");
	  }catch(Exception e)
	  {
		  logger.error("Transmission Exception: ", e);
	  }	
		
	}
	
	
}
