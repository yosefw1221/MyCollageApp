package com.yotor.solution.mytimetable.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.AdsUtil;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.AppConfig;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.About;
import com.yotor.solution.mytimetable.portal.ExamTaskActivity;
import com.yotor.solution.mytimetable.portal.GpaListActivity;
import com.yotor.solution.mytimetable.portal.MyCourseActivity;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.Setting.SettingsHeaderActivity;
import com.yotor.solution.mytimetable.ui.timetable.TimetableFragment;
@KeepName
public class MeFragment extends Fragment implements View.OnClickListener {
   // private AdView adView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (AppConfig.terminateApp()){
            View root = inflater.inflate(R.layout.terminate_layout, container, false);
            ((AppCompatTextView)root.findViewById(R.id.terminate_msg)).setText(AppConfig.terminateMsg());
            return root;
        }
       //Analytics.trackScreen(getActivity(),"ME");
        Util.applyConfig(getActivity());
        View root = inflater.inflate(R.layout.me, container, false);
        AppCompatImageButton examTaskBtn = root.findViewById(R.id.me_examtask);
        AppCompatImageButton myCourseBtn = root.findViewById(R.id.me_course);
        AppCompatImageButton share_timetable = root.findViewById(R.id.me_share_timetable);
        AppCompatImageButton gpaCalculatorBtn = root.findViewById(R.id.me_gpa);
        AppCompatImageButton about = root.findViewById(R.id.me_about);
        AppCompatImageButton settingBtn = root.findViewById(R.id.me_setting);
        examTaskBtn.setOnClickListener(this);
        share_timetable.setOnClickListener(this);
        myCourseBtn.setOnClickListener(this);
        gpaCalculatorBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        about.setOnClickListener(this);
    //    adView = root.findViewById(R.id.adView);
//        if (AppConfig.isBannerOnMe()&& AdsUtil.isNetworkConnected(getContext())){
//            adView.setVisibility(View.VISIBLE);
//            adView.loadAd(new AdRequest.Builder().build());
//        }else adView.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
       // if (adView!=null)adView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (adView!=null)adView.pause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
  //      if (adView!=null)adView.destroy();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_examtask:
               //Analytics.logClickEvent("Me","Exam Task");
                startActivity(new Intent(getContext(), ExamTaskActivity.class));
                break;
            case R.id.me_course:
               //Analytics.logClickEvent("Me","My Course");
                startActivity(new Intent(getContext(), MyCourseActivity.class));
                break;
            case R.id.me_gpa:
               //Analytics.logClickEvent("Me","Gpa");
                startActivity(new Intent(getContext(), GpaListActivity.class));
                break;
            case R.id.me_setting:
               //Analytics.logClickEvent("Me","Setting");
                startActivity(new Intent(getContext(), SettingsHeaderActivity.class));
                break;
            case R.id.me_share_timetable:
                TimetableFragment.showShareDialog(getContext()).setTitle(R.string.share_my_timetable).show();
               //Analytics.logClickEvent("Me","Setting");
                break;
            case R.id.me_about:
                startActivity(new Intent(getContext(), About.class));
               //Analytics.logClickEvent("Me","About");
                break;

        }
    }
}