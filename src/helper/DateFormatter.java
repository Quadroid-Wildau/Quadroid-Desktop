package helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Clas for converting date to string
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class DateFormatter {

	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yyyy HH:mm:ss");
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * Date to String
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	
	/**
	 * Date from millis to String
	 * @param millis
	 * @return
	 */
	public static String formatDate(long millis) {
		calendar.setTimeInMillis(millis);
		return sdf.format(calendar.getTime());
	}
}
