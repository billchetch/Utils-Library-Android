package net.chetch.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.chetch.utilities.EnumTypeAdapater;


public class MainActivity extends AppCompatActivity {

    public enum EnumTest{
        ENUM_VAL1,
        ENUM_VAL2,
        ENUM_VAL3
    }


    public enum EnumTest2{
        ENUM2_VAL1,
        ENUM2_VAL2,
        ENUM2_VAL3
    }
    public class STest{
        public EnumTest EnumVal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        STest st = new STest();
        st.EnumVal = EnumTest.ENUM_VAL2;

        GsonBuilder builder = new GsonBuilder();
        EnumTypeAdapater<EnumTest> adapter = new EnumTypeAdapater(EnumTest.class);
        Gson gson = builder.registerTypeAdapter(EnumTest.class, adapter).create();
        //Gson gson = builder.create();

        String s = gson.toJson(st);
        STest st2 = gson.fromJson(s, STest.class);

        Log.i("T", gson.toJson(st));

    }
}
