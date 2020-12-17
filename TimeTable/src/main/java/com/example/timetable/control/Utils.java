package com.example.timetable.control;

import android.content.Context;

public class Utils {
    public static int Dp2Px(Context context,float dp){
        return (int)(dp*context.getResources().getDisplayMetrics().density+0.5f);
    }
    public static int Px2Dp(Context context,float px){
        return (int)(px/context.getResources().getDisplayMetrics().density+0.5f);
    }
}
