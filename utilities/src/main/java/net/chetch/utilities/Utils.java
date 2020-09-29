package net.chetch.utilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/*
Utils class for surf forecast data xxx
 */

public class Utils {
    public enum Conversions{ METERS_2_FEET, KPH_2_MPH, KM_2_MILES, MILES_2_KM, MPH_2_KPH, ROTATE_180, DEG_2_COMPASS}

    //TODO: change this to use resources so its translatable
    final private static String[] COMPASS = new String[]{"N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};

    final private static String CHECK_INTERNET_HOST = "www.google.com";
    final private static int CHECK_INTERNET_PORT = 80;

    final public static int HOUR_IN_MILLIS = 3600*1000;
    final public static int MINUTE_IN_MILLIS = 1000*60;
    final public static int DAY_IN_MILLIS = 24*3600*1000;

    /*
    Conversion funcs
    */
    public static String convert(String val, Conversions conversion, int dp){
        if(val == null)return val;

        switch(conversion){
            case METERS_2_FEET:
                double mtr = Double.parseDouble(val);
                double ft = mtr*100/(2.54*12);
                return round2string(ft, dp);

            case KM_2_MILES:
            case KPH_2_MPH:
                double km = Double.parseDouble(val);
                double mi = km*0.6;
                return round2string(mi, dp);

            case MILES_2_KM:
            case MPH_2_KPH:
                double miles = Double.parseDouble(val);
                double kms = miles/0.6;
                return round2string(kms, dp);

            case ROTATE_180:
                double deg = Double.parseDouble(val);
                return round2string((deg + 180.0) % 360.0, dp);

            case DEG_2_COMPASS:
                deg = Double.parseDouble(val);
                int idx = (int)round((deg/360.0)*(double)COMPASS.length, 0) % COMPASS.length;
                return COMPASS[idx];

            default:
                return val;

        }
    }
    public static String convert(double val, Conversions conversion, int dp) {
        return convert(Double.toString(val), conversion, dp);
    }
    public static String convert(float val, Conversions conversion, int dp) {
        return convert(Float.toString(val), conversion, dp);
    }

    public static String convert(int val, Conversions conversion) {
        return convert(Float.toString(val), conversion, 0);
    }

    public static String round2string(double v, int dp){
        if(dp > 0 || dp < 0) {
            return Double.toString(round(v, dp));
        } else {
            int i = (int)round(v, 0);
            return Integer.toString(i);
        }
    }

    public static double round(double v, int dp){
        double shift = Math.pow(10, (double)dp);
        if(dp > 0) {
            return ((double) Math.round(v * shift)) / shift;
        } else if(dp == 0) {
            return Math.round(v);
        } else {
            return v;
        }
    }

    /*
    Date and Calendar methods
     */
    public static Calendar calendarSetHour(Calendar cal, int hour){
        Calendar newCal = calendarZeroTime(cal);
        newCal.set(Calendar.HOUR_OF_DAY, hour);
        return newCal;
    }

    public static Calendar calendarZeroTime(Calendar cal){
        Calendar newCal = (Calendar)cal.clone();
        if(newCal.get(Calendar.HOUR_OF_DAY) != 0)newCal.set(Calendar.HOUR_OF_DAY, 0);
        if(newCal.get(Calendar.MINUTE) != 0)newCal.set(Calendar.MINUTE, 0);
        if(newCal.get(Calendar.SECOND) != 0)newCal.set(Calendar.SECOND, 0);
        if(newCal.get(Calendar.MILLISECOND) != 0)newCal.set(Calendar.MILLISECOND, 0);

        //TODO: verify this works
        if(newCal.get(Calendar.DST_OFFSET) != 0)newCal.set(Calendar.DST_OFFSET, 0);

        return newCal;
    }

    //TODO: handle DST (daylight savings) offset fro 'DAYS' measurement
    public static long dateDiff(Calendar cal1, Calendar cal2, TimeUnit timeUnit){
        long duration = cal1.getTimeInMillis() - cal2.getTimeInMillis();
        switch(timeUnit){
            case HOURS:
                return (long)Math.floor(TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS));

            case DAYS:
                return (long)Math.floor(TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS));

            case MINUTES:
                return (long)Math.floor(TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS));

            case SECONDS:
                return (long)Math.floor(TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS));

            default:
                return 0;
        }
    }

    public static long dateDiff(Date d1, Date d2, TimeUnit timeUnit){
        return dateDiff(date2cal(d1), date2cal(d2), timeUnit);
    }
    public static long dateDiff(Calendar cal1, Calendar cal2){
        Calendar cz1 = calendarZeroTime(cal1);
        Calendar cz2 = calendarZeroTime(cal2);
        return dateDiff(cz1, cz2, TimeUnit.DAYS);
    }
    public static long hoursDiff(Calendar cal1, Calendar cal2){
        return dateDiff(cal1, cal2, TimeUnit.HOURS);
    }

    public static boolean dateInRange(Calendar cal, Calendar cal1, Calendar cal2){
        if(cal1.compareTo(cal2) > 0){
            Calendar swap = (Calendar)cal1.clone();
            cal2 = cal1;
            cal1 = swap;
        }
        return cal.compareTo(cal1) >= 0 && cal.compareTo(cal2) <= 0;
    }

    public static boolean isToday(Calendar cal, Calendar now){
        Calendar cal1 = calendarSetHour(now, 0);
        Calendar cal2 = (Calendar)cal1.clone();
        cal2.add(Calendar.DATE, 1);
        cal2.add(Calendar.MILLISECOND, -1);
        return dateInRange(cal, cal1, cal2);
    }

    public static boolean isToday(Calendar cal){
        return isToday(cal, Calendar.getInstance());
    }

    public static boolean isToday(Date date){
        return isToday(date2cal(date));
    }

    public static boolean isTomorrow(Calendar cal, Calendar now){
        Calendar c = (Calendar)cal.clone();
        c.add(Calendar.DATE, -1);
        return isToday(c, now);
    }

    public static boolean isTomorrow(Calendar cal){
        return isTomorrow(cal, Calendar.getInstance());
    }


    public static boolean isTomorrow(Date date){
        return isTomorrow(date2cal(date));
    }

    public static boolean isYesterday(Calendar cal, Calendar now){
        Calendar c = (Calendar)cal.clone();
        c.add(Calendar.DATE, 1);
        return isToday(c, now);
    }

    public static boolean isYesterday(Calendar cal){
        return isYesterday(cal, Calendar.getInstance());
    }

    public static boolean isYesterday(Date date){
        return isYesterday(date2cal(date));
    }

    public static List<Calendar> getDates(Calendar cal1, Calendar cal2){
        List<Calendar> dates = new ArrayList<>();
        if(cal1.compareTo(cal2) == 0){
            dates.add(cal1);
            return dates;
        } else if(cal1.compareTo(cal2) > 0){
            Calendar swap = (Calendar)cal1.clone();
            cal2 = cal1;
            cal1 = swap;
        }

        long days = dateDiff(cal2, cal1, TimeUnit.DAYS);
        for(int i = 0; i < days; i++){
            Calendar cal = (Calendar)cal1.clone();
            cal.add(Calendar.DATE, i);
            dates.add(cal);
        }
        return dates;
    }

    public static Calendar date2cal(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatDate(Date date, String format, TimeZone tz){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if(tz != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            sdf.setTimeZone(tz);
            return sdf.format(cal.getTime());
        } else {
            return sdf.format(date);
        }
    }

    public static String formatDate(Calendar cal, String format, TimeZone tz){
        return formatDate(cal.getTime(), format, tz);
    }

    public static String formatDate(Date date, String format){
        return formatDate(date, format, null);
    }

    public static String formatDate(Calendar cal, String format){
        return formatDate(cal, format, cal.getTimeZone());
    }

    public static Calendar parseDate(String dateString, String format) throws java.text.ParseException{
        DateFormat dateFormat = new SimpleDateFormat(format);
        return Utils.date2cal(dateFormat.parse(dateString));
    }

    public  enum DurationFormat{
        D_H_M_S,
        DAYS_HOURS_MINS_SECS
    }

    private static String pluralise(int val, String delimiter, String one, String many){
        return val + delimiter + (val == 1 ? one : many);
    }

    private static String pluralise(int val, String one){
        return pluralise(val, " ", one, one + "s");
    }

    public static String formatDuration(long durationInMillis, DurationFormat durationFormat) {
        if (durationInMillis <= 0) return null;
        String formatted = "";
        switch(durationFormat){
            case DAYS_HOURS_MINS_SECS:
            case D_H_M_S:
                int totalSecs = (int)(durationInMillis / 1000);
                int totalDays = totalSecs / (24*3600);
                int remainderSecs = (totalSecs - totalDays*24*3600);
                int remainderHours = remainderSecs / 3600;
                remainderSecs = remainderSecs - remainderHours*3600;
                int remainderMinutes = remainderSecs / 60;
                remainderSecs = remainderSecs - remainderMinutes*60;

                boolean longformat = durationFormat == DurationFormat.DAYS_HOURS_MINS_SECS;
                if(totalDays > 0){
                    formatted += longformat ? pluralise(totalDays, "day") : totalDays + "d";
                }
                if(remainderHours > 0){
                    formatted += " " + (longformat ? pluralise(remainderHours, "hour") : remainderHours +  "h");
                }
                if(remainderMinutes > 0){
                    formatted += " " + (longformat ? pluralise(remainderMinutes, "min") : remainderMinutes + "m");
                }
                if(remainderSecs > 0){
                    formatted += " " + (longformat ? pluralise(remainderSecs, "sec") : remainderSecs + "s");
                }
                break;
        }
        return formatted.trim();
    }

    public static String formatDuration(long durationInMillis){
        return formatDuration(durationInMillis, DurationFormat.DAYS_HOURS_MINS_SECS);
    }

    /*
    Distance funcs
     */

    //public static

    /*
    Network funcs
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static boolean isInternetAvailable(String host, int port){
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            return false;
        }
    }

    public static boolean isInternetAvailable(){
        return isInternetAvailable(CHECK_INTERNET_HOST, CHECK_INTERNET_PORT);
    }


    /*
    File funcs
     */
    public static boolean writeFile(Context context, String filename, String data){
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    public static String readFile(Context context, String filename){
        try {
            StringBuilder text = new StringBuilder();
            FileInputStream fis = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));

            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            return text.toString();
        } catch (Exception ex){
            return null;
        }
    }

    public static String stackTrace2String(Throwable t){
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }

    /*
    Download funcs
     */
    public static Bitmap getImage(String url) throws Exception{
        InputStream input = new java.net.URL(url).openStream();
        return BitmapFactory.decodeStream(input);
    }

    public static void downloadImage(String url, final Observer<Bitmap> observer){
        final String url2download = url;
        try {
            Runnable runnable = new Runnable(){
                public void run(){
                    try{
                        Bitmap bm = getImage(url2download);
                        if(observer != null){
                            observer.onChanged(bm);
                        }
                    } catch (Exception e){
                        if(observer != null){
                            observer.onChanged(null);
                        }
                        e.printStackTrace();
                    }
                }
            };
            Thread downloadThread = new Thread(runnable);
            downloadThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadImages(String[] urls, final Observer<HashMap<String, Bitmap>> observer){
        downloadImages(Arrays.asList(urls), observer);
    }

    public static void downloadImages(Set<String> urls, final Observer<HashMap<String, Bitmap>> observer){
        downloadImages(new ArrayList<>(urls), observer);
    }

    public static void downloadImages(List<String> urls, final Observer<HashMap<String, Bitmap>> observer){
        final List<String> urls2download = urls;

        try {
            Runnable runnable = new Runnable(){
                public void run(){
                    HashMap<String, Bitmap> bitmaps = new HashMap<>();
                    for(String url : urls2download){
                        try{
                            bitmaps.put(url, getImage(url));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(observer != null){
                        observer.onChanged(bitmaps);
                    }
                }
            };
            Thread downloadThread = new Thread(runnable);
            downloadThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
