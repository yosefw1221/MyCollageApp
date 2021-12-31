package com.yotor.solution.mytimetable;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
//
////import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.notifications.AlarmUtil;

public class MyApp extends Application {
    public static FirebaseAnalytics analytics;

    public FirebaseAnalytics getAnalytics() {
        return analytics == null ? FirebaseAnalytics.getInstance(getApplicationContext()) : analytics;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        new AppConfig().load(getApplicationContext());
//        if (!AppConfig.terminateApp() && AppConfig.terminateAction() != -1) {
//            try {
//               //MobileAds.initialize(this, AppConfig.getApplicationId());
//                AlarmUtil.updateAlarmList(getApplicationContext());
//                analytics = FirebaseAnalytics.getInstance(getApplicationContext());
//                AppCompatDelegate.setDefaultNightMode(ShardPref.getInt(this, "nightMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
//                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//                Util.loadLanguage(this, null);
//            } catch (Exception e) {
//                //Crashlytics.logException(e);
//            }
//        } else if (AppConfig.terminateAction() == -1) System.exit(0);
    }
}