package com.yotor.solution.mytimetable.ui.tour;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TourFragmentAdapter extends FragmentPagerAdapter {
    public TourFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
               return new TourFristFragment();
            case 1:
                return new TourSecondFragment();
            case 2:
                return new TourThirdFragment();
            case 3:
                return new TourFourthFragment();
        }
        return new TourFourthFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
