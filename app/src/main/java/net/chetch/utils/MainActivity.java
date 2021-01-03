package net.chetch.utils;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.chetch.utilities.DatePeriod;
import net.chetch.utilities.DelegateTypeAdapter;
import net.chetch.utilities.Utils;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;




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

    private void logDatePeriod(DatePeriod dp){
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Log.i("MAIN", "From: " + Utils.formatDate(dp.fromDate, dateFormat) + " To: " + Utils.formatDate(dp.toDate, dateFormat));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logDatePeriod(DatePeriod.getDayPeriod(0));
        logDatePeriod(DatePeriod.getDayPeriod(-1));

        Log.i("MAIN", "Ended on create");
    }
}
