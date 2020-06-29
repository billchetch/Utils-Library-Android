package net.chetch.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DelegateTypeAdapterFactory implements TypeAdapterFactory {

    public ArrayList<DelegateTypeAdapter> typeAdapaters = new ArrayList<>();

    public void clearTypeAdapaters(){
        typeAdapaters.clear();
    }

    public void addTypeAdapater(DelegateTypeAdapter typeAdapter){
        typeAdapaters.add(typeAdapter);
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        for(DelegateTypeAdapter typeAdapter : typeAdapaters){
           try {
               if(typeAdapter.isAdapterForType(type.getType())) {
                   typeAdapter.setDelegate(delegate);
                   return typeAdapter.newInstance();
               }
           } catch (Exception e){
                Log.e("DTAF", e.getMessage());
           }
        }

        return delegate;
    }

}
