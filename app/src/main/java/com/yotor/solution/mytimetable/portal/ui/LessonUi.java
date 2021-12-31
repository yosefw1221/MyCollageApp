package com.yotor.solution.mytimetable.portal.ui;

import static com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper.getEnabledDay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.CourseAddUpdate;
import com.yotor.solution.mytimetable.portal.CourseView;
import com.yotor.solution.mytimetable.portal.CreateLessonDialog;
import com.yotor.solution.mytimetable.portal.TimetableChangeListener;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import java.util.Calendar;
import java.util.Locale;


class LessonUi {
    private Context ctx;
    private int HEIGHT;
    private int WIDTH;
    private int TODAY;
    private int HOUR;
    private int MINUTE;
    private int DAY_HIGHT;
    private int MAX_TIME_WIDTH;
    private int CELL_WIDTH;
    private int CELL_HEIGHT;
    private TimetableChangeListener changeListener;
    private LessonTimeHelper helper;

    LessonUi(Context context, int height, int width, TimetableChangeListener listener) {
        int DPI = context.getResources().getDisplayMetrics().densityDpi;
        changeListener = listener;
        ctx = context;
        HEIGHT = height;
        DAY_HIGHT = (int) dpToPx(25);
        helper = new LessonTimeHelper(ctx);
         int days = getEnabledDay(ctx).length;
        if (width<=dpToPx(60))
            WIDTH = width;
        else{
            WIDTH = (int) (width+(width-dpToPx(60))/days);
        }
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        TODAY = calendar.get(Calendar.DAY_OF_WEEK);
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);
    }
    View getCourseItem(final Lesson lesson) {
        if (lesson.getCOURSE() == null || lesson.getCOURSE().getNAME().equals("")) {
            return getEmptyLessonItem(lesson);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.lesson_timetable_item, null, false);
        TextView COURSE_VIEW = view.findViewById(R.id.course_item);
        COURSE_VIEW.setWidth(WIDTH);
        COURSE_VIEW.setHeight(HEIGHT);
        COURSE_VIEW.setText(lesson.getCOURSE().getABBREVIATION());
        if (isCurrentLesson(lesson.getDAY(),lesson.getLESSON())){
            COURSE_VIEW.setBackgroundResource(R.drawable.currentlessonbg);
        }else {
            Drawable tint = Util.getTintDrawable(ctx.getResources().getDrawable(R.drawable.color_default), Util.getColorfromindex(ctx, lesson.getCOURSE().getCOLOR()));
            COURSE_VIEW.setBackground(tint);
        }
        COURSE_VIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CourseView.class);
                intent.putExtra("id", lesson.getID());
                intent.putExtra("courseID", lesson.getCOURSE().getID());
               //Analytics.logClickEvent("LESSON_UI_Item","CourseView");
                ctx.startActivity(intent);

            }
        });
        COURSE_VIEW.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(ctx, CourseAddUpdate.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("courseID", lesson.getCOURSE().getID());
               //Analytics.logClickEvent("LESSON_UI_Item_longClick","CourseAddUpdate");
                ctx.startActivity(intent);
                return false;
            }
        });
        return view;
    }

    private View getEmptyLessonItem(final Lesson lesson) {
        final boolean[] doubleClick = {false};
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.lesson_timetable_item, null, false);
        TextView EMPTY_VIEW = view.findViewById(R.id.course_item);
        EMPTY_VIEW.setWidth(WIDTH);
        EMPTY_VIEW.setHeight(HEIGHT);
        EMPTY_VIEW.setText("");
        if (isCurrentLesson(lesson.getDAY(),lesson.getLESSON()))
            EMPTY_VIEW.setBackgroundResource(R.drawable.currentlessonbg);
        else
             EMPTY_VIEW.setBackgroundResource(R.drawable.empty_lesson_selector);
        if (lesson.getSTART_TIME() <= Util.getfLesontime(HOUR, MINUTE) && lesson.getEND_TIME() >= Util.getfLesontime(HOUR, MINUTE) && lesson.getDAY() == TODAY) {
            EMPTY_VIEW.setBackgroundResource(R.drawable.currentlessonbg);
        }
        EMPTY_VIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!doubleClick[0]) {
                    doubleClick[0] = true;
                    CreateLessonDialog dialog = new CreateLessonDialog(ctx);
                    dialog.getDialog(lesson.getID(), changeListener).show();
                   //Analytics.logClickEvent("LESSON_UI_EmptyItem","CreateLessonDialog");
                } else {
                    doubleClick[0] = false;

                }
            }
        });
        EMPTY_VIEW.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CreateLessonDialog dialog = new CreateLessonDialog(ctx);
                dialog.getDialog(lesson.getID(), changeListener).show();
               //Analytics.logClickEvent("LESSON_UI_EmptyItem_longClick","CreateLessonDialog");
                // ctx.startActivity(new Intent(ctx, CourseAddUpdate.class));
                return false;
            }
        });
        return view;
    }

    View getDayItem(String date, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.lesson_timetable_item, null, true);
        TextView DAY_VIEW = view.findViewById(R.id.course_item);
        DAY_VIEW.setWidth(WIDTH);
        DAY_VIEW.setHeight(DAY_HIGHT);
        if (i == -1)
            DAY_VIEW.setWidth(WIDTH < dpToPx(60) ? WIDTH : (int) dpToPx(60));
        if (i == TODAY)
            DAY_VIEW.setTypeface(Typeface.DEFAULT_BOLD);
        DAY_VIEW.setText(date);
        //DAY_VIEW.setText(date.substring(0, 3));
        //DAY_VIEW.setTextColor(Color.WHITE);
        DAY_VIEW.setBackgroundResource(R.drawable.day_text_bg);
        return view;
    }

    View getTimeItem(String time) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.lesson_timetable_item, null, false);
        TextView TIME_VIEW = view.findViewById(R.id.course_item);
        TIME_VIEW.setWidth(WIDTH < dpToPx(60) ? WIDTH : (int) dpToPx(60));
        TIME_VIEW.setHeight(HEIGHT);
        TIME_VIEW.setText(time);
        //TIME_VIEW.setTextColor(Color.WHITE);
        TIME_VIEW.setBackgroundResource(R.drawable.lesson_text_bg);
        return view;
    }
    private boolean isCurrentLesson(int day ,int lesson){
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK)!=day)
            return false;
        int s = helper.getLessonTime(lesson).getTimeInt();
        int e = s+LessonTimeHelper.getLESSON_DURATION(ctx);
        int now = cal.get(Calendar.HOUR_OF_DAY)*60+cal.get(Calendar.MINUTE);

        return (s<=now&&e>now);
    }
    private float dpToPx(int dp){
        float val = dp*2;
       try {
         return dp* ctx.getResources().getDisplayMetrics().density;
       }catch (Exception e){
          //Crashlytics.logException(e);
       }
    return val;
    }
}