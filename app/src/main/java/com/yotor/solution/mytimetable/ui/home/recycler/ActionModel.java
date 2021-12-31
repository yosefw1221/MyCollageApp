package com.yotor.solution.mytimetable.ui.home.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.portal.ShardPref;

public class ActionModel {
    public static final int TYPE_PREFERENCE = 11;
    public static final int TYPE_ACTION_VIEW = 13;
    public static final int TYPE_LAUNCH_ACTIVITY = 12;
    private int TYPE;
    private String key;
    private Object value;
    private Class activity;
    private String url;
    private boolean pin;

    public ActionModel() {

    }

    static void performAction(Context context, ActionModel actionModel) {
        if (actionModel == null) return;
        try {
            switch (actionModel.TYPE) {
                case TYPE_LAUNCH_ACTIVITY:
                    context.startActivity(new Intent(context, actionModel.activity));
                    return;
                case TYPE_PREFERENCE:
                    ShardPref.saveValue(context, actionModel.key, actionModel.value);
                    return;
                case TYPE_ACTION_VIEW:
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(actionModel.url));
                        context.startActivity(intent);
                    }catch (Exception e){
                       //Crashlytics.logException(e);
                    }
                    return;
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ActionModel actionChangePref(String key, Object value) {
        this.TYPE = TYPE_PREFERENCE;
        this.key = key;
        this.value = value;
        return this;
    }
    public ActionModel actionViewUrl(String url,boolean pin) {
        this.pin = pin;
        this.TYPE = TYPE_ACTION_VIEW;
        this.url = url;
        return this;
    }

    public ActionModel actionLaunchActivity(Class activity) {
        this.activity = activity;
        this.TYPE = TYPE_LAUNCH_ACTIVITY;
        return this;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }
}
