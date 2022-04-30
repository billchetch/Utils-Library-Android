package net.chetch.utilities;

import android.util.Log;
/*
Safe Log class ... handles null and can also turn off all debugging for release versions
 */

public class SLog {
    public static boolean LOG =  true; //set this to disable all logging
    private static final String NULL_MSG = "[null]";

    private static String sanitizeNull(String s){
        return s == null ? NULL_MSG : s;
    }

    public static void e(String tag, String msg){
        if(LOG) Log.e(tag, sanitizeNull(msg));
    }

    public static void i(String tag, String msg){
        if(LOG) Log.i(tag, sanitizeNull(msg));
    }

    public static void w(String tag, String msg){
        if(LOG) Log.w(tag, sanitizeNull(msg));
    }
}
