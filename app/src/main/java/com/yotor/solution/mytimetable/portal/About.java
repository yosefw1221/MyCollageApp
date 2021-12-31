package com.yotor.solution.mytimetable.portal;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.annotation.KeepName;
import com.yotor.solution.mytimetable.AppConfig;
import com.yotor.solution.mytimetable.R;

public class About extends AppCompatActivity {
    private LinearLayout thanks;
    private AppCompatImageView icon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.about);
        thanks = findViewById(R.id.thanks_layout);
        icon = findViewById(R.id.icon_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @KeepName
    public void fbContact(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(AppConfig.getFBlink()));
            startActivity(intent);
        }catch (Exception e){
           //Crashlytics.logException(e);
        }

    }
    @KeepName
    public void tgContact(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(AppConfig.getTGlink()));
            startActivity(intent);
        }catch (Exception e){
           //Crashlytics.logException(e);
        }
    }
    @KeepName
    public void emailContact(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+AppConfig.getEmail()));
            StringBuilder data = new StringBuilder();
            data.append("SDK ").append(Build.VERSION.SDK_INT).append("\nBRAND ").append(Build.BRAND).append("\nMODEL "+Build.MODEL)
            .append("\nMANUFACTURE : ").append(Build.MANUFACTURER).append("\nWrite your feedback...");
            intent.putExtra(Intent.EXTRA_TEXT,data.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent.createChooser(intent,"Send Feedback via");
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, "yosefworku18@gmail.com", Toast.LENGTH_LONG).show();
           //Crashlytics.logException(e);
        }
    }
    @KeepName
    public void callContact(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("tel:"+AppConfig.getCallNo());
            intent.setData(uri);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, AppConfig.getCallNo(), Toast.LENGTH_LONG).show();
           //Crashlytics.logException(e);
        }
    }
    @KeepName
    public void rate(View view) {
        Util.rateAppDialog(this,false,true);
    }
    @KeepName
    public void thanks(View view) {
        if (thanks.getVisibility()==View.VISIBLE)return;
        thanks.setVisibility(View.VISIBLE);
        icon.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                thanks.setVisibility(View.GONE);
                icon.setVisibility(View.VISIBLE);
            }
        },10000);
    }
}
