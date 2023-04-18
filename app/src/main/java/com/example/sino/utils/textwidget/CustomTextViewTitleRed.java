package com.example.sino.utils.textwidget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.sino.R;

@SuppressLint("AppCompatCustomView")
public class CustomTextViewTitleRed extends TextView {
    private TextView textView;

    public CustomTextViewTitleRed(Context context) {
        super(context);
        applyCustomFont(context);
        singleLine(context);
        setTextSize(16);
        setTextColor(getResources().getColor(R.color.pino_two));
    }

    public CustomTextViewTitleRed(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        singleLine(context);
        setTextSize(16);
        setTextColor(getResources().getColor(R.color.pino_two));
    }

    public CustomTextViewTitleRed(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        singleLine(context);
        setTextSize(16);
        setTextColor(getResources().getColor(R.color.pino_two));
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("IRANSans(FaNum)_Light.ttf", context);
        setTypeface(customFont,Typeface.BOLD);

    }

    private void singleLine(Context context) {
        textView = new TextView(context);
        textView.setSingleLine(false);
    }
}
