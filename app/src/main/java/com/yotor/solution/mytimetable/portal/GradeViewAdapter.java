package com.yotor.solution.mytimetable.portal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.model.GradeModel;
import com.yotor.solution.mytimetable.portal.ui.SpinnerListener;

import java.util.List;


public class GradeViewAdapter extends ArrayAdapter{
    private List<GradeModel> lessonList;
    private Context ctx;
    private SpinnerAdapter adapter;
    private SpinnerListener listener;


    GradeViewAdapter(Context context, List<GradeModel> lessons, SpinnerListener spinnerListener) {
        super(context, 0,lessons);
        listener = null;
        listener = spinnerListener;
        lessonList = lessons;
        ctx = context;
        String[] grade = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "FX", "F"};
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, grade);
    }

    @Override
    public int getCount() {
        return lessonList.size();
    }
    class Holder{
        TextView courseNameTv;
        TextView ectsTv;
        AppCompatImageView icon;
        Spinner gradeSpinner;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         Holder holder;
        View view = convertView;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(ctx).inflate(R.layout.gpa_item_view, parent,false);
            holder.icon = view.findViewById(R.id.gpa_course_icon);
            holder.courseNameTv = view.findViewById(R.id.course_name);
            holder.ectsTv = view.findViewById(R.id.gpa_item_detail_tv);
            holder.gradeSpinner = view.findViewById(R.id.gpa_grade_spinner);
            holder.gradeSpinner.setAdapter(adapter);
            view.setTag(holder);
        }
        try{
            holder = (Holder)view.getTag();
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(ctx.getResources(),R.drawable.course_code_18dp,ctx.getTheme());
            Drawable icon = Util.getTintDrawable(vectorDrawableCompat,Util.getColorfromindex(ctx,lessonList.get(position).getCOLOR()));
            holder.icon.setImageDrawable(icon);
            holder.courseNameTv.setText(lessonList.get(position).getCOURSENAME());
            holder.gradeSpinner.setSelection(lessonList.get(position).getSelectedGRADE());
            final Holder finalHolder = holder;
            holder.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    lessonList.get(position).setSelectedGRADE(pos);
                    if (listener !=null)
                    listener.onItemSelected();
                   int ects = lessonList.get(position).getECTS();
                    finalHolder.ectsTv.setText(String.format(ctx.getString(R.string.grade_credit_detail),ects,ects*Util.getGradeMultiplyer(pos),ects*Util.getGradeMultiplyer(0)));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){
           //Crashlytics.logException(e);
        }

    return view;
    }
}
