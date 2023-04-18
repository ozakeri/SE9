package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AppUserConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static AppUser stringToChatMessage(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<AppUser>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(AppUser someObjects) {
        return gson.toJson(someObjects);
    }
}
