package com.yotor.solution.mytimetable.ui.tour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yotor.solution.mytimetable.MainActivity;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;

public class TourMain extends AppCompatActivity {
    ViewPager pager;
    PageIndicator indicator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.loadLanguage(this,null);
        if (!ShardPref.getBool(this,"firstRun",true)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.tour_main);
        pager = findViewById(R.id.tour_pager);
        indicator = findViewById(R.id.tour_pager_indicator);
        TourFragmentAdapter adapter = new TourFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentPage(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
