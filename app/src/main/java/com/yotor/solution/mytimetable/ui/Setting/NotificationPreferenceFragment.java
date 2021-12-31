package com.yotor.solution.mytimetable.ui.Setting;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.notifications.Alarm;
import com.yotor.solution.mytimetable.ui.notifications.AlarmUtil;
@Keep
@KeepName
public class NotificationPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private Preference lessonNotification;
    private Preference taskNotification;
    private Preference examNotification;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.notification_preference, rootKey);
        lessonNotification = findPreference("lesson_notification_before");
        taskNotification = findPreference("task_notification_before");
        examNotification = findPreference("exam_notification_before");
        loadSummery();

    }
    private void loadSummery(){
        SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(getContext());
        lessonNotification.setSummary(String.format(getString(R.string.before_lesson), pref.getInt("lesson_notification_before",5)));
        taskNotification.setSummary(String.format(getString(R.string.before_task),pref.getInt("task_notification_before",1)));
        examNotification.setSummary(String.format(getString(R.string.before_exam),pref.getInt("exam_notification_before",7)));
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "lesson_notification_before":
                showPickerDialog("lesson_notification_before", getString(R.string.notify_me_before), getString(R.string._minute), 0, 120, 5, new Listener() {
                    @Override
                    public void onChanged(int newVal) {
                       AlarmUtil.updateAlarmList(getContext());

                    }
                });
                break;
            case "task_notification_before":
                showPickerDialog("task_notification_before", getString(R.string.notify_me_before), getString(R.string._day), 1, 30, 1, new Listener() {
                    @Override
                    public void onChanged(int newVal) {
                        AlarmUtil.updateAlarmList(getContext());
                    }
                });
                break;
            case "exam_notification_before":
                showPickerDialog("exam_notification_before", getString(R.string.notify_me_before), getString(R.string._day), 1, 30, 7, new Listener() {
                    @Override
                    public void onChanged(int newVal) {
                        AlarmUtil.updateAlarmList(getContext());

                    }
                });
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void showPickerDialog(final String key, String title, final String formatter, int min, int max, int def, final Listener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        final NumberPicker picker = new NumberPicker(getContext());
        picker.setMinValue(min);
        picker.setMaxValue(max);
        picker.setValue(getPreferenceScreen().getSharedPreferences().getInt(key,def));
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(formatter, i);
            }
        });
        builder.setView(picker);
        builder.setNegativeButton(getString(R.string.cancel),null);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPreferenceScreen().getSharedPreferences().edit().putInt(key, picker.getValue()).apply();
                listener.onChanged(picker.getValue());
               //Analytics.logAppEvent(key, Util.intToStr(picker.getValue()));
                loadSummery();
            }
        });
        builder.show();
        picker.getValue();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        AlarmUtil util = new AlarmUtil(getContext());
       //Analytics.logAppEvent(preference.getKey(), newValue.toString());
        switch (preference.getKey()){
            case "lesson_notification":
               //Analytics.logAppEvent(preference.getKey(), newValue.toString());
                if ((boolean)newValue)
                    util.stopLessonAlarm();
                else
                    util.scheduleLessonAlarmList(true);
                break;
            case "task_notification":
                if ((boolean)newValue)
                    util.stopExamTaskAlarm(Alarm.TYPE_TASK);
                else
                    util.scheduleExamTaskAlarmList(true);
                break;
            case "exam_notification":
                if ((boolean)newValue)
                    util.stopExamTaskAlarm(Alarm.TYPE_EXAM);
                else
                    util.scheduleExamTaskAlarmList(true);
                break;
            case "push_notification":
                break;
        }
        return true;
    }

    interface Listener {
        void onChanged(int newVal);
    }
}

