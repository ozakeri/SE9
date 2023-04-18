package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.db.entity.form.FormItemAnswer;
import com.example.sino.db.entity.form.FormQuestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class FormQuestionConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<FormQuestion> stringToFormQuestionList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<FormQuestion>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<FormQuestion> someObjects) {
        return gson.toJson(someObjects);
    }
}
