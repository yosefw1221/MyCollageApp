package com.yotor.solution.mytimetable.portal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.GpaList;

import java.util.List;

public class GpaListAdapter extends ArrayAdapter {
    private List<GpaList> gpaLists;
    private Context ctx;


    GpaListAdapter(Context context, List<GpaList> lists) {
        super(context, 0, lists);
        gpaLists = lists;
        ctx = context;
    }

    @Override
    public int getCount() {
        return gpaLists.size();
    }

    class Holder {
        AppCompatTextView title;
        AppCompatTextView subtitle;
        AppCompatButton delete;
    }


    @SuppressLint("DefaultLocale")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(ctx).inflate(R.layout.gpa_list_item, parent,false);
            holder.title = view.findViewById(R.id.gpa_list_name);
            holder.subtitle = view.findViewById(R.id.gpa_list_subtitle);
            holder.delete = view.findViewById(R.id.gpa_delete_btn);
            view.setTag(holder);
        }
        holder = (Holder)view.getTag();
        holder.title.setText(gpaLists.get(position).getName());
        holder.subtitle.setText(String.format(ctx.getString(R.string.grade_),gpaLists.get(position).getGpa()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGpa(gpaLists.get(position).getName());
            }
        });
        return view;
    }

    private void deleteGpa(final String name) {
       //Analytics.logClickEvent("GpaList","Gpa Delete clicked");
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.delete_)+name)
                .setMessage(ctx.getString(R.string.are_you_sure))
                .setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     if (new StudentDB(ctx).deleteGpa(name)) {
                        //Analytics.logAppEvent("GpaList","Gpa Deleted");
                         Toast.makeText(ctx, ctx.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                     }else
                         Toast.makeText(ctx, ctx.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                        ((GpaListActivity)ctx).onResume();
                    }
                })
                .setNegativeButton(ctx.getString(R.string.no),null);
        builder.show();

    }
}