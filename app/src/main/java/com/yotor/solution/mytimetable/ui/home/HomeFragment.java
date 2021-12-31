package com.yotor.solution.mytimetable.ui.home;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.common.annotation.KeepName;
//import com.yotor.solution.mytimetable.AdsUtil;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.AppConfig;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.GpaListActivity;
import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.qrcode.QrCodeGenerator;
import com.yotor.solution.mytimetable.qrcode.QrCodeScanner;
import com.yotor.solution.mytimetable.ui.Setting.SettingsHeaderActivity;
import com.yotor.solution.mytimetable.ui.home.recycler.ActionModel;
import com.yotor.solution.mytimetable.ui.home.recycler.RecyclerAdapter;
import com.yotor.solution.mytimetable.ui.home.recycler.RecyclerDataModel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
@KeepName
public class HomeFragment extends Fragment {
    private RecyclerAdapter adapter;
//    private AdView adView;
    private List<RecyclerDataModel> models;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       //Analytics.trackScreen(getActivity(), "HOME");
        Util.applyConfig(getActivity());
        if (AppConfig.terminateApp()){
            View root = inflater.inflate(R.layout.terminate_layout, container, false);
            ((AppCompatTextView)root.findViewById(R.id.terminate_msg)).setText(AppConfig.terminateMsg());
            return root;
        }
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            RecyclerView recyclerView = root.findViewById(R.id.home_recycler_view);
            //adView = root.findViewById(R.id.adView);
            models = new LinkedList<>();
            models.add(new RecyclerDataModel().basicCard());
            models.add(new RecyclerDataModel().lessonTimer());
            models.add(getRandomActionModel());
            models.add(new RecyclerDataModel().upcomingExamTask());
            adapter = new RecyclerAdapter(getContext(), models);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            recyclerView.setAdapter(adapter);
            Toolbar toolbar = root.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.day_night_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                        ShardPref.saveValue(getContext(),"nightMode",AppCompatDelegate.MODE_NIGHT_NO);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        Toast.makeText(getContext(), "nightMode off", Toast.LENGTH_SHORT).show();
                    }else {
                        ShardPref.saveValue(getContext(),"nightMode",AppCompatDelegate.MODE_NIGHT_YES);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        Toast.makeText(getContext(), "nightMode on", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
//            if (AppConfig.isBannerOnHome()&& AdsUtil.isNetworkConnected(getContext())){
//                adView.setVisibility(View.VISIBLE);
//                adView.loadAd(new AdRequest.Builder().build());
//            }else adView.setVisibility(View.GONE);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int w = (int) (point.x- getContext().getResources().getDisplayMetrics().density*12);
 //           if (AppConfig.isNativeOnHome())
//            AdsUtil.loadNativeAds(getContext(), w, new AdsUtil.adsCallback() {
//                @Override
//                public void onLoaded(NativeExpressAdView expressAdView) {
//                    Log.e("Native ads loaded","try to bind list");
//                    models.add(new RecyclerDataModel().adsCard(expressAdView));
//                    adapter.notifyDataSetChanged();
//                }
//                @Override
//                public void onField(int errorCode) {
//                    Log.e("Native ads error"," code "+errorCode);
//                }
//            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return root;
    }

    public RecyclerDataModel getRandomActionModel() {
        List<RecyclerDataModel> modelList = new LinkedList<>();
        RecyclerDataModel message = AppConfig.getAppMessage(getContext());
        if (message!=null){
            if (message.getActionModel().isPin()){
                return message;
            }else {
                modelList.add(message);
            }
        }
        modelList.add(new RecyclerDataModel().hintAction(getString(R.string.did_you_know), getString(R.string.action_card_share_timetable_msg),
                getString(R.string.try_it), Util.getTintDrawable(getResources().getDrawable(R.drawable.ic_share_white_24dp), R.color.colorPrimary),
                new ActionModel().actionLaunchActivity(QrCodeGenerator.class)));
        modelList.add(new RecyclerDataModel().hintAction(getString(R.string.did_you_know), getString(R.string.action_card_copy_timetable_msg), getString(R.string.try_it), Util.getTintDrawable(getResources().getDrawable(R.drawable.ic_file_download_black_24dp), R.color.colorPrimary),
                new ActionModel().actionLaunchActivity(QrCodeScanner.class)));
        modelList.add(new RecyclerDataModel().hintAction(getString(R.string.themes), getString(R.string.action_card_theme), getString(R.string.go_to_settings), Util.getTintDrawable(getResources().getDrawable(R.drawable.ic_color_lens_pink_a400_24dp), R.color.colorPrimary),
                new ActionModel().actionLaunchActivity(SettingsHeaderActivity.class)));
        modelList.add(new RecyclerDataModel().hintAction(getString(R.string.timetable_settings), getString(R.string.action_card_timetable), getString(R.string.go_to_settings), Util.getTintDrawable(getResources().getDrawable(R.drawable.time_table), R.color.colorPrimary),
                new ActionModel().actionLaunchActivity(SettingsHeaderActivity.class)));
        modelList.add(new RecyclerDataModel().hintAction(getString(R.string.did_you_know), getString(R.string.action_card_gpa), getString(R.string.try_it), Util.getTintDrawable(getResources().getDrawable(R.drawable.ic_info_outline_light_green_500_18dp), R.color.colorPrimary),
                new ActionModel().actionLaunchActivity(GpaListActivity.class)));
        modelList.add(new RecyclerDataModel().hintAction("App Widget to home screen", "Add MyCollage App Widget to your home screen to access your lesson and exam/task", "...", Util.getTintDrawable(getResources().getDrawable(R.drawable.time_table), R.color.colorPrimary),
               null));
        Collections.shuffle(modelList);
        return modelList.get(0);
    }

    @Override
    public void onResume() {
        super.onResume();
       // if (adView!=null)adView.resume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //if (adView!=null)adView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if (adView!=null)adView.destroy();
    }
}