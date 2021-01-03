package net.chetch.utilities;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DatePeriod{
    public Calendar fromDate;
    public Calendar toDate;

    public DatePeriod(Calendar fromDate, Calendar toDate){
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public DatePeriod(long fromMillis, long toMillis){
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTimeInMillis(fromMillis);
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeInMillis(toMillis);

        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static DatePeriod getPeriod(Calendar initialDate, TimeUnit timeUnit, int numberOfTimeUnits, int periodOffset){
        long durationInMillis = TimeUnit.MILLISECONDS.convert(numberOfTimeUnits, timeUnit);

        long fromMillis = initialDate.getTimeInMillis() + durationInMillis*periodOffset;
        long toMillis = fromMillis + durationInMillis;

        return new DatePeriod(fromMillis, toMillis);
    }

    public static DatePeriod getPeriod(TimeUnit timeUnit, int numberOfTimeUnits, int periodOffset){
        return getPeriod(Calendar.getInstance(), timeUnit, numberOfTimeUnits, periodOffset);
    }

    public static DatePeriod getHourPeriod(int periodOffset, int numberOfUnits){
        return getPeriod(TimeUnit.HOURS, numberOfUnits, periodOffset);
    }

    public static DatePeriod getHourPeriod(int periodOffset){
        return getHourPeriod(periodOffset, 1);
    }

    public static DatePeriod getDayPeriod(int periodOffset, int numberOfUnits){
        return getPeriod(TimeUnit.DAYS, numberOfUnits, periodOffset);
    }

    public static DatePeriod getDayPeriod(int periodOffset){
        return getDayPeriod(periodOffset, 1);
    }

    public static DatePeriod getWeekPeriod(int periodOffset, int numberOfUnits){
        return getPeriod(TimeUnit.DAYS, numberOfUnits, periodOffset);
    }

    public static DatePeriod getWeekPeriod(int periodOffset){
        return getWeekPeriod(periodOffset, 1);
    }
}
