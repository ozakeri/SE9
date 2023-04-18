package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.model.carinfo.Car;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CarListConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<Car> stringToCarList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Car>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Car> someObjects) {
        return gson.toJson(someObjects);
    }
}
