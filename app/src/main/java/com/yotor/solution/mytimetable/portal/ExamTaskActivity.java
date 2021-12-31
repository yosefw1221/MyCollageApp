package com.yotor.solution.mytimetable.portal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;
import com.yotor.solution.mytimetable.portal.ui.ExamTaskAdapter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExamTaskActivity extends AppCompatActivity {
    private FloatingActionButton ExamTaskFAB;
    private LinearLayout ExamTaskLayout,ExamLayout,TaskLayout;
    private GridView ExamTaskList;
    private StudentDB db;
    private List<ExamTaskModel> examTaskList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.exam_view);
        db = new StudentDB(this);
        ExamTaskFAB = findViewById(R.id.examtask_fab);
        FloatingActionButton examFAB = findViewById(R.id.exam_fab);
        FloatingActionButton taskFAB = findViewById(R.id.task_fab);
        ExamTaskLayout = findViewById(R.id.examtask_fab_parent);
        ExamLayout = findViewById(R.id.exam_fab_parent);
        TaskLayout = findViewById(R.id.task_fab_parent);
        ExamTaskList = findViewById(R.id.examtask_list);
        ArrayAdapter adapter = new ExamTaskAdapter(this,db.getAllTaskExam(0));
        ExamTaskList.setAdapter(adapter);
        ExamTaskList.setEmptyView(findViewById(R.id.empty_examtask_view));
        ExamTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showExamTaskInfoDialog(examTaskList.get(i));
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar!=null){
            toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }


        ExamTaskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Analytics.logClickEvent("ExamTaskActivity","Create Fab clicked");
                ShowHideFabs();
            }
        });
        examFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHideFabs();
               //Analytics.logClickEvent("ExamTaskActivity","Exam Fab clicked");
                Intent intent = new Intent(ExamTaskActivity.this,CreateExamTask.class);
                intent.putExtra("TYPE",0);
                startActivity(intent);
            }
        });
        taskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHideFabs();
               //Analytics.logClickEvent("ExamTaskActivity","Task Fab clicked");
                Intent intent = new Intent(ExamTaskActivity.this,CreateExamTask.class);
                intent.putExtra("TYPE",1);
                startActivity(intent);
            }
        });
    }

    private void showExamTaskInfoDialog(final ExamTaskModel examTaskModel) {
       //Analytics.logAppEvent("ExamTaskActivity","showExamTaskInfoDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.examtaskview_dialog,null);
        AppCompatImageView header = v.findViewById(R.id.etv_header_layout);
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_lesson_topheader_blue,getTheme());
        Drawable d = Util.getTintDrawable(vectorDrawableCompat,Util.getColorfromindex(this,examTaskModel.getColor()));
        header.setImageDrawable(d);
        AppCompatTextView title = v.findViewById(R.id.etv_title);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(examTaskModel.getDATE());
        String date =  DateFormat.getDateInstance(DateFormat.MEDIUM,new Locale("am")).format(calendar.getTime());
        title.setText(date);
        AppCompatTextView info = v.findViewById(R.id.etv_info_tv);
        String s = String.format(getString(R.string.examtask_detail),examTaskModel.getTOPIC(),examTaskModel.getCOURSENAME()==null?" - ":examTaskModel.getCOURSENAME(),date,examTaskModel.getNOTES());
        info.setText(s);
        AppCompatCheckBox isDone = v.findViewById(R.id.etv_isdone_chxbox);
        isDone.setChecked(examTaskModel.isDONE());
        isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                db.setExamDoneUpdate(examTaskModel.getID(),b,null);
                onResume();
            }
        });
        builder.setView(v);
        builder.setNegativeButton(R.string.close,null);
        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ExamTaskActivity.this,CreateExamTask.class);
                intent.putExtra("TYPE",examTaskModel.getTYPE());
                intent.putExtra("ID",examTaskModel.getID());
               //Analytics.logClickEvent("ExamTaskActivity","Update clicked");
                startActivity(intent);
            }
        });

        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (db.deleteExamTask(examTaskModel.getID()))
                    Toast.makeText(ExamTaskActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                else Toast.makeText(ExamTaskActivity.this, getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
               //Analytics.logClickEvent("ExamTaskActivity","Delete clicked");
                onResume();
            }
        });
        builder.show();
    }

    private void ShowHideFabs() {
        final boolean show = ExamTaskLayout.getVisibility()==View.GONE;
        Animation animation;
        if (!show){
            animation = AnimationUtils.loadAnimation(ExamTaskActivity.this,R.anim.nav_default_exit_anim);
            RotateAnimation rotateAnimation = new RotateAnimation(135.0f,360.0f,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setFillAfter(true);
            ExamTaskFAB.startAnimation(rotateAnimation);
        }else{
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f,135.0f,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setFillAfter(true);
            ExamTaskFAB.startAnimation(rotateAnimation);
            animation = AnimationUtils.loadAnimation(ExamTaskActivity.this,R.anim.nav_default_enter_anim);
        }
        ExamTaskLayout.setVisibility(View.VISIBLE);
        animation.setDuration(300);
        Animation anim2 = animation;
        anim2.setStartOffset(250);
        ExamLayout.startAnimation(animation);
        TaskLayout.startAnimation(anim2);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (show)
                ExamTaskLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (!show)
                ExamTaskLayout.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        examTaskList = db.getAllTaskExam(0);
        ExamTaskAdapter adapter = new ExamTaskAdapter(this,examTaskList);
        ExamTaskList.setAdapter(adapter);

    }
}
