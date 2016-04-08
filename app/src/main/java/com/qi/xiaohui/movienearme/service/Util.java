package com.qi.xiaohui.movienearme.service;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TQi on 4/8/16.
 */
public class Util {
    private static Map<String, String> locale;

    public static int[] getScreenSize(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int [] dimen = new int[2];
        dimen[0] = display.getWidth();
        dimen[1] = display.getHeight();
        return dimen;
    }

    public static String lanLocale(String acronyms){
        if(locale == null){
            locale = new HashMap<>();
            locale.put("zh", "Chinese");
            locale.put("en", "English");
            locale.put("fr", "French");
            locale.put("de", "Germony");
            locale.put("it", "Italian");
            locale.put("ja", "Japinese");
            locale.put("ko", "Korean");
        }
        if(locale.get(acronyms) != null){
            return locale.get(acronyms);
        }else{
            return "English";
        }
    }
}
