package com.example.sino.utils;

public enum SurveyFormStatusEn {
    New(0), Incomplete(1), Complete(2);

    private int code;

    SurveyFormStatusEn(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
