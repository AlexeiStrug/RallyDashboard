package com.ge.dashboard.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Component("dateConversion")
public class DateConversion {

    public Date getStringCurrentDate() {
        Calendar cal = GregorianCalendar.getInstance();
        Date ninetyDaysAgoDate = cal.getTime();

        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
        return getStringCurrentDate(iso.format(ninetyDaysAgoDate));
    }

    public Date getStringCurrentDate(String date) {
        if (date == null || date.equals("")) throw new NoSuchElementException();
        return java.sql.Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        if (date1 == null || date2 == null) throw new NoSuchElementException();
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
