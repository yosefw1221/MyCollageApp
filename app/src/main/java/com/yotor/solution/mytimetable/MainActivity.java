package com.yotor.solution.mytimetable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

////import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.ui.Setting.GeneralPreferenceFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.activity_main);
       //MobileAds.initialize(this);
        //AdsUtil.showInterstitialAd(this,40);
        try {
            navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);
        } catch (Exception e) {
            //Crashlytics.logException(e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (navView!=null&&navView.getSelectedItemId()==R.id.navigation_home) {
                if (!Util.rateAppDialog(this, true, false))
                    super.onBackPressed();
                //else if (AdsUtil.ad!=null&&AdsUtil.ad.isLoaded()&&AppConfig.showAdOnExit()){
                   // AdsUtil.ad.show();
               // }
            }else
                super.onBackPressed();
        }catch (Exception e){
           ////Crashlytics.logException(e);
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       // AdsUtil.showInterstitialAd(this,40);
        if (GeneralPreferenceFragment.THEME_CHANGED){
         recreate();
         GeneralPreferenceFragment.THEME_CHANGED = false;
        }
    }
}
