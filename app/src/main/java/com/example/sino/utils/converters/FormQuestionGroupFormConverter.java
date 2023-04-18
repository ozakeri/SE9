package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.db.entity.form.FormQuestionGroupForm;
import com.example.sino.db.entity.form.FormTemp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class FormQuestionGroupFormConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<FormQuestionGroupForm> stringToFormQuestionGroupFormList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<FormQuestionGroupForm>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<FormQuestionGroupForm> someObjects) {
        return gson.toJson(someObjects);
    }
}
