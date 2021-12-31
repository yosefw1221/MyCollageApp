package com.yotor.solution.mytimetable.ui.Setting;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

interface Listener {
    void onChanged();
}
@Keep
@KeepName
public class TimetablePreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    MultiSelectListPreference lessonDays;
    Preference lessonPerDay;
    Preference lessonDuration;
    Preference lessonBreak;
    HashMap<String, Preference> startTime;
    LessonTimeHelper LTHelper;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.timetable_preference, rootKey);
        LTHelper = new LessonTimeHelper(getContext());
        lessonDays = findPreference("enabled_day");
        if (lessonDays != null)
            lessonDays.setOnPreferenceChangeListener(this);
        lessonPerDay = findPreference("total_lesson");
        lessonDuration = findPreference("lesson_duration");
        lessonBreak = findPreference("lesson_break");
        int maxLesson = 15;
        startTime = new HashMap<>(maxLesson);
        for (int i = 0; i < maxLesson; i++) {
            startTime.put("lst_" + (i + 1), findPreference("lst_" + (i + 1)));
        }
        loadLessonDaySummery(lessonDays.getValues());
        loadLstSummery();
        loadSummery();
    }

    public void loadSummery() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        totalLessonChangeListener(pref.getInt("total_lesson", 15));

        lessonPerDay.setSummary(String.format(getString(R.string._lessons),pref.getInt("total_lesson", 15)));
        lessonDuration.setSummary(String.format(getString(R.string._minutes),pref.getInt("lesson_duration", 60) ));
        lessonBreak.setSummary(String.format(getString(R.string._minutes),pref.getInt("lesson_break", 0)));
    }

    private void loadLessonDaySummery(Set<String> val) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        String[] s = val.toArray(new String[]{});
        Arrays.sort(s);
        for (String entry : s) {
            calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(entry));
            builder.append(", ").append(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        }
        lessonDays.setSummary(builder.toString().replaceFirst(", ", ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "enabled_day":
               //Analytics.logAppEvent(preference.getKey(),Util.ArrayToString(((Set<String>)newValue).toArray(new String[]{})));
                if (((Set<String>)newValue).size()==0){
                    Toast.makeText(getContext(), "Select at list one day", Toast.LENGTH_SHORT).show();
                    return false;
                }
                loadLessonDaySummery((Set<String>) newValue);

        }
        return true;
    }
    private void onValidTimeChanged(int val) {
        ShardPref.saveValue(getContext(),"valid_lesson",val);
        int tl = ShardPref.getInt(getContext(),"total_lesson",15);
        if (tl>val) {
            ShardPref.saveValue(getContext(), "total_lesson", val);
            totalLessonChangeListener(val);
        }
    }

    private void totalLessonChangeListener(int total) {
        enableStartTime(total);
    }

    private void enableStartTime(int total) {

        for (int i = 0; i < startTime.size(); i++) {
            if (i < total)
                startTime.get("lst_" + (i + 1)).setVisible(true);
            else
                startTime.get("lst_" + (i + 1)).setVisible(false);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "total_lesson":
                int max = ShardPref.getInt(getContext(),"valid_lesson",15);
                showPickerDialog("total_lesson", getString(R.string.total_lesson_title), getString(R.string._lesson), 1, max, max, new Listener() {
                    @Override
                    public void onChanged() {
                        totalLessonChangeListener(ShardPref.getInt(getContext(), "total_lesson", 15));
                        loadSummery();
                    }
                });
                break;
            case "lesson_duration":
                final int prev = getPreferenceScreen().getSharedPreferences().getInt("lesson_duration", 60);
                showPickerDialog("lesson_duration", getString(R.string.lesson_length_title), getString(R.string._minutes), 30, 300, 60, new Listener() {
                    @Override
                    public void onChanged() {
                        LTHelper.onChangeLessonDuration(getPreferenceScreen().getSharedPreferences().getInt("lesson_duration", 60), new LtimeListener() {
                            @Override
                            public void onValidTime(int val) {
                                onValidTimeChanged(val);
                            }
                        });
                        loadLstSummery();
                        loadSummery();
                    }
                });
                break;
            case "lesson_break":
                final int prev2 = getPreferenceScreen().getSharedPreferences().getInt("lesson_break", 0);
                showPickerDialog("lesson_break", getString(R.string.lesson_break), getString(R.string._minutes), 0, 30, 0, new Listener() {
                    @Override
                    public void onChanged() {
                        LTHelper.onChangeLessonGap(prev2, getPreferenceScreen().getSharedPreferences().getInt("lesson_break", 0), new LtimeListener() {
                            @Override
                            public void onValidTime(int val) {
                                onValidTimeChanged(val);
                            }
                        });
                        loadLstSummery();
                        loadSummery();
                    }
                });
                break;
            case "enabled_day":
                break;
            default:
                showTimeDialog(preference.getKey());

        }
        return false;
    }

    private void showTimeDialog(final String key) {
        final int lesson = Util.strToInt(key.replace("lst_", ""));
        LessonTimeHelper.LessonTime lessonTime = LTHelper.getLessonTime(lesson);
        RadialTimePickerDialogFragment dialogFragment = new RadialTimePickerDialogFragment()
                .setForced12hFormat()
                .setThemeLight()
                .setStartTime(lessonTime.getLessonStartTime(0).getHourOfDay(),lessonTime.getLessonStartTime(0).getMinuteOfHour()).setThemeDark()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        LTHelper.changeLessonStartTime(lesson, hourOfDay, minute, new LtimeListener() {
                            @Override
                            public void onValidTime(int val) {
                                onValidTimeChanged(val);
                            }
                        });
                        loadLstSummery();
                    }
                });
        dialogFragment.show(getFragmentManager() != null ? getFragmentManager() : getActivity().getSupportFragmentManager(),"picker");
       /* TimePickerDialog dialog = new TimePickerDialog(getActivity(), R.style.ThemeOverlay_AppCompat_Dialog_Alert, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                LTHelper.changeLessonStartTime(lesson, i, i1, new LtimeListener() {
                    @Override
                    public void onValidTime(int val) {
                        onValidTimeChanged(val);
                    }
                });
                loadLstSummery();
            }
        }, lessonTime.getLessonStartTime(0).getHourOfDay(), lessonTime.getLessonStartTime(0).getMinuteOfHour(), false);
        dialog.show();*/
    }

    public void loadLstSummery() {
        LessonTimeHelper helper = new LessonTimeHelper(getContext());
        List<LessonTimeHelper.LessonTime> timeList = helper.loadLSTimes();
        int length = ShardPref.getInt(getContext(),"lesson_duration",60);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        for (int i = 0; i < startTime.size(); i++) {
            Preference lstPref = startTime.get("lst_" + (i + 1));
            if (lstPref != null) {
                lstPref.setSummary(timeList.get(i).getLstTimeString()
                        +" - "+dateFormat.format(timeList.get(i).getDateTime().plusMinutes(length).toDate()));
            }
        }
    }

    private void showPickerDialog(final String key, String title, final String formatter, int min, int max, int def, final Listener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        final NumberPicker picker = new NumberPicker(getContext());
        picker.setMinValue(min);
        picker.setMaxValue(max);
        picker.setValue(getPreferenceScreen().getSharedPreferences().getInt(key, def));
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(formatter, i);
            }
        });
        builder.setView(picker);
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPreferenceScreen().getSharedPreferences().edit().putInt(key, picker.getValue()).apply();
               //Analytics.logAppEvent(key,Util.intToStr(picker.getValue()));
                listener.onChanged();
            }
        });
        builder.show();
        picker.getValue();
    }
}
