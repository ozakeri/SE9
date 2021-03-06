package com.gap.sino.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AwesomeTextView extends TextView {
	public AwesomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AwesomeTextView(Context context) {
        super(context);
        init();
    }
 
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        setTypeface(tf ,1);

    }
}
