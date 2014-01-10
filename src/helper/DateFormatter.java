package helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yyyy HH:mm:ss");
	private static Calendar calendar = Calendar.getInstance();
	
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	
	public static String formatDate(long millis) {
		calendar.setTimeInMillis(millis);
		return sdf.format(calendar.getTime());
	}
}
