package com.yotor.solution.mytimetable.ui.Setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

interface LtimeListener {
    void onValidTime(int val);
}

public class LessonTimeHelper {
    private Context ctx;
    private List<LessonTime> lstList = new LinkedList<>();
    private SharedPreferences shardPref;
    private int[] defaultLST = {120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720, 780, 840, 900, 960};

    public LessonTimeHelper(Context ctx) {
        shardPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.ctx = ctx;
        loadLSTimes();
    }


    public static int getTotalLesson(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt("total_lesson", 15);
    }

    public static int getLESSON_DURATION(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt("lesson_duration", 60);
    }
    public static List<Integer> getEnabledDaylist(Context ctx){
        String[] data = getEnabledDay(ctx);
        List<Integer> list = new LinkedList<>();
        for (String s : data) {
            list.add(Util.strToInt(s));
        }
    return list;
    }

    public static String[] getEnabledDay(Context ctx) {
        Set<String> defaultDay = new HashSet<>(Arrays.asList(ctx.getResources().getStringArray(R.array.lesson_default_value)));
        Set<String> enabled_day = PreferenceManager.getDefaultSharedPreferences(ctx).getStringSet("enabled_day", defaultDay);
        String[] days = enabled_day.toArray(new String[]{});
        Arrays.sort(days);
        return days;
    }

    public int getLessonGapMinute(int lesson_index) {
        if (lesson_index <= 0)
            return 0;
        LessonTime prevlessonTime = getLessonTime(lesson_index);
        LessonTime nowlessonTime = getLessonTime(lesson_index + 1);
        int prevLST = prevlessonTime.getTimeInt();
        int thisLST = nowlessonTime.getTimeInt();
        int LessonLength = getLESSON_DURATION(ctx);
        return (thisLST - (prevLST + LessonLength));
    }

    List<LessonTime> loadLSTimes() {
        shardPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        LessonTime lessonTime;
        lstList = new LinkedList<>();
        int MAX_LESSON = 15;
        for (int i = 0; i < MAX_LESSON; i++) {
            lessonTime = new LessonTime((i + 1), shardPref.getInt("lst_" + (i + 1), defaultLST[i]));
            lstList.add(lessonTime);
        }
        return lstList;
    }

    public LessonTime getLessonTime(int lesson) {
        return lstList.get(lesson - 1);
    }

    private void changeLst(String key, int timeInt) {
        shardPref.edit().putInt(key, timeInt).apply();
    }

    public void onChangeLessonDuration(int newVal, LtimeListener listener) {
        DateTime now = DateTime.now();
        int Gap = ShardPref.getInt(ctx, "lesson_break", 0);
        LessonTime fltime = getLessonTime(1);
        int valid = 0;
        for (int i = 1; i < lstList.size(); i++) {
            DateTime dateTime = fltime.getDateTime().plusMinutes(newVal * i + (Gap * i)).toDateTime();
            if (now.getDayOfMonth() == dateTime.getDayOfMonth()) {
                valid = i;
                changeLst("lst_" + (i + 1), dateTime.toDateTime().getMinuteOfDay());
            } else
                break;
            Log.e("onChangeLessonDuration " + i, "\nprev Time : " + lstList.get(i).getTimeInt() + " new Time : " + dateTime.getMinuteOfDay());
        }
        listener.onValidTime(valid+1);
        loadLSTimes();
    }


    void onChangeLessonGap(int prev, int newVal, LtimeListener listener) {
        int newGap = newVal - prev;
        DateTime now = DateTime.now();
        int valid = 0;
        for (int i = 1; i < lstList.size(); i++) {
            DateTime dateTime = getLessonTime(i + 1).getDateTime().plusMinutes(newGap * i).toDateTime();
            if (now.getDayOfMonth() == dateTime.getDayOfMonth()) {
                valid = i;
                changeLst("lst_" + (i + 1), dateTime.toDateTime().getMinuteOfDay());
            } else
                break;
            Log.e("onChangeLessonGap " + i, "\nprev Time : " + lstList.get(i).getTimeInt() + " new Time : " + lstList.get(i).getTimeInt() + (newGap * i));
            Log.e("onChangeLessonDuration " + i, "\nprev Time : " + lstList.get(i).getTimeInt() + " new Time : " + dateTime.getMinuteOfDay());
        }
        // for (int i = 1; i < lstList.size(); i++) {
        //  changeLst("lst_" + (i + 1), (lstList.get(i).getTimeInt() + (newGap * i)));
        // Log.e("onChangeLessonGap " + i, "\nprev Time : " + lstList.get(i).getTimeInt() + " new Time : " + lstList.get(i).getTimeInt() + (newGap * i));
        listener.onValidTime(valid+1);
        loadLSTimes();
    }

     public void changeLessonStartTime(int lesson, int h, int m, LtimeListener listener) {
        DateTime newTime = new DateTime().withTime(h, m, 0, 0);
        Log.e("changeLessonStartTime "+lesson,"h : "+h+" m : "+m);
        if (lesson > 1) {
            int prevtimeInt = getLessonTime(lesson - 1).getDateTime().plusMinutes(getLESSON_DURATION(ctx)).getMinuteOfDay();
            Log.e("##################"," prev "+prevtimeInt+" new time "+newTime.getMinuteOfDay());
            if (prevtimeInt > newTime.getMinuteOfDay()) {
                Toast.makeText(ctx, " Invalid time \ntime will overlap to previous lesson.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e("changeLessonStartTime "+lesson,"h : "+h+" m : "+m+"\n valid to change");
        }
        if (newTime.getMinuteOfDay()==getLessonTime(lesson).getDateTime().getMinuteOfDay()){
            Log.e("changeLessonStartTime "+lesson,"h : "+h+" m : "+m+"\n no change Ignore");
            return;
        }
        int valid = 1;
        Log.e("changeLessonStartTime "+lesson,"h : "+h+" m : "+m+"\n try to change");
        DateTime now = DateTime.now();
        int Gap = ShardPref.getInt(ctx, "lesson_break", 0);
        int Duration = getLESSON_DURATION(ctx);
        for (int i = lesson - 1, x = 0; i < lstList.size(); x++, i++) {
            DateTime dateTime = newTime.plusMinutes(Duration * x + (Gap * x)).toDateTime();
            if (now.getDayOfMonth() == dateTime.getDayOfMonth()) {
                valid = i;
                changeLst("lst_" + (i + 1), dateTime.toDateTime().getMinuteOfDay());
                Log.e("changeLessonStartTime "+i," Changed to "+dateTime.toDateTime().getMinuteOfDay());
            } else{
                Log.e("changeLessonStartTime "+lesson," Invalid time existing");
                break;
            }

        }
        Log.e("changeLessonStartTime "+lesson," Finished with valid lessons "+(valid+1));
        if (listener!=null)
        listener.onValidTime(valid+1);
    loadLSTimes();
}

public class LessonTime {
    private int lesson;
    private int timeInt;

    LessonTime(int lesson, int timeInt) {
        this.lesson = lesson;
        this.timeInt = timeInt;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getTimeInt() {
        return timeInt;
    }

    public DateTime getDateTime() {
        return new DateTime().withTime(timeInt / 60, timeInt % 60, 0, 0);
    }

    public DateTime getLessonStartTime(int nullableDay) {
        DateTime time = getDateTime();
        if (nullableDay != 0) {
            int difference = nullableDay - Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int extra = (difference) >= 0 ? (difference) : (7 + difference);
            return getDateTime().plusDays(extra).toDateTime();
        }
        return time;
    }

    public String getLstTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(getLessonStartTime(0).toDate());
    }
}
}
