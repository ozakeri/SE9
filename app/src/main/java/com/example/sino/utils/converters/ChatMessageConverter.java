package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.model.db.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ChatMessageConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static ChatMessage stringToChatMessage(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<ChatMessage>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(ChatMessage someObjects) {
        return gson.toJson(someObjects);
    }
}
