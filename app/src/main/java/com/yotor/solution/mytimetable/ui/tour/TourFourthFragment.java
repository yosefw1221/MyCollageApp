package com.yotor.solution.mytimetable.ui.tour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.MainActivity;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import java.util.Locale;

public class TourFourthFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tour_fourth, container, false);
        AppCompatButton button = view.findViewById(R.id.tour_get_start_btn);
        AppCompatTextView desc_tv = view.findViewById(R.id.tour_four_desc);
        AppCompatTextView rate_tv = view.findViewById(R.id.tour_four_rate);
        desc_tv.setText(R.string.tour_four_desc);
        button.setText(R.string.get_started);
        rate_tv.setText(R.string.tour_rate_app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StudentDB db = new StudentDB(getContext());
                            db.getAllLessons();
                            ShardPref.saveValue(getContext(), "firstRun", false);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            loadSuggestedData();
                            if (getActivity() != null)
                                getActivity().finish();
                        } catch (Exception e) {
                           //Crashlytics.logException(e);
                            ShardPref.saveValue(getContext(), "firstRun", false);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                });


            }
        });
        return view;
    }

    private void loadSuggestedData() {
        try {
            if (getContext() != null) {
                TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null && telephonyManager.getNetworkCountryIso().equalsIgnoreCase("et")) {
                    ShardPref.saveValue(getContext(), "total_lesson", 8);
                    ShardPref.saveValue(getContext(), "lesson_break", 5);
                    LessonTimeHelper timeHelper = new LessonTimeHelper(getContext());
                    if (Locale.getDefault().getLanguage().equalsIgnoreCase("am")) {
                        timeHelper.changeLessonStartTime(1, 2, 10, null);
                        timeHelper.changeLessonStartTime(5, 7, 30, null);
                    } else {
                        timeHelper.changeLessonStartTime(1, 8, 10, null);
                        timeHelper.changeLessonStartTime(5, 13, 30, null);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
        }
    }
}
