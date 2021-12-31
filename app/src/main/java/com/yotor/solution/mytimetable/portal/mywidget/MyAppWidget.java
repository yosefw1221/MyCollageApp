package com.yotor.solution.mytimetable.portal.mywidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.MainActivity;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ExamTaskActivity;
import com.yotor.solution.mytimetable.ui.notifications.AlarmUtil;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAppWidget extends AppWidgetProvider {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
    static int type = 0;
    static int date = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    static String string = dateFormat.format(Calendar.getInstance().getTime());
    static int nextBtnResId = R.drawable.ic_arrow_forward_white_48dp;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(context, WidgetRemoteViewService.class);
        intent.putExtra("date", date);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        views.setInt(R.id.widget_next_btn, "setBackgroundResource", nextBtnResId);
        views.setTextViewText(R.id.widget_date_tv, string);
        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);
        views.setOnClickPendingIntent(R.id.widget_next_btn, getPendingSelfIntent(context, "update"));
        views.setOnClickPendingIntent(R.id.widget_change_btn, getPendingSelfIntent(context, "change"));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, MyAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Intent examTaskIntent = new Intent(context, ExamTaskActivity.class);
        Intent timetableIntent = new Intent(context, MainActivity.class).putExtra("tab","1");
        PendingIntent examPendingIntent = PendingIntent.getActivity(context,23,examTaskIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent timetablePendingIntent = PendingIntent.getActivity(context,321,timetableIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent intent = new Intent(context, WidgetRemoteViewService.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
            views.setRemoteAdapter(R.id.widget_list_view, intent);
            views.setTextViewText(R.id.widget_date_tv, string);
            views.setInt(R.id.widget_next_btn, "setBackgroundResource", nextBtnResId);
            views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);
            if (type==0) {
               //Analytics.logAppEvent("App_Widget", "Type Lesson");
                views.setViewVisibility(R.id.widget_next_btn, View.VISIBLE);
                views.setOnClickPendingIntent(R.id.widget_date_tv, timetablePendingIntent);
                views.setPendingIntentTemplate(R.id.widget_list_view, timetablePendingIntent);
            }else{
               //Analytics.logAppEvent("App_Widget", "Type ExamTask");
                views.setViewVisibility(R.id.widget_next_btn, View.GONE);
                views.setOnClickPendingIntent(R.id.widget_date_tv, examPendingIntent);
                views.setPendingIntentTemplate(R.id.widget_list_view, examPendingIntent);
            }
            views.setOnClickPendingIntent(R.id.widget_next_btn, getPendingSelfIntent(context, "next"));
            views.setOnClickPendingIntent(R.id.widget_change_btn, getPendingSelfIntent(context, "change"));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
       //Analytics.logAppEvent("App_Widget", "onDeleted");
    }

    @Override
    public void onEnabled(Context context) {
       //Analytics.logAppEvent("App_Widget", "onEnabled");
        // Enter relevant functionality for when the first widget is created
        Integer[] days = AlarmUtil.sortedEnabledDay(context).toArray(new Integer[]{});
        if (days.length > 1)
            date = (date == days[0]) ? days[1] : days[0];
        else date = days[0];
        if (type==1)string = context.getString(R.string.exam_task);
        else string = dateFormat.format(getDate(date));
        nextBtnResId = R.drawable.ic_arrow_forward_white_48dp;

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
       //Analytics.logAppEvent("App_Widget", "onDisabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null)
        if (intent.getAction().equals("next"))
            onUpdate(context,0);
        else if (intent.getAction().equals("change"))
            onUpdate(context,1);
        super.onReceive(context, intent);
    }

    private void onUpdate(Context context, int i) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context.getPackageName(), getClass().getName());

        int[] ids = manager.getAppWidgetIds(componentName);
        if (i==0){
            Integer[] days = AlarmUtil.sortedEnabledDay(context).toArray(new Integer[]{});
            if (days.length > 1)
                date = (date == days[0]) ? days[1] : days[0];
            else date = days[0];
            if (date==days[0])
                nextBtnResId = R.drawable.ic_arrow_forward_white_48dp;
            else nextBtnResId = R.drawable.ic_arrow_back_white_36dp;
            string = dateFormat.format(getDate(date));
            type = 0;
        }else {
            type=type==0?1:0;
            if (type==1)string = context.getString(R.string.exam_task);
            else string = dateFormat.format(getDate(date));

        }
        onUpdate(context, manager, ids);
    }private Date getDate(int day){
        int difference = day - Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int extra = (difference) >= 0 ? (difference) : (7 + difference);
    return DateTime.now().plusDays(extra).toDateTime().toDate();
    }
}

