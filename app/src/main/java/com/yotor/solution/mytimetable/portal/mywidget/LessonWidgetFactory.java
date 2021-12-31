package com.yotor.solution.mytimetable.portal.mywidget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LessonWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<WidgetDataModel> dataList;
    private Context ctx;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    LessonWidgetFactory(Context context) {
        ctx = context;
        if (MyAppWidget.type == 0)
            dataList = new WidgetData(ctx).getLessons(MyAppWidget.date);
        else dataList = new WidgetData(ctx).getExamTaskList();
    }

    @Override
    public void onCreate() {
       //Analytics.logAppEvent("App Widget", "onCreate");
    }

    @Override
    public void onDataSetChanged() {
        //Log.e("RemoteViewsFactory ", "onDataSetChanged " + MyAppWidget.date);
        if (MyAppWidget.type == 0)
            dataList = new WidgetData(ctx).getLessons(MyAppWidget.date);
        else dataList = new WidgetData(ctx).getExamTaskList();
    }

    @Override
    public void onDestroy() {
       //Analytics.logAppEvent("App Widget", "onDestroy");
        //Log.e("RemoteViewsFactory ", "onDestroy");
    }

    @Override
    public int getCount() {
        //Log.e("RemoteViewsFactory ", "getCount " + dataList.size());
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        //Log.e("RemoteViewsFactory ", "getViewAt " + i);
        if (i == AdapterView.INVALID_POSITION || dataList.size() == 0)
            return null;
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.widget_lesson_item);
        Intent intent = new Intent().putExtra("pos", i);
        remoteViews.setOnClickFillInIntent(R.id.widget_item_parent, intent);
        if (MyAppWidget.type == 0) {
            remoteViews.setTextViewText(R.id.widget_item_title, dataList.get(i).getCourseName());
            String time = timeFormat.format(dataList.get(i).getStartTime().toDate()) + "\n-\n" + timeFormat.format(dataList.get(i).getEndTime().toDate());
            remoteViews.setViewVisibility(R.id.widget_item_subtitle, View.GONE);
            if (System.currentTimeMillis() >= dataList.get(i).getStartTime().getMillis() && System.currentTimeMillis() <= dataList.get(i).getEndTime().getMillis()) {
                remoteViews.setInt(R.id.widget_item_parent, "setBackgroundResource",R.drawable.widget_item_current);
            }
            remoteViews.setTextViewText(R.id.widget_item_date, time);
        } else {
            remoteViews.setTextViewText(R.id.widget_item_title, dataList.get(i).getCourseName());
            long dayleft = dataList.get(i).getStartTime().getMillis() - System.currentTimeMillis();
            int day = DateTime.now().withMillis(dayleft).getDayOfYear()-1;
            remoteViews.setViewVisibility(R.id.widget_item_subtitle, View.VISIBLE);
            remoteViews.setTextViewText(R.id.widget_item_date, +day + ctx.getString(R.string.days_left));
            remoteViews.setTextViewText(R.id.widget_item_subtitle, dataList.get(i).getSubTitle());
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
      //  Log.e("RemoteViewsFactory ", "getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
      //  Log.e("RemoteViewsFactory ", "getViewTypeCount");
        return 2;
    }

    @Override
    public long getItemId(int i) {
      //  Log.e("RemoteViewsFactory ", "getItemId " + i);
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
