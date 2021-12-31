package com.yotor.solution.mytimetable.portal.mywidget;

import android.content.Context;
import android.util.Log;

import com.yotor.solution.mytimetable.portal.ShardPref;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.ExamTaskModel;
import com.yotor.solution.mytimetable.portal.model.Lesson;
import com.yotor.solution.mytimetable.ui.Setting.LessonTimeHelper;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class WidgetData {
    private Context ctx;
    private StudentDB db;

    public WidgetData(Context context) {
        this.ctx = context;
        db = new StudentDB(ctx);

    }

    public List<WidgetDataModel> getLessons(int day) {
        List<WidgetDataModel> widgetDataList = new LinkedList<>();
        LessonTimeHelper helper = new LessonTimeHelper(ctx);
        WidgetDataModel data;
        int totalLesson = ShardPref.getInt(ctx, "total_lesson", 15);
        List<Lesson> list = db.getLessonOfDay(day);
        Log.e("Size of day lesson " + day, "list size " + list.size());
        for (int i = 0; i < list.size(); i++) {
            data = new WidgetDataModel();
            Log.e("", "");
            if (list.get(i).getLESSON() > totalLesson) continue;
            if (i > 0 && list.get(i - 1).getCOURSE().getID() == list.get(i).getCOURSE().getID()) {
                Log.e("Duplicate lesson" + list.get(i).getCOURSE().getID() + " with " + list.get(i - 1).getCOURSE().getID(), list.get(i).getCOURSE().getNAME());
                int totalLess = widgetDataList.get(widgetDataList.size() - 1).getTotalLesson();
                widgetDataList.get(widgetDataList.size() - 1).setTotalLesson(totalLess + 1);
                widgetDataList.get(widgetDataList.size() - 1).setEndTime(helper.getLessonTime(list.get(i).getLESSON()).getDateTime().plusMinutes(LessonTimeHelper.getLESSON_DURATION(ctx)));
                //    widgetDataList.get(widgetDataList.size()-1).set(widgetDataList.get(widgetDataList.size()-1).getTotalLesson()+1);
            } else {
                data.setCourseName(list.get(i).getCOURSE().getNAME());
                data.setStartTime(helper.getLessonTime(list.get(i).getLESSON()).getDateTime());
                data.setEndTime(helper.getLessonTime(list.get(i).getLESSON()).getDateTime().plusMinutes(LessonTimeHelper.getLESSON_DURATION(ctx)));
                data.setTotalLesson(1);
                Log.e("lesson Added " + list.get(i).getCOURSE().getID(), list.get(i).getCOURSE().getNAME());
                widgetDataList.add(data);
            }
        }
        return widgetDataList;
    }

    public List<WidgetDataModel> getExamTaskList() {
        List<ExamTaskModel> taskModels = db.getAllTaskExam(0);
        List<WidgetDataModel> examtaskList = new LinkedList<>();
        WidgetDataModel data;
        for (ExamTaskModel model : taskModels) {
            if (model.isDONE()) continue;
            if (model.getDATE() < System.currentTimeMillis()) continue;
            data = new WidgetDataModel();
            data.setCourseName(model.getTOPIC());
            data.setStartTime(DateTime.now().withMillis(model.getDATE()));
            data.setSubTitle(model.getCOURSENAME() == null ?/*Empty Subtitle */model.getNOTES() : model.getCOURSENAME());
            examtaskList.add(data);
        }
        return examtaskList;
    }
}
