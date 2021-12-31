package com.yotor.solution.mytimetable.qrcode;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.Util;

public class QrCodeGenerator extends AppCompatActivity {
    ViewPager viewPager;
    AppCompatImageButton next,prev;
    AppCompatTextView tv1,tv2,tv3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.qrcode_generator);
        try {
        viewPager = findViewById(R.id.qr_code_pager);
        next = findViewById(R.id.qr_next);
        prev = findViewById(R.id.qr_prev);
        tv1 = findViewById(R.id.qr_code_chtv_1);
        tv2 = findViewById(R.id.qr_code_chtv_2);
        tv3 = findViewById(R.id.qr_code_chtv_3);
        QrFragmentAdapter fragmentAdapter = new QrFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0,true);
        indicateCurrentQrCode(viewPager.getCurrentItem());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               //Analytics.logClickEvent("QrCodeGenerator", "pager_"+position);
                indicateCurrentQrCode(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int pos =  viewPager.getCurrentItem()==2?0:viewPager.getCurrentItem()+1;
               //Analytics.logClickEvent("QrCodeGenerator", "NextBtn");
               viewPager.setCurrentItem(pos,true);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =  viewPager.getCurrentItem()==0?2:viewPager.getCurrentItem()-1;
                viewPager.setCurrentItem(pos,true);
               //Analytics.logClickEvent("QrCodeGenerator", "PrevBtn");
            }
        });

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void indicateCurrentQrCode(int position) {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scaleanim);
        tv1.clearAnimation();
        tv2.clearAnimation();
        tv3.clearAnimation();
        switch (position){
            case 0:
                tv1.startAnimation(animation);
                break;
            case 1:
                tv2.startAnimation(animation);
                break;
            case 2:
                tv3.startAnimation(animation);
                break;
        }
    }
}
