package com.yotor.solution.mytimetable.portal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

//import com.crashlytics.android.Crashlytics;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;




public class CourseView extends AppCompatActivity {

    private AppCompatImageView course_colorView_root;
    private TextView COURSE_NAME,COURSE_INFO;
    private Button CLOSE_Btn,EDIT_btn,DeleteLessonBtn;
    private int lessonId;
    int courseID;
    boolean isCourseActivity;
    StudentDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.loadDialogTheme(this);
        setContentView(R.layout.course_view);
        db = new StudentDB(this);
        courseID =  getIntent().getIntExtra("courseID", 0);
        lessonId = getIntent().getIntExtra("id", -1);
        isCourseActivity = getIntent().getBooleanExtra("isCourse",false);
        if (courseID==-1)
            finish();
        course_colorView_root = findViewById(R.id.course_color_view);
        CLOSE_Btn = findViewById(R.id.close_btn);
        EDIT_btn = findViewById(R.id.edit_course_btn);
        DeleteLessonBtn = findViewById(R.id.delete_lesson_btn);
        COURSE_NAME = findViewById(R.id.course_name);
        COURSE_INFO = findViewById(R.id.course_info);

}

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Course COURSE = db.getCourse(courseID);
            VectorDrawableCompat drawableCompat =  VectorDrawableCompat.create(getResources(),R.drawable.ic_lesson_topheader_blue,getTheme());
            Drawable tint = Util.getTintDrawable(drawableCompat,Util.getColorfromindex(getApplicationContext(), COURSE.getCOLOR()));
            course_colorView_root.setImageDrawable(tint);
            COURSE_NAME.setText(COURSE.getNAME());
            COURSE_INFO.setText(String.format(getString(R.string.course_view_detail),
                    COURSE.getNAME(), COURSE.getABBREVIATION(), COURSE.getCODE(), COURSE.getECTS()));
            CLOSE_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            DeleteLessonBtn.setText(isCourseActivity?getString(R.string.delete_course):getString(R.string.delete_lesson));
            DeleteLessonBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //Analytics.logClickEvent("CourseViewBtn","Delete Button clicked");
                    if (isCourseActivity) {
                       //Analytics.logAppEvent("CourseViewBtn","Course deleted");
                     db.deleteCourse(courseID);}
                    if (db.deleteLesson(lessonId)){
                       //Analytics.logAppEvent("CourseViewBtn","Lesson deleted");
                        Toast.makeText(CourseView.this,"Deleted "+lessonId,Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
            EDIT_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseView.this,CourseAddUpdate.class);
                    intent.putExtra("isUpdate",true);
                    intent.putExtra("courseID",courseID);
                   //Analytics.logClickEvent("CourseViewBtn","Edit Button clicked");
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
        }
    }
}
