package com.yotor.solution.mytimetable.portal;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

//import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;




public class CourseAddUpdate extends AppCompatActivity {

    public static boolean UPDATE_VIEW = false ;
    private AppCompatImageView course_colorView_root;
    int courseID;
    private TextInputLayout CourseNameField;
    private TextInputLayout CourseCodeField;
    private TextInputLayout CourseAbbriField;
    private TextInputLayout CourseEctsField;
    boolean isUpdate;

    private int COLOR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.loadDialogTheme(this);
        setContentView(R.layout.create_update_course);
         isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (isUpdate)
            courseID = getIntent().getIntExtra("courseID", -1);
        course_colorView_root = findViewById(R.id.course_color_view);
        FloatingActionButton color_FAB = findViewById(R.id.color_choice_fab);
        Button SAVE_btn = findViewById(R.id.save_btn);
        Button CLOSE_btn = findViewById(R.id.close_btn);
         CLOSE_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                //Analytics.logClickEvent("CourseAddUpdate","CloseBtn");
                 finish();
             }
         });
       // COURSE_NAME = findViewById(R.id.course_name);
        CourseNameField = findViewById(R.id.course_name_field);
        CourseCodeField = findViewById(R.id.course_code_field);
        CourseAbbriField = findViewById(R.id.course_abbr_field);
        CourseEctsField = findViewById(R.id.course_ects_field);
        if (CourseNameField.getEditText()!=null)
       CourseNameField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count<12){
                    if (CourseAbbriField.getEditText()!=null)
                    CourseAbbriField.getEditText().setText(CourseNameField.getEditText().getText());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        try {
            course_colorView_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorDialog();
                }
            });
            color_FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorDialog();
                }
            });
        }catch (Exception e){
           //Crashlytics.logException(e);
        }

        SAVE_btn.setText(isUpdate ? getString(R.string.update_course) : getString(R.string.add_to_my_course));
        try {
            if (isUpdate) {
                final StudentDB db = new StudentDB(this);
                Course COURSE = db.getCourse(courseID);
                //COURSE_NAME.setText(COURSE.getNAME());
                CourseNameField.getEditText().setText(COURSE.getNAME());
                CourseCodeField.getEditText().setText(COURSE.getCODE()==null?"": COURSE.getCODE());
                CourseAbbriField.getEditText().setText(COURSE.getABBREVIATION()==null?"": COURSE.getABBREVIATION());
                CourseEctsField.getEditText().setText(""+ COURSE.getECTS());
                COLOR = COURSE.getCOLOR();

            }
            VectorDrawableCompat drawableCompat =  VectorDrawableCompat.create(getResources(),R.drawable.ic_lesson_topheader_blue,getTheme());
            Drawable tint = Util.getTintDrawable(drawableCompat, Util.getColorfromindex(getApplicationContext(),COLOR));
            course_colorView_root.setImageDrawable(tint);
            SAVE_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UpdateorCreateCourse()) {
                        UPDATE_VIEW = true;
                        Toast.makeText(CourseAddUpdate.this, R.string.saved_successfully, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CourseAddUpdate.this, R.string.error_occured, Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }
    public boolean UpdateorCreateCourse(){
        try {
        final StudentDB db = new StudentDB(this);
        Course course = new Course();
        String name = CourseNameField.getEditText().getText().toString().trim();
        String code = CourseCodeField.getEditText().getText().toString().trim();
        String abbri = CourseAbbriField.getEditText().getText().toString().trim();
        String ects = CourseEctsField.getEditText().getText().toString().trim();
        if (!name.isEmpty()&&!ects.isEmpty()){
            int namelength = name.length();
            course.setNAME(name);
            course.setCODE(code);
            if (abbri.isEmpty())
                course.setABBREVIATION(namelength>15?name.substring(0,14):name);
            else course.setABBREVIATION(abbri);
            course.setCOLOR(COLOR);
            course.setECTS(ects);
            if (isUpdate) {
               //Analytics.logAppEvent("CourseAddUpdate","Course Updated");
                return db.updateCourse(course, courseID);
            } else{
                Bundle bundle = new Bundle();
                bundle.putString("name",course.getNAME());
                bundle.putInt("ects",course.getECTS());
                bundle.putInt("color",course.getCOLOR());
                bundle.putString("code",course.getCODE());
                bundle.putString("abri",course.getABBREVIATION());
               //Analytics.logAppEvent("Course_Created",bundle);
                return db.createCourse(course);
        }}
        else {
            if (ects.isEmpty())
                CourseEctsField.setError(getString(R.string.course_ect_empty));
            if (name.isEmpty())
                CourseNameField.setError(getString(R.string.course_name_empty));
           //Analytics.logAppEvent("Course_AddUpdate","Error empty fields");
            return false;
        }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           //Crashlytics.logException(e);
            return false;
        }
}
    public void showColorDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(new ColorPickAdapter(this), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                COLOR = which;
                VectorDrawableCompat drawableCompat =  VectorDrawableCompat.create(getResources(),R.drawable.ic_lesson_topheader_blue,getTheme());
                Drawable tint = Util.getTintDrawable(drawableCompat, Util.getColorfromindex(getApplicationContext(),COLOR));
                course_colorView_root.setImageDrawable(tint);
               //Analytics.logAppEvent("CourseAddUpdate_Color_changed","color"+which);
                dialog.dismiss();

            }
        });
        builder.show();
    }
}

