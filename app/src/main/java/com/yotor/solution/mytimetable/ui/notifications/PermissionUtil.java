package com.yotor.solution.mytimetable.ui.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

public class PermissionUtil {

    public static boolean isIgnoringBattryOptimazation(Context ctx) {
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm != null)
                return pm.isIgnoringBatteryOptimizations(ctx.getPackageName());
            return false;
        }
        return true;
    }

    public static boolean isNotificationGranted(Context ctx) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (nm != null)
                return nm.isNotificationPolicyAccessGranted();
            return false;
        }
        return true;
    }
}
