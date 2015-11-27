package com.github.benignbeppe.smashupcompanion;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by sebastian on 2015-11-27.
 */
public class AwesomeButton extends Button {
    public AwesomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Font.fontAwesome);
    }
}
