package io.github.skywalkerdarren.simpleaccounting.util;

import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        }
        if(value){
            out.value(1);
        }else{
            out.value(0);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();

        switch (peek) {
            case BOOLEAN:
                return in.nextBoolean();//如果为true则返回为int的1，false返回0.
            case NULL:
                in.nextNull();
                return null;
            case NUMBER:
                return in.nextInt()==1;
            case STRING:
                String bool = in.nextString();
//                Log.d("BooleanTypeAdapter","msg="+bool);
                return toBoolean(bool);
            default:
                throw new JsonParseException("Expected NUMBER but was " + peek);
        }

    }

    private Boolean toBoolean(String nextString) {
        if(nextString.matches("\\d")){
            return Integer.valueOf(nextString) == 1;
        }
        if("true".equals(nextString) || "false".equals(nextString)){
            return Boolean.valueOf(nextString);
        }
        return null;
    }
}
