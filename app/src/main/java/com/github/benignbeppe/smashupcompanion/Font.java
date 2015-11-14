package com.github.benignbeppe.smashupcompanion;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by sebastian on 2015-11-14.
 */
public class Font {
    public static Typeface fontAwesome;

    public static void init(Context context) {
        fontAwesome = Typeface.createFromAsset(context.getAssets(),
                "fonts/fontawesome-webfont.ttf");
    }
}
