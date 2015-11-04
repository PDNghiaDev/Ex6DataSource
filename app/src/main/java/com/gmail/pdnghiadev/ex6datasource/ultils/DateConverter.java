package com.gmail.pdnghiadev.ex6datasource.ultils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by PDNghiaDev on 11/3/2015.
 */
public class DateConverter {


    // Convert from epoch to human readable date
    // Example: 1446536186 => 11/03/2015 02:36:26
    public String convertEpochToDate(long epoch, String formatDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatDate); // MM/dd/yyyy HH:mm:ss
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String date = dateFormat.format(epoch * 1000);

        return date;
    }

    // Convert from human readable date to epoch
    // Example: 11/03/2015 02:36:26 => 1446536186
    public long convertDateToEpoch(String date, String formatDate) throws ParseException {
        long epoch = new SimpleDateFormat(formatDate).parse(date).getTime() / 1000;

        return epoch;
    }

    // Get current epoch time
    public long getEpochCurrtent() {
        return System.currentTimeMillis() / 1000;
    }

    // Convert string to Date
    // Example: String str = "11/03/2015"
    // You need to note String format, you can change SimpleDateFormat format
    public Date stringToDate(String string, String formatDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatDate); // Can change format
        Date date = format.parse(string);

        return date;
    }

    public Calendar parseDate(Date date) {
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        int hour = Integer.parseInt(new SimpleDateFormat("HH").format(date));
        int minute = Integer.parseInt(new SimpleDateFormat("mm").format(date));
        int second = Integer.parseInt(new SimpleDateFormat("ss").format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);

        return calendar;
    }

    public String displayTime(long epoch) {
        long epochCurrent = getEpochCurrtent();
        long minute = (epochCurrent - epoch) / 60;
        String result = "";

        if (minute == 1) {
            result = "an minute ago";
        } else if (minute > 1 && minute < 60) {
            result = minute + " minutes ago";
        } else {
            long hour = minute / 60;
            if (hour == 1) {
                result = "an hour ago";
            } else if (hour > 1 && hour < 24) {
                result = hour + " hours ago";
            } else {

                long day = hour / 24;
                if (day == 1) {
                    result = "1 day ago";
                } else if (day > 1 && day < 30) {
                    result = day + " days ago";
                }
            }
        }

        return result;
    }
}
