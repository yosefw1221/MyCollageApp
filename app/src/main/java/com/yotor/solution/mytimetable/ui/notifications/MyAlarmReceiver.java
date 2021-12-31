package com.yotor.solution.mytimetable.ui.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_LESSON = "lessonChannel";
    private static final String CHANNEL_EXAM = "examChannel";
    private static final String CHANNEL_TASK = "taskChannel";
    private static final int NOTIFICATION_ID_LESSON = 1;
    private static final long[] VIBRATE = {50, 200, 50, 200};

    public static void showLessonNotification(Context ctx, Bundle bundle) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String course = "null";
        int color = 2;
        int lesson = 1;
        if (bundle != null) {
            course = bundle.getString("course");
            color = bundle.getInt("color");
            lesson = bundle.getInt("lesson");
        }
        assert bundle != null;
        String time = dateFormat.format(new DateTime().withMillis(bundle.getLong("date")).toDate());
        Log.e("ALARM LESSON", "course " + course + "  color " + color + " lesson " + lesson);
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_LESSON);
        builder.setTicker(String.format(ctx.getString(R.string.lesson_notif_ticker_txt), course, time));
        builder.setContentTitle(String.format("%S", course));
        builder.setContentText(String.format(ctx.getString(R.string.lesson_notif_ticker_txt), course, time));
        builder.setColorized(true);
        builder.setVibrate(VIBRATE);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);
        builder.setTimeoutAfter(ShardPref.getInt(ctx, "lesson_duration", 60) * 60000);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        builder.setSmallIcon(R.drawable.course_code_18dp);
        Drawable d = Util.getTintDrawable(ctx.getResources().getDrawable(R.drawable.course_code_18dp), color);
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        builder.setLargeIcon(bitmap);
        if (manager != null)
            manager.notify(NOTIFICATION_ID_LESSON, builder.build());
    }

    public static void showExamNotification(Context ctx, int type, Bundle bundle) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE MMMMM dd", Locale.getDefault());
        String course = bundle.getString("course") == null ? "-" : bundle.getString("course");
        int color = bundle.getInt("color");
        String topic = bundle.getString("topic");
        String note = bundle.getString("note");
        String time = dateFormat.format(new DateTime().withMillis(bundle.getLong("date")));
        Log.e(type == 1 ? "EXAM NOTIFICATION " : "TASK NOTIFICATION  ", " course " + course + "  color " + color + " lesson " +
                time + " topic " + topic + " note : " + note);
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, type == 1 ? CHANNEL_EXAM : CHANNEL_TASK);
        String exam = String.format(ctx.getString(R.string.exam_notification_title), course == null ? "" : course, time);
        String task = String.format(ctx.getString(R.string.task_notification_title), course == null ? "" : course, time);
        builder.setTicker(type == 2 ? task : exam);
        builder.setContentTitle(String.format("%S", topic == null ? type == 1 ? course + ctx.getString(R.string._exam_) : course + ctx.getString(R.string._task_) : topic));
        builder.setSubText(note);
        builder.setContentText(type == 2 ? task : exam);
        builder.setColorized(true);
        builder.setVibrate(VIBRATE);
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        builder.setSmallIcon(R.drawable.exam);
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);
        Drawable d = Util.getTintDrawable(ctx.getResources().getDrawable(R.drawable.exam), color);
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        builder.setLargeIcon(bitmap);
        builder.setLargeIcon(bitmap);
        if (manager != null)
            manager.notify(type, builder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive : ", new DateTime().withMillis(System.currentTimeMillis()).toString());
        if (intent.getAction() != null && intent.getAction().equals(AlarmUtil.ALARM_ACTION)) {
            Bundle bundle = intent.getBundleExtra("data");
            int type = intent.getIntExtra("type", -1);
            switch (type) {
                case 0:
                    showLessonNotification(context, bundle);
                    break;
                case 1:
                case 2:
                    showExamNotification(context, type, bundle);
                    break;
                case 3:
                    muteDevice(context, true);
                    break;
                case 4:
                    muteDevice(context, false);
                    break;
                default:
                    Log.e("INVALID ALARM TYPE", " " + type);
            }
        } else {
            Log.e("ALARM BROADCAST RECEVER", "Action " + intent.getAction());
            AlarmUtil.updateAlarmList(context);
        }


    }

    private void muteDevice(Context context, boolean b) {
        boolean hasAccess = PermissionUtil.isNotificationGranted(context);
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("automate", true);
        if (!enabled) return;
        String mode = PreferenceManager.getDefaultSharedPreferences(context).getString("automate_mode", "0");
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Toast.makeText(context, "mute device " + b + " access " + hasAccess, Toast.LENGTH_SHORT).show();
        try {
            if (manager != null)
                if (b) {
                    ShardPref.saveValue(context, "music", manager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    if (hasAccess)
                        manager.setRingerMode(mode.equals("0") ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);
                    else
                        manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.ADJUST_SAME);
                } else {
                    if (mode.equals("1")) {
                        changeInterruptionFilter(context, 1);
                    }
                    int music = ShardPref.getInt(context, "music", 3);
                    manager.setStreamVolume(AudioManager.STREAM_MUSIC, music, AudioManager.ADJUST_SAME);
                }
        } catch (Exception e) {
           //Crashlytics.log("Mute Device Access +" + hasAccess + " " + e.toString());
        }
    }

    public boolean changeInterruptionFilter(Context context, int InterceptionFilter) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (manager != null)
                manager.setInterruptionFilter(InterceptionFilter);
            return true;
        }
        return false;

    }
}
