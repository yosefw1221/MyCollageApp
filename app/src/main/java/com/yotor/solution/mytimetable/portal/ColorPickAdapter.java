package com.yotor.solution.mytimetable.portal;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yotor.solution.mytimetable.R;

public class ColorPickAdapter extends ArrayAdapter {
    private String[] Colors;
    private Context ctx;


    ColorPickAdapter(Context context) {
        super(context, 0, new String[10]);
        Colors = context.getResources().getStringArray(R.array.Colors);
        Log.e("Colors", Colors[3] + "" + Colors.length);
        ctx = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    class Holder {
        TextView tv;
        ImageView image;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = new Holder();
        if (view == null)
            view = LayoutInflater.from(ctx).inflate(R.layout.lesson_spinner_item, parent,false);
        holder.tv = view.findViewById(R.id.course_name_item);
        holder.image = view.findViewById(R.id.course_color_view);
        holder.tv.setText(String.format(ctx.getString(R.string.color) + " %d", (position + 1)));
        holder.image.setBackgroundColor(Color.parseColor(Colors[position]));
        return view;
    }
}