package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.model.db.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ChatMessageListConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<ChatMessage> stringToChatMessageList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ChatMessage>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<ChatMessage> someObjects) {
        return gson.toJson(someObjects);
    }
}
