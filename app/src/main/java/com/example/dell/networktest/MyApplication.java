package com.example.dell.networktest;

import android.app.Application;
import android.content.Context;

/**
 * Created by DELL on 2016/12/4.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public Context getContext(){
        return context;
    }
}
