package com.yotor.solution.mytimetable.qrcode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class QrFragmentAdapter extends FragmentPagerAdapter {
    public QrFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new QrFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }



}
