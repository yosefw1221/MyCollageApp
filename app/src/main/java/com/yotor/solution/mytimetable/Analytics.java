package com.yotor.solution.mytimetable;

//import android.app.Activity;
//import android.os.Build;
//import android.os.Bundle;
//
//public class//Analytics {
//    public static void logAppEvent(String key, Bundle bundle) {
//        if (MyApp.analytics != null){
//            MyApp.analytics.setUserProperty("UserEvent",key);
//            MyApp.analytics.logEvent(key.replaceAll(" ","_"), bundle);
//    }}
//
//    public static void logClickEvent(String key, String data) {
//        if (MyApp.analytics == null) return;
//        Bundle bundle = new Bundle();
//        bundle.putString(key.replaceAll(" ","_"), data.replaceAll(" ","_"));
//        MyApp.analytics.setUserProperty("UserEvent","Click");
//        MyApp.analytics.logEvent("CLICK_EVENT", bundle);
//
//    }
//
//    public static void logAppEvent(String key, String data) {
//        if (MyApp.analytics == null) return;
//        Bundle bundle = new Bundle();
//        bundle.putString(key.replaceAll(" ","_"), data.replaceAll(" ","_"));
//        MyApp.analytics.setUserProperty(key,data);
//        MyApp.analytics.logEvent("APP_EVENT", bundle);
//
//    }
//
//    public static void logAds(String status) {
//        if (MyApp.analytics == null) return;
//        Bundle bundle = new Bundle();
//        bundle.putString("Manufacture", Build.MANUFACTURER);
//        bundle.putString("Brand", Build.BRAND);
//        bundle.putInt("SDK", Build.VERSION.SDK_INT);
//        bundle.putString("Status", status.replaceAll(" ","_"));
//        MyApp.analytics.setUserProperty("ADS",status);
//        MyApp.analytics.logEvent(status, bundle);
//        MyApp.analytics.logEvent("ADS_EVENT", bundle);
//    }
//    public static void trackScreen(Activity activity, String s) {
//        if (MyApp.analytics == null) return;
//        MyApp.analytics.setCurrentScreen(activity,"ScreenFragment", s.replaceAll(" ","_"));
//    }
//
//}
