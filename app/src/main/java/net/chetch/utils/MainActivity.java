package net.chetch.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.chetch.utilities.EnumTypeAdapater;
import net.chetch.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> urls = new ArrayList();
        urls.add("http://192.168.43.123:8004/api/resource/image/profile-pics/kumis.jpg");
        urls.add("http://192.168.43.123:8004/api/resource/image/profile-pics/wahyu.jpg");

        Utils.downloadImages(urls, bms ->{


            Log.i("Main", "downloaded " + bms.size());
        });
    }
}
