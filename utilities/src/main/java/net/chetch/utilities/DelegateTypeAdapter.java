package net.chetch.utilities;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

abstract public class DelegateTypeAdapter<T> extends TypeAdapter<T>{

    protected TypeAdapter<T> delegate;

    abstract public boolean isAdapterForType(Type t);

    public void setDelegate(TypeAdapter<T> delegate){
        this.delegate = delegate;
    }

    public void write(JsonWriter out, T value) throws IOException {
        delegate.write(out, value);
    }

    public T read(JsonReader in) throws IOException {
        return delegate.read(in);
    }

    //helper method when writing new instance
    protected DelegateTypeAdapter<T> create() {
        try {
            DelegateTypeAdapter<T> ta = getClass().getConstructor().newInstance();
            ta.setDelegate(delegate);
            return ta;
        } catch (Exception e){
            Log.e("DTA", e.getMessage());
            return null;
        }
    }

    //override this to create an instance for delegate type adapter factory
    abstract public DelegateTypeAdapter<T> newInstance();
}
