package com.yotor.solution.mytimetable.portal;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

//import com.crashlytics.android.Crashlytics;
import com.google.android.material.textfield.TextInputLayout;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class CreateExamTask extends AppCompatActivity {
    AppCompatImageView HeaderLayout;
    AppCompatTextView TypeTitle;
    AppCompatSpinner CourseSpinner;
    TextInputLayout Topic, Date, Description;
    AppCompatButton SaveBtn, CloseBtn;
    StudentDB db;
    DateTime cal;
    List<Course> courses;
    int TYPE;
    int ID;
    ExamTaskModel examTaskModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.loadDialogTheme(this);
        setContentView(R.layout.create_examtask);
        TYPE = getIntent().getIntExtra("TYPE", 0);
        ID = getIntent().getIntExtra("ID",0);
        db = new StudentDB(this);
        HeaderLayout = findViewById(R.id.bg_top_header);
        TypeTitle = findViewById(R.id.cet_title);
        TypeTitle.setText(TYPE==0?getString(R.string.new_exam_):getString(R.string.new_task_));
        cal = TYPE==0?DateTime.now().plusDays(7).toDateTime():DateTime.now().plusDays(2).toDateTime();
        Topic = findViewById(R.id.cet_topic);
        Date = findViewById(R.id.cet_date_picker);
        Description = findViewById(R.id.cet_description);
        CourseSpinner = findViewById(R.id.cet_course_spinner);
        CourseSpinner.setAdapter(getCourseAdapter());
        SaveBtn = findViewById(R.id.save_btn);
        if (ID!=0){
            TypeTitle.setText(TYPE==0?getString(R.string.update_exam):getString(R.string.update_task));
            List<ExamTaskModel> list =  db.getAllTaskExam(ID);
            if (list.size()>0)
                examTaskModel = db.getAllTaskExam(ID).get(0);
            SaveBtn.setText(R.string.update);
            Topic.getEditText().setText(examTaskModel.getTOPIC()==null?"":examTaskModel.getTOPIC());
            Description.getEditText().setText(examTaskModel.getNOTES()==null?"":examTaskModel.getNOTES());
            CourseSpinner.setEnabled(false);
            cal.withMillis(examTaskModel.getDATE());
        }
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.toDate());
        Date.getEditText().setText(date);
        CloseBtn = findViewById(R.id.close_btn);
        Date.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog();
            }
        });
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExamTask(ID!=0);
            }
        });
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             if (i!=0){
                 VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_lesson_topheader_blue,getTheme());
                 Drawable d = Util.getTintDrawable(vectorDrawableCompat,Util.getColorfromindex(CreateExamTask.this,courses.get(i-1).getCOLOR()));
                 HeaderLayout.setImageDrawable(d);
                //Analytics.logAppEvent("CreateExamTask","Course Spinner Selected");
             }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void datePickerDialog() {
       DatePickerDialog dialog = new DatePickerDialog(CreateExamTask.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                cal =  cal.withMillis(calendar.getTimeInMillis()).toDateTime();
                String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.toDate());
                if (Date.getEditText()!=null)
                Date.getEditText().setText(date);
               //Analytics.logAppEvent("CreateExamTask","Date Changed");
            }
        }, cal.getYear(), cal.getMonthOfYear(), cal.getDayOfMonth());
        dialog.show();

    }
    private SpinnerAdapter getCourseAdapter() {
        courses = db.getAllCourses();
        List<String> courseList = new LinkedList<>();
        courseList.add(getString(R.string.select_course_));
        for (int i = 0; i < courses.size(); i++) {
            courseList.add(courses.get(i).getNAME());
        }
        return new ArrayAdapter<>(CreateExamTask.this, android.R.layout.simple_list_item_1, courseList);
    }

    private void saveExamTask(boolean isUpdate) {
        try {
            if (Topic.getEditText().getText().toString().isEmpty()) {
                Topic.setError("* Required");
            } else {
                Topic.setError(null);
                ExamTaskModel examtask = new ExamTaskModel();
                if (CourseSpinner.getSelectedItemPosition() != 0) {
                    Course course = courses.get(CourseSpinner.getSelectedItemPosition() - 1);
                    examtask.setCOURSE_ID(course.getID());
                    examtask.setCOURSENAME(course.getNAME());
                }
                examtask.setTOPIC(Topic.getEditText().getText().toString());
                examtask.setDATE(cal.getMillis());
                examtask.setNOTES(Description.getEditText().getText().toString());
                examtask.setTYPE(TYPE);
                if (isUpdate)
                    examtask.setID(ID);
                db.AddExamTask(examtask,isUpdate);
                Bundle bundle = new Bundle();
                bundle.putString("topic",examtask.getTOPIC());
                bundle.putString("note",examtask.getNOTES());
                bundle.putBoolean("update",isUpdate);
                bundle.putInt("type",examtask.getTYPE());
               //Analytics.logAppEvent("CreateExamTask",bundle);
                finish();

            }
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }

}
