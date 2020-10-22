package com.example.tikicloneapp;

import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClass {

    public static void callPanel(final LinearLayout panel, int millisecondsDelay) {
        panel.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                panel.setVisibility(View.GONE);
            }
        }, millisecondsDelay);
    }

    public static void setTextView_StrikeThrough(TextView textView) {
        //set throughStrike
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        return format.format(date);
    }

    public static String convertDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public static String getTextAddress(String province, String district, String ward, String numberAddress) {
        return numberAddress + ", " + ward + ", " + district + ", " + province;
    }
    public static String getTextAddress(String province, String district, String ward) {
        return  ward + ", " + district + ", " + province;
    }
}
