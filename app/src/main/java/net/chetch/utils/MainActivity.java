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
        public DelegateTypeAdapter<String> newInstance() {
            StringTypeAdapater ta = (StringTypeAdapater)create();
            ta.s = s;
            return ta;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DelegateTypeAdapterFactory f = new DelegateTypeAdapterFactory();
        f.addTypeAdapater(new StringTypeAdapater());

        for(DelegateTypeAdapter typeAdapter : f.typeAdapaters){
            try {
                //if(typeAdapter.isAdapterForType(type.getType())) {
                    //typeAdapter.setDelegate(delegate);
                    DelegateTypeAdapter ta = typeAdapter.newInstance();
                    ta.setDelegate(null);
                //}
            } catch (Exception e){
                Log.e("DTAF", e.getMessage());
            }
        }
    }
}
