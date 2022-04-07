package com.fimet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {
	public static final SimpleDateFormat yyMMddhhmmss_FMT = new SimpleDateFormat("yyMMddkkmmss");
	public static final SimpleDateFormat MMddhhmmss_FMT = new SimpleDateFormat("MMddkkmmss");
	//public static final SimpleDateFormat kkmmss_FMT = new SimpleDateFormat("kkmmss");
	public static final SimpleDateFormat hhmmss_FMT = new SimpleDateFormat("kkmmss");
	public static final SimpleDateFormat yyyyMMdd_FMT = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat hh_mm_ss_FMT = new SimpleDateFormat("hh:mm:ss");
	public static final SimpleDateFormat hhmmss000_FMT = new SimpleDateFormat("hh:mm:ss.000");
	public static final SimpleDateFormat yyyyMMdd_hhmmssSSS_FMT = new SimpleDateFormat("yyyyMMdd hh:mm:ss.SSS");
	public static final SimpleDateFormat hhmmssSSS_FMT = new SimpleDateFormat("hh:mm:ss.SSS");
	public static final SimpleDateFormat YYMMDD_hhmmss = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
	public static final SimpleDateFormat TIMESTAMP_DAY_FMT = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
	public static final SimpleDateFormat DATE_TIME_FMT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	public static final SimpleDateFormat MMdd_FMT = new SimpleDateFormat("MMdd");
	public static final SimpleDateFormat MMddyy_FMT = new SimpleDateFormat("MMddyy");
	public static final SimpleDateFormat yyMMdd_FMT = new SimpleDateFormat("yyMMdd");
	public static final SimpleDateFormat HTTP_FMT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
	static {
		HTTP_FMT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	private DateUtils() {
	}
	public static boolean timeIsLessThan(String ts1, String ts2) {
		checkTimeFormat(ts1);
		checkTimeFormat(ts2);
		if (Integer.parseInt(ts2.substring(0,2)) < Integer.parseInt(ts1.substring(0,2))){
			return true;
		} else if (Integer.parseInt(ts2.substring(3,5)) > Integer.parseInt(ts1.substring(3,5))){
			return true;
		} else if (Integer.parseInt(ts2.substring(6,8)) > Integer.parseInt(ts1.substring(6,8))){
			return true;
		} else {
			return Integer.parseInt(ts2.substring(9)) > Integer.parseInt(ts1.substring(9));
		}
	}
	public static boolean timeIsGreatestThan(String ts1, String ts2) {
		checkTimeFormat(ts1);
		checkTimeFormat(ts2);
		if (Integer.parseInt(ts1.substring(0,2)) < Integer.parseInt(ts2.substring(0,2))){
			return false;
		} else if (Integer.parseInt(ts1.substring(3,5)) < Integer.parseInt(ts2.substring(3,5))){
			return false;
		} else if (Integer.parseInt(ts1.substring(6,8)) < Integer.parseInt(ts2.substring(6,8))){
			return false;
		} else {
			return Integer.parseInt(ts1.substring(9)) > Integer.parseInt(ts2.substring(9));
		}
	}
	private static void checkTimeFormat(String ts) {
		if (ts == null) {
			throw new NullPointerException();
		}
		if (!ts.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}")) {
			throw new RuntimeException("Invalid format for time: "+ts);
		}
	}
	public static boolean timestampIsGreatestThan(String ts1, String ts2) {
		checkTimestampFormat(ts1);
		checkTimestampFormat(ts2);
		try {
			if (ts1.substring(0,ts1.lastIndexOf('.')).equals(ts2.substring(0,ts2.lastIndexOf('.')))) {
				return Integer.parseInt(ts1.substring(ts1.lastIndexOf('.')+1)) > Integer.parseInt(ts2.substring(ts2.lastIndexOf('.')+1));
			} else if (DATE_TIME_FMT.parse(ts1.substring(0,ts1.lastIndexOf('.'))).after(DATE_TIME_FMT.parse(ts2.substring(0,ts2.lastIndexOf('.'))))) {
				return true;
			} else {
				return  false;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Invalid format for timestamp: "+ts1+","+ts2,e);
		}
	}
	public static boolean timestampIsLessThan(String ts1, String ts2) {
		checkTimestampFormat(ts1);
		checkTimestampFormat(ts2);
		try {
			if (ts1.substring(0,ts1.lastIndexOf('.')).equals(ts2.substring(0,ts2.lastIndexOf('.')))) {
				return  Integer.parseInt(ts1.substring(ts1.lastIndexOf('.')+1)) < Integer.parseInt(ts2.substring(ts2.lastIndexOf('.')+1));
			} else if (DATE_TIME_FMT.parse(ts1.substring(0,ts1.lastIndexOf('.'))).before(DATE_TIME_FMT.parse(ts2.substring(0,ts2.lastIndexOf('.'))))) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Invalid format for timestamp: "+ts1+","+ts2,e);
		}
	}
	private static void checkTimestampFormat(String ts) {
		if (ts == null) {
			throw new NullPointerException();
		}
		if (!ts.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{1,3}")) {
			throw new RuntimeException("Invalid format for timestamp: "+ts);
		}
	}
	public static long addDays(long date, int days) {
		return addDays(new Date(date), days).getTime();
	}
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
	public static Date addHours(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hours);
		return calendar.getTime();
	}
	public static Date addMinutes(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}
	public static String formatTimestamp(String timestamp) {
		if (timestamp == null || timestamp.length() == 0) {
			return timestamp;
		}
		timestamp = StringUtils.rightTrim(timestamp, '0');
		if (timestamp.endsWith(".")) {
			timestamp = timestamp+"0";
		}
		return timestamp;
	}
	public static Date parseHour(String hhmm) {
		try {
			return DATE_TIME_FMT.parse("2000-01-01 "+hhmm+":00");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	public static long getHourLong(Date date) {
		return (date.getTime() % 86400000) / 3600000;
	}
	public static Date getHourDate(Date date) {
		return new Date((date.getTime() % 86400000) / 3600000);
	}
	public static long getDayLong(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
	public static Date getDayDate(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	public static int getMinute(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}
	public static int getHourOfDay(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	public static int getDayOfYear(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	public static int getYear(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	@SuppressWarnings("deprecation")
	public static Date addHours(Date date, Date hours) {
		return addMinutes(addHours(date, hours.getHours()), hours.getMinutes());
	}
	public static Date setTime(Date date, int hours, int minutes, int seconds) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		/*if (hours / 24 > 0) {
			calendar.set(Calendar.DAY_OF_YEAR, date.getDay()+(hours/24));	
		}*/
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	public static String formatDateExtractorFile(Date day) {
		return MMddyy_FMT.format(day);
	}
	public static String formatDateExtractorFile(long day) {
		return MMddyy_FMT.format(new Date(day));
	}
	public static String formatyyMMdd(Date date) {
		return yyMMdd_FMT.format(date);
	}
	public static String formatMMddyy(Date date) {
		return MMddyy_FMT.format(date);
	}
	public static String formathhmmss(Date date) {
		return hh_mm_ss_FMT.format(date);
	}
	public static String formatyyyyMMdd(Date date) {
		return yyyyMMdd_FMT.format(date);
	}
	public static void main(String[] args) {
		try {
			Date date = parseyyyyMMddhhmmssSSS("20200705 11:09:40.481");
			System.out.println(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final String PATTERN_TIME = "[0-9]{8} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}";
	public static Date parseyyyyMMddhhmmssSSS(String dateText) throws ParseException {
		if (!StringUtils.isBlank(dateText) && dateText.matches(PATTERN_TIME)) {
			return yyyyMMdd_hhmmssSSS_FMT.parse(dateText);
		} else {
			throw new ParseException("Invalid Date "+dateText+" for format 'yyyyMMdd hh:mm:ss.SSS'", 0);
		}
	}
	public static String formatyyyyMMddhhmmssSSS(Date date) {
		return yyyyMMdd_hhmmssSSS_FMT.format(date);
	}
	public static long parsehhmmssSSSAsTime(String yyyyMMdd, String time) {
		String dateText = yyyyMMdd+" "+time;
		try {
			Date date = parseyyyyMMddhhmmssSSS(dateText);
			return date != null ? date.getTime() : -1;
		} catch (Exception e) {
			return -1;
		}
	}
	public static long parsehhmmssSSSAsTime(String time) {
		String yyyyMMdd = yyyyMMdd_FMT.format(new Date());
		return parsehhmmssSSSAsTime(yyyyMMdd, time);
	}
	public static String formatyyMMddhhmmss(Date date) {
		return YYMMDD_hhmmss.format(date);
	}
	public static String formathhmmssSSS(Date date) {
		return hhmmssSSS_FMT.format(date);
	}
}
