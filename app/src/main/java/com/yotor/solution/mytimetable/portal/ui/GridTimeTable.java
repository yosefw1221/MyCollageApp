package com.yotor.solution.mytimetable.portal.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TableRow;

//import com.crashlytics.android.Crashlytics;
import com.yotor.solution.mytimetable.portal.TimetableChangeListener;
import com.yotor.solution.mytimetable.portal.Util;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import java.util.LinkedList;
import java.util.List;


public class GridTimeTable {
    private int CELL_HEIGHT;
    private Context ctx;
    private LessonUi LESSON_UI;
    private LessonTimeHelper timeHelper;
    private String[] Days;

    public GridTimeTable(Context context, int width, int height, TimetableChangeListener listener) {
        ctx = context;
        timeHelper = new LessonTimeHelper(ctx);
        Days = Util.getDaysRowTitle(LessonTimeHelper.getEnabledDay(ctx));
        int DayHeight = (int) dpToPx(25);
        int high = height-DayHeight;
        int h = Util.getCellHight(high, LessonTimeHelper.getTotalLesson(ctx));
        CELL_HEIGHT = h<dpToPx(60)? (int) dpToPx(60) :h;
        int CELL_WIDTH = Util.getCellWidth(width, Days.length);
        LESSON_UI = new LessonUi(context, CELL_HEIGHT, CELL_WIDTH, listener);
    }
    public synchronized TableRow getHeader() {
        final TableRow header = new TableRow(ctx);
        TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        header.setLayoutParams(param);
        final String[] enabledDay = LessonTimeHelper.getEnabledDay(ctx);
        new Runnable() {
            @Override
            public void run() {
                int i = -1;
                for (String day : Days) {
                    if (i==-1)
                        header.addView(LESSON_UI.getDayItem(day, -1));
                    else
                        header.addView(LESSON_UI.getDayItem(day, Integer.parseInt(enabledDay[i])));
                    i++;
                }
            }
        }.run();
        return header;
    }

    public synchronized List<TableRow> getLessonsRows() {
        StudentDB db = new StudentDB(ctx);
        final List<List<Lesson>> listList = db.getAllLessons();
        final List<TableRow> rows = new LinkedList<>();
        final TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listList.size(); i++) {
                    TableRow row = new TableRow(ctx);
                    int GAP = (timeHelper.getLessonGapMinute(i) * CELL_HEIGHT) / LessonTimeHelper.getLESSON_DURATION(ctx);
                    row.setPadding(0, GAP / 2, 0, 0);
                    row.setLayoutParams(param);
                    row.addView(LESSON_UI.getTimeItem(timeHelper.getLessonTime(i + 1).getLstTimeString()));
                    for (Lesson course : listList.get(i)) {
                        row.addView(LESSON_UI.getCourseItem(course));
                    }
                    rows.add(row);
                }
            }
        }.run();
        return rows;
    }
    private float dpToPx(int dp){
        float val = dp*2;
        try {
            val = dp* ctx.getResources().getDisplayMetrics().density;
        }catch (Exception e){
           //Crashlytics.logException(e);
        }
        return val;
    }
}