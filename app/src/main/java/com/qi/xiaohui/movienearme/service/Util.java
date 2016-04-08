package com.qi.xiaohui.movienearme.service;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by TQi on 4/8/16.
 */
public class Util {
    public static int[] getScreenSize(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int [] dimen = new int[2];
        dimen[0] = display.getWidth();
        dimen[1] = display.getHeight();
        return dimen;
    }
}
