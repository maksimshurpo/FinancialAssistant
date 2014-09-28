package com.shurpo.financialassistant.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getCurrentDate(){
        return getFormatDate(new Date());
    }

    public static String getLastMonthDate(int countMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentMonth = calendar.get(Calendar.MONTH);
        if(currentMonth < 5){
           calendar.roll(Calendar.YEAR, -1);
        }
        calendar.roll(Calendar.MONTH, -countMonth);
        Date dateFormat = calendar.getTime();
        return getFormatDate(dateFormat);
    }

    public static String getFormatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public static String getLastWeekDate(int countWeeks){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(currentDay < 6){
            calendar.roll(Calendar.MONTH, -1);
        }
        calendar.roll(Calendar.WEEK_OF_MONTH, -countWeeks);
        Date dateFormat = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(dateFormat);
    }
}
