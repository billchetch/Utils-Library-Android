package net.chetch.utilities;

import android.util.MonthDisplayHelper;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DatePeriod{

    public enum InitiaDateOptions{
        NOW_AS_END_OF_PERIOD,
        NOW_AS_START_OF_PERIOD
    }

    public static DatePeriod getPeriod(Calendar initialDate, TimeUnit timeUnit, int numberOfTimeUnits, int periodOffset){
        long durationInMillis = TimeUnit.MILLISECONDS.convert(numberOfTimeUnits, timeUnit);

        long fromMillis = initialDate.getTimeInMillis() + durationInMillis*periodOffset;
        long toMillis = fromMillis + durationInMillis;

        return new DatePeriod(fromMillis, toMillis, timeUnit);
    }

    public static Calendar getInitialDate(TimeUnit timeUnit, int offset){
        Calendar initialDate = Calendar.getInstance();
        switch(timeUnit){
            case HOURS:
                initialDate.set(Calendar.MINUTE, 0);
                initialDate.set(Calendar.SECOND, 0);
                break;

            case DAYS:
                initialDate = Utils.calendarZeroTime(initialDate);
                break;
        }

        long millis = initialDate.getTimeInMillis();
        if(offset != 0)initialDate.setTimeInMillis(millis + TimeUnit.MILLISECONDS.convert(offset, timeUnit));

        return initialDate;
    }


    public static DatePeriod getPeriod(TimeUnit timeUnit, InitiaDateOptions initiaDateOptions, int numberOfTimeUnits, int periodOffset){
        int offset = 0;
        switch(initiaDateOptions){
            case NOW_AS_END_OF_PERIOD:
                offset = 1 - numberOfTimeUnits;
                break;
            case NOW_AS_START_OF_PERIOD:
                offset = 0;
                break;
        }

        Calendar initialDate = getInitialDate(timeUnit, offset);
        return getPeriod(initialDate, timeUnit, numberOfTimeUnits, periodOffset);
    }

    public static DatePeriod getPeriod(TimeUnit timeUnit, int numberOfTimeUnits, int periodOffset){
        return getPeriod(timeUnit, InitiaDateOptions.NOW_AS_END_OF_PERIOD, numberOfTimeUnits, periodOffset);
    }

    //Instance
    public Calendar fromDate;
    public Calendar toDate;
    public TimeUnit timeUnit;

    public DatePeriod(Calendar fromDate, Calendar toDate, TimeUnit timeUnit){
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.timeUnit = timeUnit;
    }

    public DatePeriod(long fromMillis, long toMillis, TimeUnit timeUnit){
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTimeInMillis(fromMillis);
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeInMillis(toMillis);

        this.fromDate = fromDate;
        this.toDate = toDate;
        this.timeUnit = timeUnit;
    }

    public long getDuration(TimeUnit timeUnit){
        long durationInMillis = toDate.getTimeInMillis() - fromDate.getTimeInMillis();
        return timeUnit.convert(durationInMillis, TimeUnit.MILLISECONDS);
    }

    public long getDuration(){
        return getDuration(TimeUnit.MILLISECONDS);
    }

    public String toString(String dateFormat){
        return Utils.formatDate(fromDate, dateFormat) + " to " + Utils.formatDate(toDate, dateFormat);
    }
}
