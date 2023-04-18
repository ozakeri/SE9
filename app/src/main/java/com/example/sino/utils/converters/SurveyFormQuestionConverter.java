package com.example.sino.utils.converters;

import androidx.room.TypeConverter;

import com.example.sino.db.entity.form.FormItemAnswer;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class SurveyFormQuestionConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<SurveyFormQuestion> stringToSurveyFormQuestionList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SurveyFormQuestion>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<SurveyFormQuestion> someObjects) {
        return gson.toJson(someObjects);
    }
}
