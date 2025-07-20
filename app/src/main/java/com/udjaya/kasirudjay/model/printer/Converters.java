package com.udjaya.kasirudjay.model.printer;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Integer> fromString(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Integer> list) {
        return gson.toJson(list);
    }
}
