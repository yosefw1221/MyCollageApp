package com.yotor.solution.mytimetable.ui.tour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.yotor.solution.mytimetable.R;

public class TourSecondFragment extends Fragment {
    AppCompatTextView desc_tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tour_second,container,false);
        desc_tv = view.findViewById(R.id.tour_two_desc);
        desc_tv.setText(R.string.tour_two_guide_desc);
        return view;
    }
}
