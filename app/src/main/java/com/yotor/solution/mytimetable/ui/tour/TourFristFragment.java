package com.yotor.solution.mytimetable.ui.tour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;

import java.util.Locale;

public class TourFristFragment extends Fragment {
    private AppCompatTextView desc, title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tour_frist, container, false);
        try {
            AppCompatRadioButton en_radio = view.findViewById(R.id.tour_eng_radio);
            AppCompatRadioButton am_radio = view.findViewById(R.id.tour_am_radio);
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("am"))
                am_radio.setChecked(true);
            else en_radio.setChecked(true);
            desc = view.findViewById(R.id.tour_one_desc);
            title = view.findViewById(R.id.tour_one_title);
            loadResource();
            en_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    loadResource();
                    if (Locale.getDefault().getLanguage().equalsIgnoreCase("am") && b) {
                        ShardPref.saveValue(getContext(), "language", "en");
                        Util.loadLanguage(getContext(), "en");
                        getActivity().recreate();
                    }
                }
            });
            am_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (Locale.getDefault().getLanguage().equalsIgnoreCase("en") && b) {
                        ShardPref.saveValue(getContext(), "language", "am");
                        Util.loadLanguage(getContext(), "am");
                        getActivity().recreate();
                    }
                }
            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return view;
    }

    private void loadResource() {
        desc.setText(R.string.tour_first_desc);
        title.setText(R.string.wellcome);
    }
}
