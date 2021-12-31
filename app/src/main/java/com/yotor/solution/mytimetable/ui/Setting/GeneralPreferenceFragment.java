package com.yotor.solution.mytimetable.ui.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.notifications.PermissionUtil;

@Keep
@KeepName
public class GeneralPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    public static boolean THEME_CHANGED = false;
    private Preference time_before;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.general_preferences, rootKey);
        ListPreference lang_pref = findPreference("language");
        ListPreference theme_pref = findPreference("themes");
        ListPreference automuteMode_pref = findPreference("automate_mode");
        time_before = findPreference("automate_before");
        lang_pref.setOnPreferenceChangeListener(this);
        theme_pref.setOnPreferenceChangeListener(this);
        automuteMode_pref.setOnPreferenceChangeListener(this);
        time_before.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadSummery();
    }

    private void loadSummery() {
        String timeBefore =  String.format(getString(R.string.minute_before_lesson),getPreferenceScreen().getSharedPreferences().getInt("automate_before",5) );
        time_before.setSummary(timeBefore);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
       //Analytics.logClickEvent("GENERAL_PREF",preference.getKey());
       if (preference.getKey().equals("automate_before")) {
            showPickerDialog();
        }
        return false;
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
       //Analytics.logClickEvent("GENERAL_PREF",preference.getKey());
        switch (preference.getKey()) {
            case "language":
                if (ShardPref.getString(getContext(),"language","").equalsIgnoreCase((String) newValue))
                    return true;
                Util.loadLanguage(getContext(), newValue.toString());
               //Analytics.logAppEvent("Language_changed", newValue.toString());
                THEME_CHANGED = true;
                getActivity().recreate();
                break;
            case "themes":
                if (ShardPref.getString(getContext(), "themes", "").equalsIgnoreCase((String) newValue))
                    return true;
                Util.applyConfig(getActivity());
               //Analytics.logAppEvent("Theme_changed", newValue.toString());
                THEME_CHANGED = true;
                getActivity().recreate();
                break;
            case "automate_mode":
                if (newValue.equals("1"))
                   //Analytics.logAppEvent("automate_mode", newValue.toString());
                return showPermissionDialog();
        }
        return true;
    }

    private boolean showPermissionDialog() {
        /// do not destrurb access
        if (PermissionUtil.isNotificationGranted(getContext()))return true;
        else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, 89);
                Toast.makeText(getContext(), getString(R.string.grant_notif_permision), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==89){
            if (PermissionUtil.isNotificationGranted(getContext())){
               //Analytics.logAppEvent("DO_NOT_DISTURB","ACCESS_GRANTED");
                Toast.makeText(getContext(), getString(R.string.notif_granted), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), getString(R.string.notification_permision_denied), Toast.LENGTH_SHORT).show();
               //Analytics.logAppEvent("DO_NOT_DISTURB","ACCESS_DINED");
            }

        }
    }

    private void showPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.time_before_title);
        final NumberPicker picker = new NumberPicker(getContext());
        picker.setMinValue(0);
        picker.setMaxValue(30);
        picker.setValue(getPreferenceScreen().getSharedPreferences().getInt("automate_before",5));
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(getString(R.string.automate_no_picker_format), i);
            }
        });
        builder.setView(picker);
        builder.setNegativeButton(getString(R.string.cancel),null);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPreferenceScreen().getSharedPreferences().edit().putInt("automate_before", picker.getValue()).apply();
                loadSummery();
            }
        });
        builder.show();
        picker.getValue();

    }
}
