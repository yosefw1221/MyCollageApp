package com.yotor.solution.mytimetable.portal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

//import com.crashlytics.android.Crashlytics;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import java.util.LinkedList;
import java.util.List;

public class CreateLessonDialog {
    private  Spinner lessonHourSpinner;
    private TimetableChangeListener timetableChangeListener;
    private Spinner courseSpinner;
    private Context ctx;
    private StudentDB db;

    public CreateLessonDialog(Context context) {
        ctx = context;
        timetableChangeListener = null;
        db = new StudentDB(context);
    }

    public AlertDialog getDialog(final int lessonId, TimetableChangeListener listener) {
        timetableChangeListener = listener;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        try {
            builder.setTitle(null);
            View view = LayoutInflater.from(ctx).inflate(R.layout.create_lesson_dialog, null);
            courseSpinner = view.findViewById(R.id.course_spinner);
            lessonHourSpinner = view.findViewById(R.id.lesson_spinner);
            courseSpinner.setAdapter(getCourseSpinnerAdapter());
            lessonHourSpinner.setAdapter(getLessonLengthAdapter());
            builder.setView(view);
            courseSpinner.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.shakeanim));
            courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (CourseAddUpdate.UPDATE_VIEW) {
                        courseSpinner.setAdapter(getCourseSpinnerAdapter());
                        CourseAddUpdate.UPDATE_VIEW = false;
                    }
                    if (parent.getSelectedItem().toString().equalsIgnoreCase(ctx.getString(R.string.add_new_course_))) {
                        Intent intent = new Intent(ctx, CourseAddUpdate.class);
                        ctx.startActivity(intent);
                    }
                    if (parent.getSelectedItemPosition()==0){
                        courseSpinner.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.shakeanim));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    courseSpinner.setAdapter(getCourseSpinnerAdapter());
                }
            });
            builder.setPositiveButton(ctx.getString(R.string.add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (courseSpinner.getSelectedItemPosition() == 0) {
                        courseSpinner.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.shakeanim));
                       //Analytics.logAppEvent("CreateLessonDialog","add without select course ");
                    }
                    if (courseSpinner.getSelectedItemPosition() != 0){
                       //Analytics.logAppEvent("CreateLessonDialog","Lesson Created");
                        createLesson(lessonId);
                        timetableChangeListener.onChanged();
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton(ctx.getString(R.string.close),null);

        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return builder.create();
    }

    private SpinnerAdapter getCourseSpinnerAdapter() {
        List<Course> courses = db.getAllCourses();
        List<String> courseList = new LinkedList<>();
        courseList.add(ctx.getString(R.string.select_course));
        for (int i = 0; i < courses.size(); i++) {
            courseList.add(courses.get(i).getNAME());
        }
        courseList.add(ctx.getString(R.string.add_new_course_));
        return new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, courseList);
    }

    @SuppressLint("DefaultLocale")
    private SpinnerAdapter getLessonLengthAdapter() {
        List<String> list = new LinkedList<>();
        int duration = LessonTimeHelper.getLESSON_DURATION(ctx);
        list.add(String.format(ctx.getString(R.string.lesson_1_min), duration));
        list.add(String.format(ctx.getString(R.string.lesson_2_min), 2 * duration));
        list.add(String.format(ctx.getString(R.string.lesson_3_min), 3 * duration));
        list.add(String.format(ctx.getString(R.string.lesson_4_min), 4 * duration));
        return new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, list);
    }

    private void createLesson(int lessonId) {
        Lesson lesson = db.getLesson(lessonId);
        if (!courseSpinner.getSelectedItem().toString().equalsIgnoreCase(ctx.getString(R.string.add_new_course_))&&courseSpinner.getSelectedItemPosition()!=0) {
            lesson.setCOURSE(db.getCourse(courseSpinner.getSelectedItem().toString()));
            lesson.setSTART_TIME(lesson.getLESSON());
            lesson.setEND_TIME(lesson.getLESSON() * 60);
            switch (lessonHourSpinner.getSelectedItemPosition()) {
                case 3:
                    lesson.setID(lessonId + (StudentDB.MAXIMUM_LESSON_DAYS * 3));
                    db.updateLesson(lesson);
                case 2:
                    lesson.setID(lessonId + (StudentDB.MAXIMUM_LESSON_DAYS * 2));
                    db.updateLesson(lesson);
                case 1:
                    lesson.setID(lessonId + (StudentDB.MAXIMUM_LESSON_DAYS));
                    db.updateLesson(lesson);
                case 0:
                    lesson.setID(lessonId);
                    db.updateLesson(lesson);

            }
        } else {
            Toast.makeText(ctx, R.string.error_add_course_frist, Toast.LENGTH_LONG).show();
           //Analytics.logAppEvent("CreateLessonDialog","Error Add Course First");
        }
    }

}

