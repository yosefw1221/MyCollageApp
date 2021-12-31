package com.yotor.solution.mytimetable.portal.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExamTaskAdapter extends ArrayAdapter {
    private Context ctx;
    private List<ExamTaskModel> examTaskList;

    public ExamTaskAdapter(@NonNull Context context, List<ExamTaskModel>examTaskModelList) {
        super(context, 0,examTaskModelList);
        ctx = context;
        examTaskList = examTaskModelList;
    }

    @Override
    public int getCount() {
        return examTaskList.size();
    }

    class Holder{
        AppCompatImageView icon;
        AppCompatTextView date;
        AppCompatTextView title;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Holder holder;
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(ctx.getResources(),R.drawable.ic_check_circle_green_800_18dp,ctx.getTheme());
        if (view == null){
            holder = new Holder();
            view = LayoutInflater.from(ctx).inflate(R.layout.examtask_item,parent,false);
            holder.icon = view.findViewById(R.id.examtask_icon);
            holder.date = view.findViewById(R.id.examtask_date);
            holder.title = view.findViewById(R.id.examtask_info);
            holder.icon.setImageResource(examTaskList.get(position).getTYPE()==0?R.drawable.exam:R.drawable.ic_event_note_pink_500_24dp);
            holder.date.setText(getDate(examTaskList.get(position).getDATE()));
            holder.date.setBackgroundColor(Util.getColorfromindex(ctx,examTaskList.get(position).getColor()));
            holder.title.setText(examTaskList.get(position).getTOPIC());
            if (examTaskList.get(position).isDONE())
                holder.title.setCompoundDrawablesRelative(vectorDrawableCompat,null,null,null);
            view.setTag(holder);
        }else{
            holder = (Holder)view.getTag();
            holder.icon.setImageResource(examTaskList.get(position).getTYPE()==0?R.drawable.exam:R.drawable.ic_event_note_pink_500_24dp);
            holder.date.setText(getDate(examTaskList.get(position).getDATE()));
            holder.title.setText(examTaskList.get(position).getTOPIC());
            if (examTaskList.get(position).isDONE())
                holder.title.setCompoundDrawables(vectorDrawableCompat,null,null,null);
            holder.date.setBackgroundColor(Util.getColorfromindex(ctx,examTaskList.get(position).getColor()));
        }
        return view;

    }

    private String getDate(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String mon = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,new Locale("am"));
        String day = Util.intToStr(cal.get(Calendar.DAY_OF_MONTH));
        return mon+" "+day;
    }
}
