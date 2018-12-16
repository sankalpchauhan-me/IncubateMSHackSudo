package com.sudo.campusambassdor;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();


        ApplicationHelper.initDatabaseHelper(this);
    }
}
