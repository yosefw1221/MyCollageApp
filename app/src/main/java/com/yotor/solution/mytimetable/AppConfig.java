package com.yotor.solution.mytimetable;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.home.recycler.ActionModel;
import com.yotor.solution.mytimetable.ui.home.recycler.RecyclerDataModel;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

public class AppConfig {
   // static FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    public static String getFBlink() {
        return "https://www.facebook.com/profile.php?id=100008174175280";//mFirebaseRemoteConfig.getString("about_fb");
    }

    public static String getTGlink() {
        return "http://t.me/josefworku";//mFirebaseRemoteConfig.getString("about_tg");
    }

    public static String getCallNo() {
        return "+251920005744";//mFirebaseRemoteConfig.getString("about_call");
    }

    public static String getEmail() {
        return "yosefworku18@gmail.com";//mFirebaseRemoteConfig.getString("about_email");
    }

    public static int getAdInterval() {
        return (int) 0;//mFirebaseRemoteConfig.getLong("ads_interval") * 1000;
    }

    public static boolean isNativeOnHome() {
        return false;//mFirebaseRemoteConfig.getBoolean("native_ads");
    }

    public static boolean isBannerOnMe() {
        return false;//mFirebaseRemoteConfig.getBoolean("ads_banner_on_me");
    }

    public static String getApplicationId() {
        return "";//mFirebaseRemoteConfig.getString("application_ids");
    }

//    public static boolean isBannerOnGpa() {
//        return mFirebaseRemoteConfig.getBoolean("ads_banner_on_gpa");
//    }
//
//    public static boolean isBannerOnHome() {
//        return mFirebaseRemoteConfig.getBoolean("ads_banner_on_home");
//    }

//    public static int getAdUpdate() {
//        return (int) mFirebaseRemoteConfig.getLong("ads_update") * 1000;
//    }

    public static boolean terminateApp() {
        return false;//mFirebaseRemoteConfig.getBoolean("terminate");
    }

    public static String terminateMsg() {
        return "";//mFirebaseRemoteConfig.getString("terminate_msg");
    }
//
//    public static int terminateAction() {
//        return (int) mFirebaseRemoteConfig.getLong("terminate_action");
//    }

    public static RecyclerDataModel getAppMessage(Context context) {
        RecyclerDataModel model = null;
        String json = null;//mFirebaseRemoteConfig.getString("message");
        if (json==null)return null;
        try {
            JSONObject object = new JSONObject(json);
            String id = object.getString("id");
            String title = object.getString("title");
            String msg = object.getString("msg");
            String url = object.getString("url");
            String action = object.getString("action");
            int expires = object.getInt("expire");
            boolean pin = object.getBoolean("pin");
            Log.e("MESSAGE DATA", json);
            Log.e("MESSAGE", String.format("id : %s\ntitle : %s\nmsg : %s\nUrl : %s\naction : %s\nexpire %d\npin %s", id
                    , title, msg, url, action, expires, Boolean.toString(pin)));
            if (id != null && !id.equalsIgnoreCase("0")) {
                long lastShow = ShardPref.getLong(context, id, 0);
                if (lastShow == 0) {
                    ShardPref.saveValue(context, id, System.currentTimeMillis());
                }
                DateTime expire = DateTime.now().withMillis(ShardPref.getLong(context, id, System.currentTimeMillis())).plusDays(expires);
                if (System.currentTimeMillis() < expire.getMillis()) {
                    model = new RecyclerDataModel().hintAction(title, msg, action, Util.getTintDrawable(context.getResources().getDrawable(R.drawable.messages), R.color.colorPrimary),
                            new ActionModel().actionViewUrl(url, pin));
                }
            }
        } catch (JSONException e) {
            Log.e("ERRRRRRRRRRROr", e.toString());
           //Crashlytics.logException(e);
        }
        return model;
    }

//    public static boolean showAdOnExit() {
//        return mFirebaseRemoteConfig.getBoolean("ad_on_exist");
//    }

//    void load(final Context ctx) {
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(mFirebaseRemoteConfig.getLong("min_fetch_interval"))
//                .build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_setting);
//        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
//            @Override
//            public void onComplete(@NonNull Task<Boolean> task) {
//            }
//        });
//
//    }
}
