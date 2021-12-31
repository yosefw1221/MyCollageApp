package com.yotor.solution.mytimetable.portal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

//import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.yotor.solution.mytimetable.AdsUtil;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.AppConfig;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.GpaList;
import com.yotor.solution.mytimetable.portal.model.GradeModel;
import com.yotor.solution.mytimetable.portal.ui.SpinnerListener;

import java.util.List;


public class GpaCalculator extends AppCompatActivity implements SpinnerListener {
    private ListView listView;
    private TextView INFO;
    private StudentDB db;
    private float Grade;
    private List<GradeModel> model;
    private String name;
   // private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.gpa_view);
        listView = findViewById(R.id.gpa_listview);
     //   adView = findViewById(R.id.adView);
        listView.setEmptyView(findViewById(R.id.gpa_empty_view));
        INFO = findViewById(R.id.gpa_info_tv);
        db = new StudentDB(this);
        name = getIntent().getStringExtra("gpaName");
        if (name==null)
            model = db.getAllCourse();
        else if (name.isEmpty())
            model = db.getAllCourse();
        else
            model = db.getCourseGrade(name);
        try {
//            if (AppConfig.isBannerOnGpa()&& AdsUtil.isNetworkConnected(this)){
//                adView.setVisibility(View.VISIBLE);
//               // adView.loadAd(new AdRequest.Builder().build());
//            }else adView.setVisibility(View.GONE);
            Toolbar bar = findViewById(R.id.toolbar);
            if (bar != null) {
                bar.setTitle(getString(R.string.grade_calculator_title));
                bar.setNavigationIcon(R.drawable.ic_back_white_24dp);
                bar.inflateMenu(R.menu.gpa_menu);
                bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        saveGradeDialog();
                        return false;
                    }
                });
                bar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }


    }

    private void saveGradeDialog() {
        if (name!=null){
            if (db.createUpdateCourseGrade(model,new GpaList(name,Grade),true))
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }else {
           //Analytics.logClickEvent("GpaCalculator","Grade Saved");
        final AppCompatEditText editText = new AppCompatEditText(GpaCalculator.this);
        editText.setHint(getResources().getString(R.string.name));
        AlertDialog.Builder builder = new AlertDialog.Builder(GpaCalculator.this)
                .setTitle("Save Grade")
                .setView(editText)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().toString().isEmpty())
                            editText.setError("Name is required!");
                        else {
                            String name  = editText.getText().toString().replace("@","_").replace(","," ");
                            if (db.createUpdateCourseGrade(model,new GpaList(name,Grade),false))
                                Toast.makeText(GpaCalculator.this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(GpaCalculator.this, getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.close, null);
        builder.show();

    }}

    @Override
    protected void onResume() {
        super.onResume();
        try {
           // if (adView!=null)adView.resume();
            GradeViewAdapter adapter = new GradeViewAdapter(GpaCalculator.this, model, this);
            listView.setAdapter(adapter);
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        //if (adView!=null)adView.pause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // if (adView!=null)adView.destroy();

    }
    @Override
    public void onItemSelected() {
        try {
            GradeModel gradeModel;
            int TotalECTS = 0;
            float GradePoint = 0;
            int TotalCourse = listView.getCount();
            for (int i = 0; i < TotalCourse; i++) {
                gradeModel = (GradeModel) listView.getAdapter().getItem(i);
                TotalECTS += gradeModel.getECTS();
                GradePoint += gradeModel.getECTS() * Util.getGradeMultiplyer(gradeModel.getSelectedGRADE());
            }
            Grade = GradePoint / TotalECTS;
            INFO.setText(String.format(getString(R.string.gpa_view_detail), TotalCourse, TotalECTS, GradePoint, TotalECTS * Util.getGradeMultiplyer(0), Grade));
        } catch (Exception e) {
           //Crashlytics.logException(e);
            recreate();
        }
    }
}
