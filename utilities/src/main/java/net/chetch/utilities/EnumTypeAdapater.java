package net.chetch.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EnumTypeAdapater<T extends Enum<T>> extends TypeAdapter<T> {

    private final Map<Integer, T> int2enum = new HashMap<>();

    private TypeAdapter<T> enumTypeAdapter;

    public EnumTypeAdapater(Class<T> cls){
        for (T constant : cls.getEnumConstants()) {
            int2enum.put(constant.ordinal(), constant);
        }
    }

    @Override
    public void write(JsonWriter out, T value)  throws IOException{
        out.value(value == null ? null : value.ordinal());
    }

    @Override
    public T read(JsonReader in) throws IOException{
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        return int2enum.get(in.nextInt());
    }
}
