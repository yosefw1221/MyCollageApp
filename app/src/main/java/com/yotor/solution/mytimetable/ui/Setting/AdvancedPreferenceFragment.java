package com.yotor.solution.mytimetable.ui.Setting;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
@Keep
interface AlertListener {
    void onConfirmed();
}
@Keep
@KeepName
public class AdvancedPreferenceFragment extends PreferenceFragmentCompat {
   private StudentDB studentDB;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.advanced_preference, rootKey);
        studentDB = new StudentDB(getContext());
        /*Preference deleteAllCourse = findPreference("delete_course");*/
        /*Preference deleteAllLesson = findPreference("delete_lesson");*/
        /*Preference deleteAllExamTask = findPreference("delete_exam_task");*/
        /*Preference resetAppSetting = findPreference("reset_app_setting");*/
        /*Preference clearCache = findPreference("clear_catch");*/
    }

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
       //Analytics.logAppEvent("Advanced_setting",preference.getKey());
        switch (preference.getKey()) {
            case "delete_course":
                showAlertDialog(getString(R.string.delete_all_course_title), getString(R.string.delete_all_course_msg), new AlertListener() {
                    @Override
                    public void onConfirmed() {
                        studentDB.deleteCourse(-1);
                       //Analytics.logAppEvent("confirmed","Deleted all course");
                    }
                });
                break;
            case "delete_lesson":
                showAlertDialog(getString(R.string.delete_all_lesson_title), getString(R.string.delete_all_lesson_msg), new AlertListener() {
                    @Override
                    public void onConfirmed() {
                        studentDB.deleteAllLesson(-1);
                       //Analytics.logAppEvent("confirmed","Deleted all lesson");
                    }
                });
                break;
            case "delete_exam_task":
                showAlertDialog(getString(R.string.delete_examtask_title), getString(R.string.delete_examtask_msg), new AlertListener() {
                    @Override
                    public void onConfirmed() {
                        try {
                            studentDB.getWritableDatabase().delete(StudentDB.TABLE4, null, null);
                        } catch (Exception e) {
                           //Crashlytics.logException(e);
                        }
                       //Analytics.logAppEvent("confirmed","Deleted all exam and task");
                    }
                });
                break;
            case "reset_app_setting":
                showAlertDialog(getString(R.string.reset_app_setting_title), getString(R.string.reset_app_setting_msg), new AlertListener() {
                    @Override
                    public void onConfirmed() {
                        try {
                            getPreferenceManager().getSharedPreferences().edit().clear().apply();
                        } catch (Exception e) {
                           //Crashlytics.logException(e);
                        }
                       //Analytics.logAppEvent("confirmed","reset_app_setting");
                    }
                });
                break;
            case "clear_catch":
                showAlertDialog(getString(R.string.delete_catch_file_title), getString(R.string.delete_catch_file_msg), new AlertListener() {
                    @Override
                    public void onConfirmed() {
                        try {
                            getContext().getCacheDir().delete();
                        } catch (Exception e) {
                           //Crashlytics.logException(e);
                        }
                       //Analytics.logAppEvent("confirmed","clear_catch");

                    }
                });
                break;
        }
        return false;
    }

    private void showAlertDialog(String title, String msg, final AlertListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onConfirmed();
            }
        });
        builder.setNegativeButton(R.string.no,null);
        builder.show();
    }
}
