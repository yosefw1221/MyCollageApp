package com.yotor.solution.mytimetable.portal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

//import com.crashlytics.android.Crashlytics;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Course;

import java.util.List;


public class MyCourseAdapter extends ArrayAdapter {
    private List<Course> courseList;
    private Context ctx;


    MyCourseAdapter(Context context, List<Course> courses) {
        super(context, 0, courses);
        ctx = context;
        courseList = courses;
    }

    class Holder {
        AppCompatImageView courseIcon;
        AppCompatTextView title;
        AppCompatTextView subtitle;
        AppCompatButton editBtn;
        AppCompatButton deleteBtn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = new Holder();
        if (convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.mycourse_item, parent,false);
        }
        try {
            holder.courseIcon = view.findViewById(R.id.my_course_icon);
            holder.title = view.findViewById(R.id.my_course_name);
            holder.subtitle = view.findViewById(R.id.my_course_subtext);
            holder.editBtn = view.findViewById(R.id.my_course_edit_btn);
            holder.deleteBtn = view.findViewById(R.id.my_course_delete_btn);
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, CourseAddUpdate.class);
                    intent.putExtra("isUpdate", true);
                    intent.putExtra("courseID", courseList.get(position).getID());
                   //Analytics.logClickEvent("MyCourseActivity","Edit course btn");
                    ctx.startActivity(intent);
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Analytics.logClickEvent("MyCourseActivity","Delete course btn");
                    deleteCourse(position);
                }
            });
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(ctx.getResources(),R.drawable.course_name_48dp,ctx.getTheme());
            holder.courseIcon.setImageDrawable(Util.getTintDrawable(vectorDrawableCompat, Util.getColorfromindex(ctx,courseList.get(position).getCOLOR())));
            holder.title.setText(courseList.get(position).getNAME());
            String sub =  String.format(ctx.getString(R.string.my_course_subtitle),courseList.get(position).getCODE(),courseList.get(position).getECTS());
            holder.subtitle.setText(sub.replaceAll("null", " - "));
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
        return view;
    }

    private void deleteCourse(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.delete_) + courseList.get(pos).getNAME());
        builder.setMessage(R.string.delete_course_msg);
        builder.setIcon(ctx.getResources().getDrawable(R.drawable.delete_btn_selector));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StudentDB db = new StudentDB(ctx);
                db.deleteCourse(courseList.get(pos).getID());
                ((MyCourseActivity) ctx).onResume();
                Toast.makeText(ctx, R.string.deleted, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }
}