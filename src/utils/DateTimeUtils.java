/**
 * 
 */
package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.DateFormat;

/**
 * Utilities class for work with DateTime
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class DateTimeUtils {

	/**
	 * Private constructor. Utils class cannot be instantiated
	 */
	private DateTimeUtils() {
		// do nothing
	}

	/**
	 * Gets current date in string representation
	 * 
	 * @param aContext
	 *            Context
	 * @param aDate
	 *            Current date
	 * @return Current date
	 */
	public static String getCurrentTimeString(Context aContext, long aDate) {
		String pattern = "";
		if (DateFormat.is24HourFormat(aContext)) {
			pattern = "dd-MM-yyyy  HH:mm";
		} else {
			pattern = "dd-MM-yyyy  hh:mm a";
		}
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTimeInMillis(aDate);

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setTimeZone(TimeZone.getDefault());
		String result = formatter.format(calendar.getTimeInMillis());
		return result;
	}
}
