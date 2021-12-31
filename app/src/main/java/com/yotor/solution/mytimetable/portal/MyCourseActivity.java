package com.yotor.solution.mytimetable.portal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;

import java.util.List;


public class MyCourseActivity extends AppCompatActivity {

    private RelativeLayout empty_view;
    private Course COURSE;
    private ListView listView;
    StudentDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.my_course_view);
        db = new StudentDB(this);
        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle(getString(R.string.my_course_title));
        bar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white_24dp));
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = findViewById(R.id.my_course_list);
        listView.setEmptyView(findViewById(R.id.empty_course_view));
        FloatingActionButton create_FAB = findViewById(R.id.create_course_fab);
        create_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Analytics.logClickEvent("MyCourseActivity","Create Course fab");
                Intent intent = new Intent(MyCourseActivity.this, CourseAddUpdate.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            final List<Course> courseList = db.getAllCourses();
            MyCourseAdapter adapter = new MyCourseAdapter(this, courseList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyCourseActivity.this, CourseView.class);
                    intent.putExtra("courseID", courseList.get(position).getID());
                    intent.putExtra("isCourse", true);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }


}
