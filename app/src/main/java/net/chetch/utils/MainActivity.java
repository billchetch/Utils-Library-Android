package net.chetch.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.TypeAdapterFactory;

import net.chetch.utilities.DelegateTypeAdapter;
import net.chetch.utilities.DelegateTypeAdapterFactory;
import net.chetch.utilities.EnumTypeAdapater;
import net.chetch.utilities.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static public class StringTypeAdapater extends DelegateTypeAdapter<String>{

        public String s = "hey " + Calendar.getInstance().getTimeInMillis();

        @Override
        public boolean isAdapterForType(Type t) {
            return t.equals(String.class);
        }

        @Override
        public DelegateTypeAdapter<String> useInstance() {
            StringTypeAdapater ta = (StringTypeAdapater)create();
            ta.s = s;
            return ta;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal1 = Calendar.getInstance();
        cal1 = Utils.calendarSetHour(cal1, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, 1);
        cal2 = Utils.calendarSetHour(cal2, 0);

        boolean b1 = Utils.isToday(cal1);
        boolean b2 = Utils.isToday(cal2);

        Log.i("MAIN", "Ended on create");
    }
}
