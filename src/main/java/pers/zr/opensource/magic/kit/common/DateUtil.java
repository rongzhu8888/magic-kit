package pers.zr.opensource.magic.kit.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_sss = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String HH_MM_SS = "HH:mm:ss";

	public static final Logger log = LoggerFactory.getLogger(DateUtil.class);

	public static Date parseDate(String dateStr, String format) {
		Date date;
		try {
			DateFormat	dateFormat = new SimpleDateFormat(format);
			String dt = dateStr.replaceAll("/", "-");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]", "0");
			}
			date = (Date) dateFormat.parse(dt);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return date;
	}

	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	public static long getMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}


	public static String getDate(Date date) {
		return format(date, YYYY_MM_DD);
	}

	public static String getTime(Date date) {
		return format(date, HH_MM_SS);
	}

	public static String getDateTime(Date date) {
		return format(date, YYYY_MM_DD_HH_MM_SS);
	}

	public static Date addDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static Date addYear(Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + years); //加3年
		return calendar.getTime();
	}

	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	public static String getMonthBegin(String dateString) {
		Date date = parseDate(dateString, YYYY_MM_DD);
		return format(date, "yyyy-MM") + "-01";
	}

	public static String getMonthEnd(String dateString) {
		Date  date = parseDate(getMonthBegin(dateString), YYYY_MM_DD);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return format(calendar.getTime(), YYYY_MM_DD);
	}


	public static int dateCompare(String date1 ,String date2){
		int num= -1;
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		try {
			num = sdf.parse(date1).compareTo(sdf.parse(date2));
		} catch (ParseException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return num;
	}

	public static void main(String []rags) {
		System.out.println(DateUtil.format(new Date(), YYYY_MM_DD_HH_MM_SS_sss));
	}

}
