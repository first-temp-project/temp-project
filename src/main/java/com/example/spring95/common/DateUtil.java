package com.example.spring95.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getDatePath() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString();
	}

	public static String getYesterDayPath() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()).toString();
	}

	public static String formatDateString(String registerDate) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(registerDate);
			return new SimpleDateFormat("yy-MM-dd HH::mm").format(date).toString();
		} catch (ParseException e) {
			e.printStackTrace();
			return registerDate;
		}
	}
}
